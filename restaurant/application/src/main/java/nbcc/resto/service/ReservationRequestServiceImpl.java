package nbcc.resto.service;

import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.email.domain.EmailRequest;
import nbcc.email.service.EmailService;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.ReservationGuestDetail;
import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.ReservationRequestSummary;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.repository.PopUpEventRepository;
import nbcc.resto.repository.ReservationRequestRepository;
import nbcc.resto.repository.SeatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationRequestServiceImpl implements ReservationRequestService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationRequestServiceImpl.class);
    private static final DateTimeFormatter EMAIL_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private final ReservationRequestRepository reservationRequestRepository;
    private final SeatingRepository seatingRepository;
    private final PopUpEventRepository popUpEventRepository;
    private final EmailService emailService;

    public ReservationRequestServiceImpl(ReservationRequestRepository reservationRequestRepository,
                                         SeatingRepository seatingRepository,
                                         PopUpEventRepository popUpEventRepository,
                                         EmailService emailService) {
        this.reservationRequestRepository = reservationRequestRepository;
        this.seatingRepository = seatingRepository;
        this.popUpEventRepository = popUpEventRepository;
        this.emailService = emailService;
    }

    @Override
    public ValidationResults<List<PopUpEvent>> getActiveEventsForReserve() {
        try {
            var all = popUpEventRepository.getAll();
            var list = all != null ? all.stream()
                    .filter(e -> e.getId() != null && !Boolean.TRUE.equals(e.getIsArchived()))
                    .sorted(Comparator.comparing(PopUpEvent::getStartDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList()) : List.<PopUpEvent>of();
            return ValidationResults.success(list);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<List<Seating>> getActiveSeatingsForReserve() {
        try {
            var all = seatingRepository.getAll();
            var list = all != null ? all.stream()
                    .filter(s -> s.getId() != null && !s.isArchived())
                    .sorted(Comparator.comparing(Seating::getStartDateTime, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList()) : List.<Seating>of();
            return ValidationResults.success(list);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<ReservationRequest> create(ReservationRequest request) {
        try {
            List<ValidationError> errors = new ArrayList<>();

            if (request.getFirstName() == null || request.getFirstName().isBlank()) {
                errors.add(new ValidationError("First name is required", "firstName"));
            }
            if (request.getLastName() == null || request.getLastName().isBlank()) {
                errors.add(new ValidationError("Last name is required", "lastName"));
            }
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                errors.add(new ValidationError("Email is required", "email"));
            } else if (!request.getEmail().contains("@")) {
                errors.add(new ValidationError("Please enter a valid email address", "email"));
            }
            if (request.getGroupSize() < 1) {
                errors.add(new ValidationError("Group size must be at least 1", "groupSize"));
            }
            if (request.getSeatingId() == null) {
                errors.add(new ValidationError("Please select a seating", "seatingId"));
            }

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(request, errors);
            }

            var seatingOpt = seatingRepository.get(request.getSeatingId());
            if (seatingOpt.isEmpty()) {
                errors.add(new ValidationError("Selected seating is not available", "seatingId"));
                return ValidationResults.invalid(request, errors);
            }
            Seating seating = seatingOpt.get();
            if (seating.isArchived()) {
                errors.add(new ValidationError("Selected seating is not available", "seatingId"));
                return ValidationResults.invalid(request, errors);
            }

            var eventOpt = popUpEventRepository.get(seating.getEventId());
            if (eventOpt.isEmpty() || Boolean.TRUE.equals(eventOpt.get().getIsArchived())) {
                errors.add(new ValidationError("The event for this seating is not available", "seatingId"));
                return ValidationResults.invalid(request, errors);
            }

            request.setStatus(ReservationRequestStatus.PENDING);
            request.setUuid(UUID.randomUUID());
            request.setCreatedAt(LocalDateTime.now());

            ReservationRequest created = reservationRequestRepository.create(request);
            sendReservationStatusEmail(created);
            return ValidationResults.success(created);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<List<ReservationRequestSummary>> listForEmployee(Long eventId, ReservationRequestStatus status) {
        try {
            var list = reservationRequestRepository.findSummaries(eventId, status);
            return ValidationResults.success(list != null ? list : List.of());
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    //Method implementation to receive the list of approved reservations
    @Override
    public ValidationResults<List<ReservationRequestSummary>> listApprovedForEmployee(Long eventId) {
        return listForEmployee(eventId, ReservationRequestStatus.APPROVED);
    }

    @Override
    public Optional<ReservationRequest> get(Long id) {
        return reservationRequestRepository.get(id);
    }

    @Override
    public Optional<ReservationGuestDetail> findGuestDetailByUuid(UUID uuid) {
        if (uuid == null) {
            return Optional.empty();
        }
        var reqOpt = reservationRequestRepository.getByUuid(uuid);
        if (reqOpt.isEmpty()) {
            return Optional.empty();
        }
        var req = reqOpt.get();
        var seatingOpt = seatingRepository.get(req.getSeatingId());
        if (seatingOpt.isEmpty()) {
            return Optional.empty();
        }
        var seating = seatingOpt.get();
        var eventOpt = popUpEventRepository.get(seating.getEventId());
        if (eventOpt.isEmpty()) {
            return Optional.empty();
        }
        var event = eventOpt.get();
        return Optional.of(new ReservationGuestDetail(
                req.getUuid(),
                req.getFirstName(),
                req.getLastName(),
                req.getGroupSize(),
                req.getStatus(),
                event.getName(),
                seating.getStartDateTime()));
    }

    @Override
    public ValidationResults<ReservationRequest> approve(Long id, Long tableId) {
        try {
            List<ValidationError> errors = new ArrayList<>();
            if (id == null) {
                errors.add(new ValidationError("Reservation request id is required", "id"));
                return ValidationResults.invalid(null, errors);
            }
            if (tableId == null) {
                errors.add(new ValidationError("A table must be selected to approve", "tableId"));
                return ValidationResults.invalid(null, errors);
            }

            var reqOpt = reservationRequestRepository.get(id);
            if (reqOpt.isEmpty()) {
                return ValidationResults.invalid(null, List.of(new ValidationError("Reservation request not found")));
            }

            var req = reqOpt.get();
            if (req.getStatus() == ReservationRequestStatus.APPROVED) {
                errors.add(new ValidationError("An approved reservation cannot be changed", "status"));
                return ValidationResults.invalid(req, errors);
            }

            var seatingOpt = seatingRepository.get(req.getSeatingId());
            if (seatingOpt.isEmpty() || seatingOpt.get().isArchived()) {
                errors.add(new ValidationError("Seating is not available", "seatingId"));
                return ValidationResults.invalid(req, errors);
            }
            var seating = seatingOpt.get();
            if (seating.getTableIds() == null || !seating.getTableIds().contains(tableId)) {
                errors.add(new ValidationError("Selected table is not part of this seating", "tableId"));
                return ValidationResults.invalid(req, errors);
            }

            if (reservationRequestRepository.isTableReservedForSeating(seating.getId(), tableId)) {
                errors.add(new ValidationError("That table is already reserved for this seating", "tableId"));
                return ValidationResults.invalid(req, errors);
            }

            req.setStatus(ReservationRequestStatus.APPROVED);
            req.setTableId(tableId);
            var updated = reservationRequestRepository.update(req);
            sendReservationStatusEmail(updated);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<ReservationRequest> deny(Long id) {
        try {
            List<ValidationError> errors = new ArrayList<>();
            if (id == null) {
                errors.add(new ValidationError("Reservation request id is required", "id"));
                return ValidationResults.invalid(null, errors);
            }

            var reqOpt = reservationRequestRepository.get(id);
            if (reqOpt.isEmpty()) {
                return ValidationResults.invalid(null, List.of(new ValidationError("Reservation request not found")));
            }

            var req = reqOpt.get();
            if (req.getStatus() == ReservationRequestStatus.APPROVED) {
                errors.add(new ValidationError("An approved reservation cannot be changed", "status"));
                return ValidationResults.invalid(req, errors);
            }

            req.setStatus(ReservationRequestStatus.DENIED);
            req.setTableId(null);
            var updated = reservationRequestRepository.update(req);
            sendReservationStatusEmail(updated);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    private void sendReservationStatusEmail(ReservationRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
                return;
            }

            var seatingOpt = seatingRepository.get(request.getSeatingId());
            if (seatingOpt.isEmpty()) {
                return;
            }
            var seating = seatingOpt.get();

            var eventOpt = popUpEventRepository.get(seating.getEventId());
            if (eventOpt.isEmpty()) {
                return;
            }
            var event = eventOpt.get();

            String subject = switch (request.getStatus()) {
                case APPROVED -> "Your reservation request was approved";
                case DENIED -> "Your reservation request was denied";
                default -> "Your reservation request was received";
            };

            String body = buildStatusEmailBody(request, event, seating);
            var emailRequest = new EmailRequest()
                    .setTo(request.getEmail())
                    .setSubject(subject)
                    .setBody(body);

            if (emailService.sendEmail(emailRequest)) {
                logger.info("Reservation email sent to {}", request.getEmail());
            } else {
                logger.warn("Reservation email failed to send to {}", request.getEmail());
            }
        } catch (Exception ex) {
            logger.error("Error while sending reservation email for request {}", request != null ? request.getId() : null, ex);
        }
    }

    private String buildStatusEmailBody(ReservationRequest request, PopUpEvent event, Seating seating) {
        String seatingDateTime = seating.getStartDateTime() != null
                ? seating.getStartDateTime().format(EMAIL_DATETIME_FORMATTER)
                : "N/A";

        return "Reservation update\n\n" +
                "Event Name: " + (event.getName() != null ? event.getName() : "N/A") + "\n" +
                "Seating Date and Time: " + seatingDateTime + "\n" +
                "Guest First Name: " + (request.getFirstName() != null ? request.getFirstName() : "") + "\n" +
                "Guest Last Name: " + (request.getLastName() != null ? request.getLastName() : "") + "\n" +
                "Group Size: " + request.getGroupSize() + "\n" +
                "Request Status: " + request.getStatus();
    }
}

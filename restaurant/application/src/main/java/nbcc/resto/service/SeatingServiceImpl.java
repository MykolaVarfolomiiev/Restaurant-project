package nbcc.resto.service;

import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.DiningTable;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.repository.ReservationRequestRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.SeatingValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeatingServiceImpl implements SeatingService {

    private final SeatingRepository seatingRepository;
    private final TableService tableService;
    private final PopUpEventService popUpEventService;
    private final SeatingValidationService seatingValidationService;
    private final ReservationRequestRepository reservationRequestRepository;

    public SeatingServiceImpl(SeatingRepository seatingRepository, TableService tableService, PopUpEventService popUpEventService, SeatingValidationService seatingValidationService, ReservationRequestRepository reservationRequestRepository) {
        this.seatingRepository = seatingRepository;
        this.tableService = tableService;
        this.popUpEventService = popUpEventService;
        this.seatingValidationService = seatingValidationService;
        this.reservationRequestRepository = reservationRequestRepository;
    }

    @Override
    public ValidationResults<List<Seating>> getAll() {
        var list = seatingRepository.getAll();
        var nonArchived = list != null ? list.stream()
                .filter(s -> !s.isArchived())
                .sorted(Comparator.comparing(Seating::getStartDateTime))
                .collect(Collectors.toList()) : new ArrayList<Seating>();
        return ValidationResults.success(nonArchived);
    }

    @Override
    public ValidationResults<Seating> get(Long id) {
        var seating = seatingRepository.get(id);
        return ValidationResults.success(seating);
    }

    @Override
    public ValidationResults<Seating> create(Seating seating) {
        try {
            List<ValidationError> errors = new ArrayList<>();

            if (seating.getName() == null || seating.getName().isBlank()) {
                errors.add(new ValidationError("Name is required", "name"));
            }
            if (seating.getEventId() == null) {
                errors.add(new ValidationError("Event is required", "eventId"));
            }
            if (seating.getStartDateTime() == null) {
                errors.add(new ValidationError("Start date and time is required", "startDateTime"));
            }
            if (seating.getTableIds() == null || seating.getTableIds().isEmpty()) {
                errors.add(new ValidationError("At least one table must be selected", "tableIds"));
            }

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(seating, errors);
            }

            var eventResult = popUpEventService.get(seating.getEventId());
            if (eventResult.isEmpty() || eventResult.isError()) {
                errors.add(new ValidationError("Invalid event", "eventId"));
                return ValidationResults.invalid(seating, errors);
            }

            PopUpEvent event = eventResult.getValue();
            if (seating.getDurationMinutes() < 1) {
                seating.setDurationMinutes(event.getDurationInMinutes() != null && event.getDurationInMinutes() >= 1
                        ? event.getDurationInMinutes() : 60);
            }
            if (seating.getDurationMinutes() < 1) {
                errors.add(new ValidationError("Duration must be at least 1 minute", "durationMinutes"));
                return ValidationResults.invalid(seating, errors);
            }
            if (event.getDurationInMinutes() != null && seating.getDurationMinutes() > event.getDurationInMinutes()) {
                errors.add(new ValidationError("Duration cannot be greater than the event duration (" +
                        event.getDurationInMinutes() + " minutes)", "durationMinutes"));
                return ValidationResults.invalid(seating, errors);
            }

            seatingValidationService.validateSeatingWithinEventRange(seating, event).ifPresent(errors::add);
            if (!errors.isEmpty()) {
                return ValidationResults.invalid(seating, errors);
            }

            var tablesResult = tableService.getAll();
            if (tablesResult.isError()) {
                return ValidationResults.error();
            }
            var allTables = tablesResult.getValue();
            var validTableIds = allTables.stream().map(DiningTable::getId).collect(Collectors.toSet());
            for (Long tid : seating.getTableIds()) {
                if (!validTableIds.contains(tid)) {
                    errors.add(new ValidationError("Invalid table selected", "tableIds"));
                    return ValidationResults.invalid(seating, errors);
                }
            }

            LocalDateTime start = seating.getStartDateTime();
            LocalDateTime end = start.plusMinutes(seating.getDurationMinutes());

            var allSeatings = seatingRepository.getAll();
            if (allSeatings != null) {
                for (Seating existing : allSeatings) {
                    if (existing.isArchived()) continue;
                    LocalDateTime exStart = existing.getStartDateTime();
                    LocalDateTime exEnd = exStart.plusMinutes(existing.getDurationMinutes());

                    for (Long tableId : seating.getTableIds()) {
                        if (existing.getTableIds().contains(tableId)) {
                            if (start.isBefore(exEnd) && end.isAfter(exStart)) {
                                var tableOpt = allTables.stream().filter(t -> t.getId().equals(tableId)).findFirst();
                                String tableName = tableOpt.map(DiningTable::getName).orElse("Table");
                                errors.add(new ValidationError(
                                        tableName + " is already in use during this time. Next available at " + formatTime(exEnd) + ".",
                                        "tableIds"));
                                return ValidationResults.invalid(seating, errors);
                            }
                        }
                    }
                }
            }

            seating.setCreatedDate(LocalDateTime.now());
            Seating created = seatingRepository.create(seating);
            return ValidationResults.success(created);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<Seating> update(Seating seating) {
        try {
            List<ValidationError> errors = new ArrayList<>();

            if (seating.getId() == null) {
                errors.add(new ValidationError("Seating ID is required for update", "id"));
                return ValidationResults.invalid(seating, errors);
            }
            if (seating.getName() == null || seating.getName().isBlank()) {
                errors.add(new ValidationError("Name is required", "name"));
            }
            if (seating.getEventId() == null) {
                errors.add(new ValidationError("Event is required", "eventId"));
            }
            if (seating.getStartDateTime() == null) {
                errors.add(new ValidationError("Start date and time is required", "startDateTime"));
            }

            if (!errors.isEmpty()) {
                return ValidationResults.invalid(seating, errors);
            }

            var existingOpt = seatingRepository.get(seating.getId());
            if (existingOpt.isEmpty()) {
                errors.add(new ValidationError("Seating not found", "id"));
                return ValidationResults.invalid(seating, errors);
            }
            Seating existing = existingOpt.get();
            seating.setTableIds(existing.getTableIds());
            seating.setCreatedDate(existing.getCreatedDate());
            seating.setArchived(existing.isArchived());

            var eventResult = popUpEventService.get(seating.getEventId());
            if (eventResult.isEmpty() || eventResult.isError()) {
                errors.add(new ValidationError("Invalid event", "eventId"));
                return ValidationResults.invalid(seating, errors);
            }

            PopUpEvent event = eventResult.getValue();
            if (seating.getDurationMinutes() < 1) {
                seating.setDurationMinutes(event.getDurationInMinutes() != null && event.getDurationInMinutes() >= 1
                        ? event.getDurationInMinutes() : 60);
            }
            if (seating.getDurationMinutes() < 1) {
                errors.add(new ValidationError("Duration must be at least 1 minute", "durationMinutes"));
                return ValidationResults.invalid(seating, errors);
            }
            if (event.getDurationInMinutes() != null && seating.getDurationMinutes() > event.getDurationInMinutes()) {
                errors.add(new ValidationError("Duration cannot be greater than the event duration (" +
                        event.getDurationInMinutes() + " minutes)", "durationMinutes"));
                return ValidationResults.invalid(seating, errors);
            }

            seatingValidationService.validateSeatingWithinEventRange(seating, event).ifPresent(errors::add);
            if (!errors.isEmpty()) {
                return ValidationResults.invalid(seating, errors);
            }

            var allSeatings = seatingRepository.getAll();
            if (allSeatings != null) {
                var tablesResult = tableService.getAll();
                var allTables = tablesResult.isError() ? List.<DiningTable>of() : tablesResult.getValue();
                LocalDateTime start = seating.getStartDateTime();
                LocalDateTime end = start.plusMinutes(seating.getDurationMinutes());

                for (Seating s : allSeatings) {
                    if (s.getId() != null && s.getId().equals(seating.getId())) continue;
                    if (s.isArchived()) continue;
                    LocalDateTime exStart = s.getStartDateTime();
                    LocalDateTime exEnd = exStart.plusMinutes(s.getDurationMinutes());

                    for (Long tableId : seating.getTableIds()) {
                        if (s.getTableIds().contains(tableId)) {
                            if (start.isBefore(exEnd) && end.isAfter(exStart)) {
                                var tableOpt = allTables.stream().filter(t -> t.getId().equals(tableId)).findFirst();
                                String tableName = tableOpt.map(DiningTable::getName).orElse("Table");
                                errors.add(new ValidationError(
                                        tableName + " is already in use during this time. Next available at " + formatTime(exEnd) + ".",
                                        "tableIds"));
                                return ValidationResults.invalid(seating, errors);
                            }
                        }
                    }
                }
            }

            seating.setUpdatedAt(LocalDateTime.now());
            Seating updated = seatingRepository.update(seating);
            return ValidationResults.success(updated);
        } catch (Exception e) {
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidationResults<Seating> deleteOrArchive(Long id) {
        try {
            var existingOpt = seatingRepository.get(id);
            if (existingOpt.isEmpty()) {
                return ValidationResults.success();
            }
            Seating existing = existingOpt.get();
            int reservationCount = getReservationCountForSeating(id);

            if (reservationCount == 0) {
                seatingRepository.delete(id);
                return ValidationResults.success();
            } else {
                existing.setArchived(true);
                existing.setUpdatedAt(LocalDateTime.now());
                seatingRepository.update(existing);
                return ValidationResults.success(existing);
            }
        } catch (Exception e) {
            if (seatingRepository.get(id).isEmpty()) {
                return ValidationResults.success();
            }
            return ValidationResults.error(e);
        }
    }

    private int getReservationCountForSeating(Long seatingId) {
        return (int) reservationRequestRepository.countBySeatingId(seatingId);
    }

    private static String formatTime(LocalDateTime dt) {
        return dt.getMonth().toString().substring(0, 3) + " " + dt.getDayOfMonth() + ", " + dt.getYear() + " — " +
                String.format("%d:%02d %s", dt.getHour() == 0 ? 12 : dt.getHour() > 12 ? dt.getHour() - 12 : dt.getHour(),
                        dt.getMinute(), dt.getHour() < 12 ? "AM" : "PM");
    }
}

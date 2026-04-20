package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.repository.PopUpEventRepository;
import nbcc.resto.repository.ReservationRequestRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.PopUpEventValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
public class PopUpEventServiceImpl implements PopUpEventService {

    private final Logger logger = LoggerFactory.getLogger(PopUpEventServiceImpl.class);
    private final PopUpEventRepository popUpEventRepository;
    private final PopUpEventValidationService popUpEventValidationService;
    private final SeatingRepository seatingRepository;
    private final ReservationRequestRepository reservationRequestRepository;

    public PopUpEventServiceImpl(PopUpEventRepository popUpEventRepository, PopUpEventValidationService popUpEventValidationService,
                                 SeatingRepository seatingRepository, ReservationRequestRepository reservationRequestRepository) {
        this.popUpEventRepository = popUpEventRepository;
        this.popUpEventValidationService = popUpEventValidationService;
        this.seatingRepository = seatingRepository;
        this.reservationRequestRepository = reservationRequestRepository;
    }

    @Override
    public Result<Collection<PopUpEvent>> getAll() {
     return getAll(false, false);
    }

    @Override
    public Result<Collection<PopUpEvent>> getAll(boolean loadSeatings, boolean loadMenu) {
        try {
            var events = popUpEventRepository.getAll();

            if (loadSeatings) {
                var allSeatings = seatingRepository.getAll();

                for (var event : events) {
                    var eventSeatings = allSeatings.stream()
                            .filter(seating -> event.getId() != null && event.getId().equals(seating.getEventId()))
                            .toList();

                    event.setSeatings(eventSeatings);
                }
            }

            //Here Menu is automatically loaded via @ManyToOne relationship in PopUpEvent
            return ValidationResults.success(events);
        } catch (Exception e) {
            logger.error("Error retrieving all events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<PopUpEvent>> search(String name, LocalDate startDate, LocalDate endDate) {
        try {
            return ValidationResults.success(popUpEventRepository.search(name, startDate, endDate));
        } catch (Exception e) {
            logger.error("Error searching events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<PopUpEvent> get(Long id) {
        return get(id, false, false);
    }

    @Override
    public ValidatedResult<PopUpEvent> get(Long id, boolean loadSeatings, boolean loadMenu) {
        try {
            var optionalEvent = popUpEventRepository.get(id);

            if(optionalEvent.isEmpty()){
                return ValidationResults.error();
            }

            var event = optionalEvent.get();

            if(loadSeatings){
                var seatings = seatingRepository.getAll().stream()
                        .filter(seating -> id.equals(seating.getEventId()))
                        .toList();

                event.setSeatings(seatings);
            }

            //Here Menu is automatically loaded via @ManyToOne relationship in PopUpEvent
            return ValidationResults.success(event);
        } catch (Exception e) {
            logger.error("Error retrieving event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<PopUpEvent> create(PopUpEvent event) {
        try {
            event.setIsArchived(false);

            var errors = popUpEventValidationService.validate(event);

            if (errors.isEmpty()) {
                return ValidationResults.success(popUpEventRepository.create(event));
            } else {
                logger.debug("Validation errors for event create {}: {}", event, errors);
                return ValidationResults.invalid(event, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating event: {}", event, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<PopUpEvent> update(PopUpEvent event) {
        try {
            var errors = popUpEventValidationService.validate(event);

            if (errors.isEmpty()) {
                try {
                    return ValidationResults.success(popUpEventRepository.update(event));
                } catch (ConcurrencyException e) {
                    errors.add(new ValidationError("Event was modified since it was displayed, please refresh and try again"));
                }
            }

            logger.debug("Validation errors for event update {}: {}", event, errors);
            return ValidationResults.invalid(event, errors);

        } catch (Exception e) {
            logger.error("Error updating event: {}", event, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            var eventOpt = popUpEventRepository.get(id);
            if (eventOpt.isEmpty()) {
                return ValidationResults.success();
            }

            var allSeatings = seatingRepository.getAll();
            var seatingsForEvent = allSeatings == null ? List.<Seating>of() : allSeatings.stream()
                    .filter(seating -> id.equals(seating.getEventId()))
                    .toList();

            boolean anyReservation = seatingsForEvent.stream()
                    .anyMatch(s -> s.getId() != null && reservationRequestRepository.countBySeatingId(s.getId()) > 0);

            if (anyReservation) {
                PopUpEvent event = eventOpt.get();
                event.setIsArchived(true);
                event.setIsActive(false);
                event.setMenu(null);
                try {
                    popUpEventRepository.update(event);
                } catch (ConcurrencyException e) {
                    logger.warn("Concurrency updating event {} for archive", id, e);
                    return ValidationResults.error(e);
                }

                for (Seating s : seatingsForEvent) {
                    if (!s.isArchived()) {
                        s.setArchived(true);
                        s.setUpdatedAt(LocalDateTime.now());
                        seatingRepository.update(s);
                    }
                }

                logger.debug("Event with id {} archived (has reservations)", id);
                return ValidationResults.success();
            }

            PopUpEvent event = eventOpt.get().setMenu(null);
            popUpEventRepository.update(event);

            for (Seating seating : seatingsForEvent) {
                seatingRepository.delete(seating.getId());
            }

            popUpEventRepository.delete(id);
            logger.debug("Event with id {} deleted", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}

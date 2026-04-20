package nbcc.resto.domain.validation;

import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.Seating;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;


public final class SeatingEventDateRangeValidator {

    private SeatingEventDateRangeValidator() {
    }

    /**
     * @param fieldName field to attach the error to when end-of-seating spills outside the range
     */
    public record Violation(String field, String message) {
    }


    public static Optional<Violation> validate(PopUpEvent event, LocalDateTime seatingStart, int durationMinutes) {
        if (event == null || event.getStartDate() == null || event.getEndDate() == null || seatingStart == null) {
            return Optional.empty();
        }
        if (durationMinutes < 1) {
            return Optional.empty();
        }

        LocalDate eventStart = event.getStartDate();
        LocalDate eventEnd = event.getEndDate();
        LocalDateTime seatingEnd = seatingStart.plusMinutes(durationMinutes);

        LocalDate startDay = seatingStart.toLocalDate();
        LocalDate endDay = seatingEnd.toLocalDate();

        if (startDay.isBefore(eventStart) || startDay.isAfter(eventEnd)) {
            return Optional.of(new Violation("startDateTime",
                    "Seating start time must fall within the event date range (" + eventStart + " to " + eventEnd + ")"));
        }
        if (endDay.isBefore(eventStart) || endDay.isAfter(eventEnd)) {
            return Optional.of(new Violation("durationMinutes",
                    "Seating end time must fall within the event date range (" + eventStart + " to " + eventEnd + ")"));
        }
        return Optional.empty();
    }

    public static Optional<Violation> validate(PopUpEvent event, Seating seating) {
        if (seating == null) {
            return Optional.empty();
        }
        return validate(event, seating.getStartDateTime(), seating.getDurationMinutes());
    }
}

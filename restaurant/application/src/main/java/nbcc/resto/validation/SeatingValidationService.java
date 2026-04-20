package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.Seating;

import java.util.Optional;

public interface SeatingValidationService {

    /**
     * Validates that seating start and end times fall within the event's date range.
     *
     * @param seating the seating to validate
     * @param event   the event the seating belongs to
     * @return empty if valid, or a ValidationError if seating times are outside event range
     */
    Optional<ValidationError> validateSeatingWithinEventRange(Seating seating, PopUpEvent event);
}

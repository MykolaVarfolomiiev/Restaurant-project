package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.domain.validation.SeatingEventDateRangeValidator;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SeatingValidationServiceImpl implements SeatingValidationService {

    @Override
    public Optional<ValidationError> validateSeatingWithinEventRange(Seating seating, PopUpEvent event) {
        return SeatingEventDateRangeValidator.validate(event, seating)
                .map(v -> new ValidationError(v.message(), v.field()));
    }
}

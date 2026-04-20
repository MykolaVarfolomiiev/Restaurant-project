package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.Menu;

import java.util.Collection;

public interface MenuValidationService {

    Collection<ValidationError> validate(Menu menu);
}

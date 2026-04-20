package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.MenuItem;

import java.util.Collection;

public interface MenuItemValidationService {

    Collection<ValidationError> validate(MenuItem menuItem);
}

package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.PopUpEvent;
import org.springframework.stereotype.Service;

import java.util.Collection;

public interface PopUpEventValidationService {

    Collection<ValidationError> validate(PopUpEvent popUpEvent);
}

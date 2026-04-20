package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.repository.PopUpEventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class PopUpEventValidationServiceImpl implements PopUpEventValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final PopUpEventRepository popUpEventRepository;

    public PopUpEventValidationServiceImpl(AnnotationValidationService annotationValidationService, PopUpEventRepository popUpEventRepository) {
        this.annotationValidationService = annotationValidationService;
        this.popUpEventRepository = popUpEventRepository;
    }

    @Override
    public Collection<ValidationError> validate(PopUpEvent popUpEvent) {
        var errors = new ArrayList<ValidationError>();

        if (popUpEvent == null) {
            errors.add(new ValidationError("Pop-up event is required"));
            return errors;
        }

        errors.addAll(annotationValidationService.validate(popUpEvent));

        // Check for a valid Event date range
        // (End date of the Event can't be before start date)
        if (
                popUpEvent.getStartDate() != null
                        && popUpEvent.getEndDate() != null
                        && popUpEvent.getEndDate().isBefore(popUpEvent.getStartDate())
        ) {
            errors.add(new ValidationError("End date must be on or after start date", "endDate", popUpEvent.getEndDate()));
        }

        // Check for Event isActive
        // you can't do it in a regular way (by annotations), because isActive is a boolean
        if (popUpEvent.getIsActive() == null) {
            errors.add(new ValidationError("Active status is required", "isActive"));
        }

        // Check for duplicate name
        // On create: check Repository.exists(name) reject if another event already uses this name
        // On update: only reject if the name was changed and the new name already exists
        if (popUpEvent.getName() != null && !popUpEvent.getName().isBlank()) {
            var name = popUpEvent.getName().trim();

            boolean duplicateExists;

            if (popUpEvent.getId() == null) {
                duplicateExists = popUpEventRepository.exists(name);
            } else {
                var existingEvent = popUpEventRepository.get(popUpEvent.getId());

                duplicateExists = existingEvent
                        .map(existing -> !name.equalsIgnoreCase(existing.getName())
                                && popUpEventRepository.exists(name))
                        .orElseGet(() -> popUpEventRepository.exists(name));
            }

            if (duplicateExists) {
                errors.add(new ValidationError("An event with this name already exists", "name", popUpEvent.getName()));
            }
        }

        // Check if the Event has a menu assigned to be Active
        if (popUpEvent.getIsActive() != null && popUpEvent.getIsActive()) {
            if (popUpEvent.getMenu() == null) {
                errors.add(new ValidationError("Event cannot be active until a menu is assigned","isActive"));
            }
        }

        return errors;
    }
}

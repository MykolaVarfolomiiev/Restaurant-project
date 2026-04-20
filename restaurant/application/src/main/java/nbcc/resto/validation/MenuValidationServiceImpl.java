package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.Menu;
import nbcc.resto.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class MenuValidationServiceImpl implements MenuValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final MenuRepository menuRepository;

    public MenuValidationServiceImpl(AnnotationValidationService annotationValidationService, MenuRepository menuRepository) {
        this.annotationValidationService = annotationValidationService;
        this.menuRepository = menuRepository;
    }

    public Collection<ValidationError> validate(Menu menu) {
        var errors = new ArrayList<ValidationError>();

        if (menu == null) {
            errors.add(new ValidationError("Menu is required"));
            return errors;
        }

        errors.addAll(annotationValidationService.validate(menu));

        // Check for duplicate name
        // On create name: check menuRepository.exists(name)
        // On update name:
        //     check if the name is unchanged, skip duplicate check
        //     check if the name is changed, check menuRepository.exists(name)
        if (menu.getName() != null && !menu.getName().isBlank()) {
            var name = menu.getName().trim();

            boolean duplicateExists = switch (menu.getId() == null ? "create" : "update") {
                case "create" -> menuRepository.exists(name);
                case "update" -> menuRepository.get(menu.getId())
                        .map(existing -> !name.equalsIgnoreCase(existing.getName()) && menuRepository.exists(name))
                        .orElseGet(() -> menuRepository.exists(name));
                default -> false;
            };

            if (duplicateExists) {
                errors.add(new ValidationError("A menu with this name already exists", "name", menu.getName()));
            }
        }

        return errors;
    }
}

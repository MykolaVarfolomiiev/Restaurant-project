package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MenuItemValidationServiceImpl implements MenuItemValidationService{

    private final AnnotationValidationService annotationValidationService;
    private final MenuItemRepository menuItemRepository;

    public MenuItemValidationServiceImpl(AnnotationValidationService annotationValidationService, MenuItemRepository menuItemRepository) {
        this.annotationValidationService = annotationValidationService;
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public Collection<ValidationError> validate(MenuItem menuItem) {
        var errors = new ArrayList<ValidationError>();

        if(menuItem == null){
            errors.add(new ValidationError("Menu item is required"));
            return errors;
        }

        errors.addAll(annotationValidationService.validate(menuItem));

//        Check for duplicate menuItem name

//        if(menuItem.getName() != null && !menuItem.getName().isBlank()){
//            var name = menuItem.getName().trim();
//
//            boolean duplicateExists = menuItemRepository.exists(name);;
//
//            if (menuItem.getId() != null) {
//                var existingMenuItem = menuItemRepository.get(menuItem.getId());
//                duplicateExists = existingMenuItem
//                        .map(found -> !name.equalsIgnoreCase(found.getName()) && menuItemRepository.exists(name))
//                        .orElse(duplicateExists);
//            }
//
//            if(duplicateExists){
//                errors.add(new ValidationError("A menu item with this name already exists", "name", menuItem.getName()));
//            }
//        }

        return errors;
    }
}

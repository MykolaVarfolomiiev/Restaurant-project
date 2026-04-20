package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.Menu;
import nbcc.resto.repository.MenuItemRepository;
import nbcc.resto.repository.MenuRepository;
import nbcc.resto.validation.MenuValidationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MenuServiceImpl implements MenuService {

    private final Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);

    private final MenuRepository menuRepository;
    private final MenuValidationServiceImpl menuValidationService;
    private final MenuItemRepository menuItemRepository;
    private final PopUpEventService popUpEventService;

    public MenuServiceImpl(MenuRepository menuRepository, MenuValidationServiceImpl menuValidationService, MenuItemRepository menuItemRepository, PopUpEventService popUpEventService) {
        this.menuRepository = menuRepository;
        this.menuValidationService = menuValidationService;
        this.menuItemRepository = menuItemRepository;
        this.popUpEventService = popUpEventService;
    }

    @Override
    public Result<Collection<Menu>> getAll() {
        return getAll(false);
    }

    @Override
    public Result<Collection<Menu>> getAll(boolean loadMenuItems) {
        try {
            var menus = menuRepository.getAll();

            if (loadMenuItems) {
                for (var menu : menus) {
                    menu.setMenuItems(menuItemRepository.getAll(menu.getId()));
                }
            }

            return ValidationResults.success(menus);
        } catch (Exception e) {
            logger.error("Failed to retrieve all menus", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> get(Long id) {
        return get(id, false);
    }

    @Override
    public ValidatedResult<Menu> get(Long id, boolean loadMenuItems) {
        try {
            var optionalMenu = menuRepository.get(id);
            if(optionalMenu.isEmpty()){
                return ValidationResults.error();
            }

            var menu = optionalMenu.get();

            if (loadMenuItems) {
                menu.setMenuItems(menuItemRepository.getAll(menu.getId()));
            }

            return ValidationResults.success(menu);
        } catch (Exception e) {
            logger.error("Failed to retrieve menu with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> create(Menu menu) {
        try {
            var errors = menuValidationService.validate(menu);

            if(errors.isEmpty()){
                return ValidationResults.success(menuRepository.create(menu));
            } else {
                logger.debug("Validation errors for menu create {}: {}", menu, errors);
                return ValidationResults.invalid(menu, errors);
            }
        } catch (Exception e) {
            logger.error("Failed creating menu: {}", menu, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Menu> update(Menu menu) {
        try {
            var errors = menuValidationService.validate(menu);

            if(errors.isEmpty()) {
                try {
                    return ValidationResults.success(menuRepository.update(menu));
                }catch (ConcurrencyException e) {
                    errors.add(new ValidationError("Menu was modified since it was displayed, please refresh and try again"));
                }
            }

            logger.debug("Validation errors for menu update {}: {}", menu, errors);
            return ValidationResults.invalid(menu, errors);

        } catch (Exception e) {
            logger.error("Failed updating menu: {}", menu, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
       try {
           var menuResult = get(id);

           if (menuResult.isError() || menuResult.isEmpty()) {
               return ValidationResults.error();
           }

           menuRepository.delete(id);

           logger.debug("Menu with id {} deleted from event", id);
           return ValidationResults.success();
       } catch (Exception e) {
            logger.error("Failed deleting menu with id {}", id, e);
            return ValidationResults.error(e);
       }
    }
}

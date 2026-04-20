package nbcc.resto.service;

import nbcc.common.exception.ConcurrencyException;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.repository.MenuItemRepository;
import nbcc.resto.validation.MenuItemValidationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final Logger logger = LoggerFactory.getLogger(MenuItemServiceImpl.class);
    private final MenuItemRepository menuItemRepository;
    private final MenuItemValidationServiceImpl menuItemValidationService;

    public MenuItemServiceImpl(MenuItemRepository menuItemRepository, MenuItemValidationServiceImpl menuItemValidationService) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemValidationService = menuItemValidationService;
    }

    @Override
    public Result<Collection<MenuItem>> getAll() {
        try {
            return ValidationResults.success(menuItemRepository.getAll());
        } catch (Exception e) {
            logger.error("Failed to retrieve all menu items", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Collection<MenuItem>> getAll(Long menuId) {
        try {
            return ValidationResults.success(menuItemRepository.getAll(menuId));
        } catch (Exception e) {
            logger.error("Failed to retrieve menu items for menu ID: {}", menuId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> get(Long id) {
        try {
            return ValidationResults.success(menuItemRepository.get(id));
        } catch (Exception e) {
            logger.error("Failed to retrieve menu item with ID: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> create(MenuItem menuItem) {
        try {
            var errors = menuItemValidationService.validate(menuItem);

            if(errors.isEmpty()){
                return ValidationResults.success(menuItemRepository.create(menuItem));
            } else {
                logger.debug("Validation errors for menu item create {}: {}", menuItem, errors);
                return ValidationResults.invalid(menuItem,errors);
            }
        } catch (Exception e) {
            logger.error("Failed to create menu item: {}", menuItem, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<MenuItem> update(MenuItem menuItem) {
       try {
            var errors = menuItemValidationService.validate(menuItem);

            if(errors.isEmpty()){
                try {
                    return ValidationResults.success(menuItemRepository.update(menuItem));
                } catch (ConcurrencyException e) {
                errors.add(new ValidationError("Menu item was modified since it was displayed, please refresh and try again"));
                }
            }
                logger.debug("Validation errors for menu item update {}: {}", menuItem, errors);
                return ValidationResults.invalid(menuItem,errors);

       } catch (Exception e) {
            logger.error("Failed to update menu item: {}", menuItem, e);
            return ValidationResults.error(e);
       }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            var menuItemResult = get(id);

            if (menuItemResult.isError() || menuItemResult.isEmpty()) {
                return ValidationResults.error();
            }

            menuItemRepository.delete(id);

            logger.debug("Menu item with id {} deleted", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Failed to delete menu item with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> unlink(Long id) {
        try {

            var menuItemResult = get(id);

            if (menuItemResult.isError() || menuItemResult.isEmpty()) {
                return ValidationResults.error();
            }

            var menuItem = menuItemResult.getValue();
            menuItem.setMenu(null);

            var updateResult = update(menuItem);
            if (updateResult.isError()) {
                return ValidationResults.error();
            }

            if (updateResult.isInvalid()) {
                return ValidationResults.invalid(null, updateResult.getValidationErrors());
            }

            logger.debug("Menu item with id {} detached from menu", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Failed to unlink menu item with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}

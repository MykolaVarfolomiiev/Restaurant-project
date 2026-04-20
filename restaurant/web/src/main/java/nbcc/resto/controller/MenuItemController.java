package nbcc.resto.controller;

import nbcc.resto.domain.dto.Menu;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/menu")
public class MenuItemController {

    private final Logger logger = LoggerFactory.getLogger(MenuItemController.class);
    private final MenuItemService menuItemService;
    private final MenuService menuService;

    public MenuItemController(MenuItemService menuItemService, MenuService menuService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
    }

    @GetMapping("/{menuId}/menuitem/{menuItemId}")
    public String details(@PathVariable Long menuId,
                          @PathVariable Long menuItemId,
                          Model model) {
        logger.debug("loading menu item for display with menuItemId: {}, and menuId: {}", menuItemId, menuId);

        var menuItemResult = menuItemService.get(menuItemId);

        if(menuItemResult.isError()){
            model.addAttribute("message", "Error retrieving menu details");
            return "error/errorPage";
        }

        if(menuItemResult.isEmpty()){
            model.addAttribute("message", "Menu not found");
            return "error/404";
        }

        var menuItem = menuItemResult.getValue();
        var menuResult = menuService.get(menuId);
        if (menuResult.hasValue()) {
            menuItem.setMenu(menuResult.getValue());
        }
        loadModel(menuItem, model);

        return "menuitem/detail";
    }

    @GetMapping("/{menuId}/menuitem/create")
    public String create(@PathVariable Long menuId,
                         Model model) {
        logger.debug("Attempting to load menu item create page for menuId: {}", menuId);

        var menuResult = menuService.get(menuId);

        if (menuResult.isError()) {
            model.addAttribute("message", "Menu not found");
            return "error/errorPage";
        }
        if (menuResult.isEmpty()) {
            model.addAttribute("message", "The Menu you are trying to add an item to was not found");
            return "error/404";
        }

        var menu = menuResult.getValue();
        model.addAttribute("menu", menu);
        model.addAttribute("menuItem", new MenuItem(menu));

        return "menuitem/create";
    }

    @PostMapping("/{menuId}/menuitem/create")
    public String create(@PathVariable Long menuId,
                         @ModelAttribute("menuItem") MenuItem menuItem,
                         BindingResult br,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        logger.debug("Attempting to create menu item for menuId: {}", menuId);

        var menuResult = menuService.get(menuId);

        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "There was a problem trying to retrieve menu you are trying to add an item to");
            return "error/errorPage";
        }

        menuItem.setMenu(menuResult.getValue());
        var menuItemResult = menuItemService.create(menuItem);

        if (menuItemResult.isError()) {
            model.addAttribute("message", "There was a problem trying to create the item");
            return "error/errorPage";
        }

        if(menuItemResult.isInvalid()){
            addErrorsToBindingResults(br, menuItemResult);
            model.addAttribute("menu", menuResult.getValue());
            return "menuitem/create";
        }

        redirectAttributes.addAttribute("menuId", menuId);
        return "redirect:/event/menu/edit/{menuId}";
    }

    @GetMapping("/{menuId}/menuitem/edit/{menuItemId}")
    public String edit(@PathVariable Long menuId,
                       @PathVariable Long menuItemId,
                       Model model) {
        logger.debug("Attempting to load menu item for editing menuItemId: {}", menuItemId);

        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError() || menuItemResult.isEmpty()) {
            model.addAttribute("message", "There was a problem trying to retrieve the menu item");
            return "error/errorPage";
        }

        var menuItem = menuItemResult.getValue();
        var menuResult = menuService.get(menuId);
        if (menuResult.hasValue()) {
            menuItem.setMenu(menuResult.getValue());
        }
        loadModel(menuItem, model);

        return "menuitem/edit";
    }

    @PostMapping("/{menuId}/menuitem/edit")
    public String edit(@PathVariable Long menuId,
                       @ModelAttribute("menuItem") MenuItem menuItem,
                       BindingResult br,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        logger.debug("Attempting to edit menu item: {}, which belongs to menuId: {}", menuItem.getId(), menuItem.getMenuId());

        var menuItemResult = menuItemService.update(menuItem);

        if (menuItemResult.isError()) {
            model.addAttribute("message", "There was a problem trying to edit the menu item");
            return "error/errorPage";
        }

        if (menuItemResult.isInvalid()) {
            addErrorsToBindingResults(br, menuItemResult);

            var menuResult = menuService.get(menuItem.getMenuId());
            if (menuResult.hasValue()) {
                menuItem.setMenu(menuResult.getValue());
                loadModel(menuItem, model);
            }

            return "menuitem/edit";
        }

        redirectAttributes.addAttribute("menuId", menuItem.getMenuId());
        return "redirect:/event/menu/edit/{menuId}";
    }

    @GetMapping("/{menuId}/menuitem/delete/{menuItemId}")
    public String delete(@PathVariable Long menuId,
                         @PathVariable Long menuItemId,
                         Model model) {
        logger.debug("Attempting to load menu item delete page for menuItemId: {}", menuItemId);

        var menuItemResult = menuItemService.get(menuItemId);

        if (menuItemResult.isError()) {
            return "error/errorPage";
        }

        if (menuItemResult.isEmpty()) {
            model.addAttribute("message", "The menu item you are trying to delete was not found");
            return "error/404";
        }

        var menuItem = menuItemResult.getValue();
        loadModel(menuItem, model);

        return "menuitem/delete";
    }

    @PostMapping("/{menuId}/menuitem/delete/{menuItemId}")
    public String delete(@PathVariable Long menuId,
                         @PathVariable Long menuItemId,
                         RedirectAttributes redirectAttributes) {
        logger.debug("Attempting to delete menu item with menuItemId: {}", menuItemId);

        var menuItemResult = menuItemService.delete(menuItemId);

        if (menuItemResult.isError()) {
            return "error/errorPage";
        }

        redirectAttributes.addAttribute("menuId", menuId);
        return "redirect:/event/menu/edit/{menuId}";
    }

    private void loadModel(MenuItem menuItem, Model model){
        loadModel(menuItem.getMenu(), menuItem,  model);
    }

    private void loadModel(Menu menu, MenuItem menuItem, Model model) {
        model.addAttribute("menu", menu);
        model.addAttribute("menuItem", menuItem);
    }
}

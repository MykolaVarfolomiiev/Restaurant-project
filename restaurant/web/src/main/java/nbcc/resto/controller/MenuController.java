package nbcc.resto.controller;

import nbcc.resto.domain.dto.Menu;
import nbcc.resto.domain.dto.MenuItem;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.service.MenuItemService;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.PopUpEventService;
import nbcc.resto.viewmodels.MenuItemListViewModel;
import nbcc.resto.viewmodels.MenuListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize( "isAuthenticated()")
@RequestMapping("/event")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;
    private final MenuItemService menuItemService;
    private final PopUpEventService popUpEventService;

    public MenuController(MenuService menuService, MenuItemService menuItemService, PopUpEventService popUpEventService) {
        this.menuService = menuService;
        this.menuItemService = menuItemService;
        this.popUpEventService = popUpEventService;
    }

    @GetMapping("/menu")
    public String getAll(@RequestParam(value = "search", required = false) String search, Model model) {
        logger.debug("Attempting to load all menus");

        var menusResult = menuService.getAll();
        if (menusResult.isError()) {
            model.addAttribute("message", "There was a problem trying to retrieve menus");
            return "error/errorPage";
        }

        var menus = menusResult.getValue();
        var filteredMenus = filterMenus(menus, search);

        model.addAttribute("search", search);
        model.addAttribute("menuListViewModel", new MenuListViewModel(filteredMenus, null, false, true));
        model.addAttribute("showEvent", true);

        return "menu/list";
    }

    @GetMapping("/menu/create")
    public String createGlobal(Model model) {
        model.addAttribute("menu", new Menu());
        return "menu/create";
    }

    @PostMapping("/menu/create")
    public String createGlobal(@ModelAttribute("menu") Menu menu,
                               BindingResult br,
                               Model model) {
        var result = menuService.create(menu);

        if (result.isError()) {
            model.addAttribute("message", "Could not create menu");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            return "menu/create";
        }

        return "redirect:/event/menu";
    }

    @GetMapping("/{eventId}/menu/create")
    public String showCreatePage(@PathVariable Long eventId, Model model) {
        var eventResult = popUpEventService.get(eventId);

        if (eventResult.isError()) {
            return "error/errorPage";
        }

        if (eventResult.isEmpty()) {
            model.addAttribute("message", "Event not found");
            return "error/404";
        }

        model.addAttribute("event", eventResult.getValue());
        model.addAttribute("menu", new Menu(eventResult.getValue()));

        var menusResult = menuService.getAll();
        if (menusResult.isError()) {
            model.addAttribute("message", "Could not load menus");
            return "error/errorPage";
        }

        model.addAttribute("availableMenus", menusResult.getValue());
        return "menu/create";
    }

    @PostMapping("/{eventId}/menu/create-new")
    public String createNewMenu(@PathVariable Long eventId,
                                @ModelAttribute("menu") Menu menu,
                                BindingResult br,
                                Model model) {

        var eventResult = popUpEventService.get(eventId);
        if (eventResult.isError() || eventResult.isEmpty()) {
            model.addAttribute("message", "Event not found");
            return "error/errorPage";
        }

        // Create the menu first
        var result = menuService.create(menu);
        if (result.isError()) {
            model.addAttribute("message", "Could not create menu");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);
            model.addAttribute("event", eventResult.getValue());

            var menusResult = menuService.getAll();
            if (menusResult.isError()) {
                model.addAttribute("message", "Could not load menus");
                return "error/errorPage";
            }

            model.addAttribute("availableMenus", menusResult.getValue());
            return "menu/create";
        }

        // Now attach the menu to the event
        var event = eventResult.getValue();
        event.setMenu(result.getValue());

        var updateResult = popUpEventService.update(event);
        if (updateResult.isError()) {
            model.addAttribute("message", "Could not attach menu to event");
            return "error/errorPage";
        }

        return "redirect:/event/edit/" + eventId;
    }

    @PostMapping("/{eventId}/menu/attach-existing")
    public String attachExistingMenu(@PathVariable Long eventId,
                                     @RequestParam Long menuId,
                                     Model model) {

        var eventResult = popUpEventService.get(eventId);
        if (eventResult.isError() || eventResult.isEmpty()) {
            model.addAttribute("message", "Event not found");
            return "error/errorPage";
        }

        var menuResult = menuService.get(menuId);
        if (menuResult.isError() || menuResult.isEmpty()) {
            model.addAttribute("message", "Menu not found");
            return "error/errorPage";
        }

        var event = eventResult.getValue();
        event.setMenu(menuResult.getValue());

        var updateResult = popUpEventService.update(event);
        if (updateResult.isError()) {
            model.addAttribute("message", "Could not attach menu");
            return "error/errorPage";
        }

        return "redirect:/event/edit/" + eventId;
    }

    @GetMapping("/menu/edit/{menuId}")
    public String edit(@PathVariable Long menuId, Model model){
        logger.debug("Attempting to load menu for editing menuId: {}", menuId);

        var menuResult = menuService.get(menuId, true);

        if(menuResult.isError() || menuResult.isEmpty()){
            model.addAttribute("message", "There was a problem trying to retrieve menu you are trying to edit");
            return "error/errorPage";
        }

        var menu = menuResult.getValue();
        loadModel(menu, model);
        model.addAttribute("currentMenuId", menuId);
        loadCurrentMenuItems(menuId, model);
        loadAvailableMenuItems(menuId, model);

        return "menu/edit";
    }

    @PostMapping("/menu/edit")
    public String edit(@ModelAttribute("menu") Menu menu,
                       BindingResult br,
                       Model model,
                       RedirectAttributes redirectAttributes) {

        logger.debug("Attempting to edit menu: {}, which belongs to eventId: {}", menu.getId(), menu.getPopUpEventId());

        var menuResult = menuService.update(menu);

        if(menuResult.isError()){
            model.addAttribute("message", "There was a problem trying to edit menu");
            return "error/errorPage";
        }

        if(menuResult.isInvalid()){
            addErrorsToBindingResults(br, menuResult);
            var eventResult = popUpEventService.get(menu.getPopUpEventId());
            if(eventResult.hasValue()) {
                menu.setPopUpEvent(eventResult.getValue());
                loadModel(menu, model);
            }
            loadCurrentMenuItems(menu.getId(), model);
            loadAvailableMenuItems(menu.getId(), model);
            return "menu/edit";
        }

        return "redirect:/event/menu";
    }

    @GetMapping("/menu/delete/{menuId}")
    public String delete(@PathVariable Long menuId, Model model){
        logger.debug("Attempting to delete menu with menuId: {}", menuId);

        var menuResult = menuService.get(menuId);

        if(menuResult.isError()) {
            return "error/errorPage";
        }

        if(menuResult.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to delete was not found");
            return "error/404";
        }

        var menu = menuResult.getValue();
        loadModel(menu, model);

        return "menu/delete";
    }

    @PostMapping("/menu/delete/{menuId}")
    public String delete(@PathVariable Long menuId,
                         RedirectAttributes redirectAttributes){

        logger.debug("Attempting to delete menu: {}", menuId);

        var menuResult = menuService.get(menuId);
        if (menuResult.isError() || menuResult.isEmpty()) {
            return "error/errorPage";
        }

        Long eventId = menuResult.getValue().getPopUpEventId();
        var result = menuService.delete(menuId);

        if(result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/event/menu";
    }

    @GetMapping("/menu/{menuId}")
    public String details(@PathVariable Long menuId, Model model){
        logger.debug("Attempting to load menu details for menuId: {}", menuId);

        var menuResult = menuService.get(menuId, true);

        if(menuResult.isError()) {
            return "error/errorPage";
        }

        if(menuResult.isEmpty()) {
            model.addAttribute("message", "The menu you are trying to view was not found");
            return "error/404";
        }

        var menu = menuResult.getValue();
        loadModel(menu, model);

        return "menu/detail";
    }

    @PostMapping("/{eventId}/menu/unlink/{menuId}")
    public String unlinkMenu(@PathVariable Long eventId,
                             @PathVariable Long menuId) {

        var eventResult = popUpEventService.get(eventId);
        if (eventResult.isError() || eventResult.isEmpty()) {
            return "error/errorPage";
        }

        var event = eventResult.getValue();
        event.setMenu(null);           // Sets the menu to null on the EVENT
        event.setIsActive(false);

        var updateResult = popUpEventService.update(event);
        if (updateResult.isError()) {
            return "error/errorPage";
        }

        return "redirect:/event/edit/" + eventId;
    }

    private void loadModel(Menu menu, Model model){
        loadModel(menu, menu.getPopUpEvent(), model);
    }

    private void loadModel(Menu menu, PopUpEvent event, Model model){
        model.addAttribute("menu", menu);
        model.addAttribute("event", event);
    }

    private void loadCurrentMenuItems(Long menuId, Model model) {
        var menuItemsResult = menuItemService.getAll(menuId);
        logger.debug("Current menu item count for menu {}: {}", menuId, menuItemsResult.hasValue() ? menuItemsResult.getValue().size() : 0);
        if (!menuItemsResult.isError()) {
            model.addAttribute(
                    "menuItemListViewModel",
                    new MenuItemListViewModel(menuItemsResult.getValue(), menuId, true)
            );
        } else {
            model.addAttribute(
                    "menuItemListViewModel",
                    new MenuItemListViewModel(List.of(), menuId, false)
            );
        }
    }

    private void loadAvailableMenuItems(Long menuId, Model model) {
        var currentMenuItemsResult = menuItemService.getAll(menuId);
        var allMenuItemsResult = menuItemService.getAll();

        if (currentMenuItemsResult.isError() || allMenuItemsResult.isError()) {
            model.addAttribute(
                    "availableMenuItemListViewModel",
                    new MenuItemListViewModel(List.of(), null, false)
            );
            return;
        }

        var currentMenuItemIds = currentMenuItemsResult.getValue().stream()
                .map(MenuItem::getId)
                .collect(Collectors.toSet());

        var availableMenuItems = allMenuItemsResult.getValue().stream()
                .filter(menuItem -> !currentMenuItemIds.contains(menuItem.getId()))
                .collect(Collectors.toList());

        model.addAttribute(
                "availableMenuItemListViewModel",
                new MenuItemListViewModel(availableMenuItems, null, true)
        );
    }

    private Collection<Menu> filterMenus(Collection<Menu> menus, String search) {
        if (search == null || search.isBlank()) {
            return menus;
        }

        var normalizedSearch = search.trim().toLowerCase(Locale.ROOT);

        return menus.stream()
                .filter(menu -> menu.getName() != null
                        && menu.getName().toLowerCase(Locale.ROOT).contains(normalizedSearch))
                .collect(Collectors.toList());
    }
}

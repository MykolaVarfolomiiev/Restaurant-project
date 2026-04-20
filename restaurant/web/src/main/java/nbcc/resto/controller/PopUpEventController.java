package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.common.service.LoginService;
import nbcc.resto.domain.dto.Menu;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.service.MenuService;
import nbcc.resto.service.PopUpEventService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.viewmodels.MenuListViewModel;
import nbcc.resto.viewmodels.PopUpEventListViewModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping({"/event"})
public class PopUpEventController {

    private final LoginService loginService;
    private final PopUpEventService popUpEventService;
    private final SeatingService seatingService;
    private final MenuService menuService;
    private final Logger logger = LoggerFactory.getLogger(PopUpEventController.class);

    public PopUpEventController(LoginService loginService, PopUpEventService popUpEventService, SeatingService seatingService, MenuService menuService) {
        this.loginService = loginService;
        this.popUpEventService = popUpEventService;
        this.seatingService = seatingService;
        this.menuService = menuService;
    }

    @GetMapping
    public String getAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        var result = popUpEventService.search(name, startDate, endDate);
        if (result.isError()) {
            model.addAttribute("message", "Error retrieving events");
            return "error/errorPage";
        }

        PopUpEventListViewModel viewModel = new PopUpEventListViewModel(result.getValue(), loginService.isLoggedIn());
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("name", name);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "event/list";
    }

    @GetMapping("/{eventId}")
    public String details(@PathVariable Long eventId, Model model){
        var result = popUpEventService.get(eventId);
        if(result.isError()){
            model.addAttribute("message", "Error retrieving event details");
            return "error/errorPage";
        }

        if(result.isEmpty()){
            model.addAttribute("message", "Event not found");
            return "error/404";
        }
        loadModel(result.getValue(), model);
        return "event/detail";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("event", new PopUpEvent());

        var menusResult = menuService.getAll();
        if (menusResult.isError()) {
            model.addAttribute("message", "Error retrieving menus");
            return "error/errorPage";
        }

        model.addAttribute("availableMenus", menusResult.getValue());
        return "event/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("event") PopUpEvent event,
                         @RequestParam(required = false) Long existingMenuId,
                         @RequestParam(required = false, defaultValue = "false") Boolean isActive,
                         BindingResult br, Model model){

        if (existingMenuId != null) {
            var menuResult = menuService.get(existingMenuId);
            if (menuResult.isError() || menuResult.isEmpty()) {
                model.addAttribute("message", "Menu not found");
                return "error/errorPage";
            }
            event.setMenu(menuResult.getValue());
            event.setIsActive(isActive);
        } else {
            event.setMenu(null);
            event.setIsActive(false);
        }

        var result = popUpEventService.create(event);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);

            var menusResult = menuService.getAll();
            if (menusResult.isError()) {
                model.addAttribute("message", "Error retrieving menus");
                return "error/errorPage";
            }

            model.addAttribute("availableMenus", menusResult.getValue());
            return "event/create";
        }

        return "redirect:/event";
    }

    @GetMapping("/edit/{eventId}")
    public String edit(@PathVariable Long eventId, Model model){
        var result = popUpEventService.get(eventId);
        if(result.isError()){
            return "error/errorPage";
        }

        if(result.isEmpty()){
            model.addAttribute("message", "Event not found");
            return "error/404";
        }

        var event = result.getValue();

        // Get all available menus for dropdown
        var allMenusResult = menuService.getAll();
        if (allMenusResult.isError()) {
            model.addAttribute("message", "Error retrieving available menus");
            return "error/errorPage";
        }
        model.addAttribute("availableMenus", allMenusResult.getValue());

        // Get the currently selected menu ID (if any) from the event
        Long selectedMenuId = event.getMenu() != null ? event.getMenu().getId() : null;

        model.addAttribute("selectedMenuId", selectedMenuId);

        // Load menu list view model with the event's menu (if any)
        Collection<Menu> menuList = event.getMenu() != null ? List.of(event.getMenu()) : List.of();
        loadMenuListViewModel(model, menuList, eventId);
        loadModel(event, model, true);
        return "event/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute("event") PopUpEvent event, @RequestParam(required = false) Long existingMenuId, BindingResult br, Model model){

        // Get the fresh event from a database
        var eventResult = popUpEventService.get(event.getId());
        if (eventResult.isError() || eventResult.isEmpty()) {
            model.addAttribute("message", "Event not found");
            return "error/errorPage";
        }

        var freshEvent = eventResult.getValue();

        // Update the menu relationship
        if (existingMenuId != null) {
            var menuResult = menuService.get(existingMenuId);
            if (menuResult.isError() || menuResult.isEmpty()) {
                model.addAttribute("message", "Menu not found");
                return "error/errorPage";
            }
            event.setMenu(menuResult.getValue());
        } else {
            event.setMenu(null);
        }

        // Now update the event
        var result = popUpEventService.update(event);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result);

            // Get the menu from the event itself
            Long selectedMenuId = event.getMenu() != null ? event.getMenu().getId() : null;
            Collection<Menu> menuList = event.getMenu() != null ? List.of(event.getMenu()) : List.of();

            loadMenuListViewModel(model, menuList, event.getId());
            model.addAttribute("selectedMenuId", selectedMenuId);

            // Get all available menus for dropdown on validation error
            var allMenusResult = menuService.getAll();
            if (!allMenusResult.isError()) {
                model.addAttribute("availableMenus", allMenusResult.getValue());
            }

            loadModel(event, model, true);
            return "event/edit";
        }

        return "redirect:/event";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model){
        var result = popUpEventService.get(id);
        if(result.isError()){
            return "error/errorPage";
        }
        if(result.isEmpty()){
            model.addAttribute("message", "Event not found");
            return "error/404";
        }

        var seatingResult = seatingService.getAll();
        if (seatingResult.isError()) {
            model.addAttribute("message", "Error retrieving event seatings");
            return "error/errorPage";
        }

        var event = result.getValue();
        var eventSeatings = seatingResult.getValue().stream()
                .filter(seating -> event.getId() != null && event.getId().equals(seating.getEventId()))
                .toList();

        loadModel(event, model, false);
        model.addAttribute("seatings", eventSeatings);
        return "event/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        var result = popUpEventService.delete(id);
        if(result.isError()){
            return "error/errorPage";
        }

        return "redirect:/event";
    }

    // --- Helper Methods ---
    private void loadModel(PopUpEvent event, Model model) {
        loadModel(event, model, false);
    }

    private void loadModel(PopUpEvent event, Model model, boolean showManage) {
        model.addAttribute("event", event);
        model.addAttribute("showManage", showManage && loginService.isLoggedIn());
    }

    private void loadMenuListViewModel(Model model) {
        model.addAttribute(
                "menuListViewModel",
                new MenuListViewModel(List.of(), null, false, false)
        );
    }

    private void loadMenuListViewModel(Model model, Collection<Menu> menus, Long eventId) {
        model.addAttribute(
                "menuListViewModel",
                new MenuListViewModel(
                        menus != null ? menus : List.of(),
                        eventId,
                        loginService.isLoggedIn(),
                        true
                )
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request){
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI() , request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}

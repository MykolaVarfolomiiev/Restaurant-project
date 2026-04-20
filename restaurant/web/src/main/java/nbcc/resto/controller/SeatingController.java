package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.domain.dto.DiningTable;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.service.PopUpEventService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/seatings")
public class SeatingController {

    private final SeatingService seatingService;
    private final TableService tableService;
    private final PopUpEventService popUpEventService;
    private final Logger logger = LoggerFactory.getLogger(SeatingController.class);

    public SeatingController(SeatingService seatingService, TableService tableService, PopUpEventService popUpEventService) {
        this.seatingService = seatingService;
        this.tableService = tableService;
        this.popUpEventService = popUpEventService;
    }

    @GetMapping
    public String list(Model model) {
        var eventsResult = popUpEventService.getAll();
        var seatingsResult = seatingService.getAll();
        var tablesResult = tableService.getAll();

        if (eventsResult.isError() || seatingsResult.isError()) {
            model.addAttribute("message", "Error retrieving data");
            return "error/errorPage";
        }

        List<PopUpEvent> events = eventsResult.getValue() != null ? List.copyOf(eventsResult.getValue()) : List.of();
        List<Seating> allSeatings = seatingsResult.getValue() != null ? seatingsResult.getValue() : List.of();

        Map<PopUpEvent, List<Seating>> eventsWithSeatings = new LinkedHashMap<>();
        for (PopUpEvent event : events) {
            List<Seating> seatingsForEvent = allSeatings.stream()
                    .filter(s -> event.getId() != null && event.getId().equals(s.getEventId()))
                    .collect(Collectors.toList());
            eventsWithSeatings.put(event, seatingsForEvent);
        }

        Map<Long, String> tableNamesById = new HashMap<>();
        if (!tablesResult.isError() && tablesResult.getValue() != null) {
            for (DiningTable t : tablesResult.getValue()) {
                tableNamesById.put(t.getId(), t.getName());
            }
        }

        model.addAttribute("eventsWithSeatings", eventsWithSeatings);
        model.addAttribute("tableNamesById", tableNamesById);
        return "seating/list";
    }

    @GetMapping("/create")
    public String create(@RequestParam(required = false) Long eventId, Model model) {
        var eventsResult = popUpEventService.getAll();
        var tablesResult = tableService.getAll();

        if (eventsResult.isError() || tablesResult.isError()) {
            model.addAttribute("message", "Error retrieving data");
            return "error/errorPage";
        }

        Seating seating = new Seating();
        seating.setEventId(eventId);

        model.addAttribute("seating", seating);
        model.addAttribute("events", eventsResult.getValue());
        model.addAttribute("tables", tablesResult.getValue());
        return "seating/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("seating") Seating seating, BindingResult br, Model model) {
        var result = seatingService.create(seating);

        if (result.isError()) {
            model.addAttribute("message", "Error creating seating");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "seating");
            var eventsResult = popUpEventService.getAll();
            var tablesResult = tableService.getAll();
            if (!eventsResult.isError()) model.addAttribute("events", eventsResult.getValue());
            if (!tablesResult.isError()) model.addAttribute("tables", tablesResult.getValue());
            return "seating/create";
        }

        return "redirect:/seatings";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var seatingResult = seatingService.get(id);
        if (seatingResult.isEmpty() || seatingResult.isError()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }
        Seating seating = seatingResult.getValue();
        if (seating.isArchived()) {
            model.addAttribute("message", "Cannot edit an archived seating");
            return "error/errorPage";
        }

        var eventsResult = popUpEventService.getAll();
        var tablesResult = tableService.getAll();
        if (eventsResult.isError() || tablesResult.isError()) {
            model.addAttribute("message", "Error retrieving data");
            return "error/errorPage";
        }

        model.addAttribute("seating", seating);
        model.addAttribute("events", eventsResult.getValue());
        model.addAttribute("tables", tablesResult.getValue());
        return "seating/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("seating") Seating seating, BindingResult br, Model model) {
        var result = seatingService.update(seating);

        if (result.isError()) {
            model.addAttribute("message", "Error updating seating");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "seating");
            var eventsResult = popUpEventService.getAll();
            var tablesResult = tableService.getAll();
            if (!eventsResult.isError()) model.addAttribute("events", eventsResult.getValue());
            if (!tablesResult.isError()) model.addAttribute("tables", tablesResult.getValue());
            return "seating/edit";
        }

        return "redirect:/seatings";
    }

    @GetMapping("/delete/{id}")
    public String deleteConfirm(@PathVariable Long id, Model model) {
        var seatingResult = seatingService.get(id);
        if (seatingResult.isEmpty() || seatingResult.isError()) {
            model.addAttribute("message", "Seating not found");
            return "error/errorPage";
        }
        Seating seating = seatingResult.getValue();
        if (seating.isArchived()) {
            model.addAttribute("message", "Cannot delete an archived seating");
            return "error/errorPage";
        }

        var eventsResult = popUpEventService.getAll();
        var tablesResult = tableService.getAll();
        Map<Long, String> tableNamesById = new HashMap<>();
        if (!tablesResult.isError() && tablesResult.getValue() != null) {
            for (DiningTable t : tablesResult.getValue()) {
                tableNamesById.put(t.getId(), t.getName());
            }
        }
        String eventName = "";
        if (!eventsResult.isError() && eventsResult.getValue() != null) {
            eventName = eventsResult.getValue().stream()
                    .filter(e -> e.getId() != null && e.getId().equals(seating.getEventId()))
                    .findFirst()
                    .map(PopUpEvent::getName)
                    .orElse("");
        }

        model.addAttribute("seating", seating);
        model.addAttribute("eventName", eventName);
        model.addAttribute("tableNamesById", tableNamesById);
        return "seating/delete-confirm";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        var result = seatingService.deleteOrArchive(id);

        if (result.isError()) {
            model.addAttribute("message", "Error deleting seating");
            return "error/errorPage";
        }

        return "redirect:/seatings";
    }

    @ExceptionHandler(Exception.class)
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        if (ex instanceof AuthorizationDeniedException) {
            throw (AuthorizationDeniedException) ex;
        }
        logger.error("Unexpected Exception on uri {}: on method {} ", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}

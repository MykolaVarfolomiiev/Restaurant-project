package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.service.PopUpEventService;
import nbcc.resto.service.ReservationRequestService;
import nbcc.resto.service.SeatingService;
import nbcc.resto.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/reservations")
public class ReservationRequestsController {

    private final ReservationRequestService reservationRequestService;
    private final PopUpEventService popUpEventService;
    private final SeatingService seatingService;
    private final TableService tableService;
    private final Logger logger = LoggerFactory.getLogger(ReservationRequestsController.class);

    public ReservationRequestsController(ReservationRequestService reservationRequestService,
                                         PopUpEventService popUpEventService,
                                         SeatingService seatingService,
                                         TableService tableService) {
        this.reservationRequestService = reservationRequestService;
        this.popUpEventService = popUpEventService;
        this.seatingService = seatingService;
        this.tableService = tableService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) Long eventId,
                       @RequestParam(required = false) String status,
                       Model model) {
        ReservationRequestStatus statusFilter = null;
        if (status != null && !status.isBlank()) {
            try {
                statusFilter = ReservationRequestStatus.valueOf(status.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                model.addAttribute("filterStatusInvalid", true);
            }
        }

        var eventsResult = popUpEventService.getAll();
        if (eventsResult.isError()) {
            model.addAttribute("message", "Error loading events");
            return "error/errorPage";
        }

        var listResult = reservationRequestService.listForEmployee(eventId, statusFilter);
        if (listResult.isError()) {
            model.addAttribute("message", "Error loading reservation requests");
            return "error/errorPage";
        }

        List<PopUpEvent> events = eventsResult.getValue() != null
                ? new ArrayList<>(eventsResult.getValue())
                : new ArrayList<>();

        model.addAttribute("requests", listResult.getValue());
        model.addAttribute("events", events);
        model.addAttribute("selectedEventId", eventId);
        model.addAttribute("selectedStatus", statusFilter != null ? statusFilter.name() : "");
        model.addAttribute("statuses", ReservationRequestStatus.values());
        return "reservation/requests";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        var opt = reservationRequestService.get(id);
        if (opt.isEmpty()) {
            model.addAttribute("message", "Reservation request not found");
            return "error/errorPage";
        }
        ReservationRequest reservationRequest = opt.get();

        var seatingRes = seatingService.get(reservationRequest.getSeatingId());
        if (seatingRes.isError() || seatingRes.getValue() == null) {
            model.addAttribute("message", "Seating for this request could not be loaded");
            return "error/errorPage";
        }
        Seating seating = seatingRes.getValue();

        var eventResult = popUpEventService.get(seating.getEventId());
        if (eventResult.isError() || eventResult.getValue() == null) {
            model.addAttribute("message", "Event for this request could not be loaded");
            return "error/errorPage";
        }
        PopUpEvent event = eventResult.getValue();

        Map<Long, String> tableNamesById = new HashMap<>();
        var tablesResult = tableService.getAll();
        if (!tablesResult.isError() && tablesResult.getValue() != null) {
            tablesResult.getValue().forEach(t -> tableNamesById.put(t.getId(), t.getName()));
        }

        model.addAttribute("reservationRequest", reservationRequest);
        model.addAttribute("seating", seating);
        model.addAttribute("event", event);
        model.addAttribute("tableNamesById", tableNamesById);
        return "reservation/request-detail";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id,
                          @RequestParam(required = false) Long tableId,
                          RedirectAttributes redirectAttributes) {
        var result = reservationRequestService.approve(id, tableId);
        if (result.isError()) {
            redirectAttributes.addFlashAttribute("actionMessage", "Error approving reservation request");
            redirectAttributes.addFlashAttribute("actionMessageType", "danger");
            return "redirect:/reservations/" + id;
        }
        if (result.isInvalid()) {
            var msg = result.getValidationErrors() != null && !result.getValidationErrors().isEmpty()
                    ? result.getValidationErrors().iterator().next().getMessage()
                    : "Unable to approve reservation request";
            redirectAttributes.addFlashAttribute("actionMessage", msg);
            redirectAttributes.addFlashAttribute("actionMessageType", "warning");
            return "redirect:/reservations/" + id;
        }
        redirectAttributes.addFlashAttribute("actionMessage", "Reservation request approved");
        redirectAttributes.addFlashAttribute("actionMessageType", "success");
        return "redirect:/reservations/" + id;
    }

    @PostMapping("/{id}/deny")
    public String deny(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        var result = reservationRequestService.deny(id);
        if (result.isError()) {
            redirectAttributes.addFlashAttribute("actionMessage", "Error denying reservation request");
            redirectAttributes.addFlashAttribute("actionMessageType", "danger");
            return "redirect:/reservations/" + id;
        }
        if (result.isInvalid()) {
            var msg = result.getValidationErrors() != null && !result.getValidationErrors().isEmpty()
                    ? result.getValidationErrors().iterator().next().getMessage()
                    : "Unable to deny reservation request";
            redirectAttributes.addFlashAttribute("actionMessage", msg);
            redirectAttributes.addFlashAttribute("actionMessageType", "warning");
            return "redirect:/reservations/" + id;
        }
        redirectAttributes.addFlashAttribute("actionMessage", "Reservation request denied");
        redirectAttributes.addFlashAttribute("actionMessageType", "success");
        return "redirect:/reservations/" + id;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exceptionHandler(Model model, Exception ex, HttpServletRequest request) {
        if (ex instanceof AuthorizationDeniedException) {
            throw (AuthorizationDeniedException) ex;
        }
        logger.error("Unexpected Exception on uri {}: {}", request.getRequestURI(), request.getMethod(), ex);
        model.addAttribute("message", "Unexpected Error Occurred");
        return "error/errorPage";
    }
}

package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.service.ReservationRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("permitAll()")
@RequestMapping("/reserve")
public class ReservationRequestController {

    private final ReservationRequestService reservationRequestService;
    private final Logger logger = LoggerFactory.getLogger(ReservationRequestController.class);

    public ReservationRequestController(ReservationRequestService reservationRequestService) {
        this.reservationRequestService = reservationRequestService;
    }

    @GetMapping
    public String reserve(Model model) {
        if (!model.containsAttribute("reservationRequest")) {
            model.addAttribute("reservationRequest", new ReservationRequest());
        }

        var eventsResult = reservationRequestService.getActiveEventsForReserve();
        var seatingsResult = reservationRequestService.getActiveSeatingsForReserve();

        if (eventsResult.isError() || seatingsResult.isError()) {
            model.addAttribute("message", "Error loading reservation page");
            return "error/errorPage";
        }

        model.addAttribute("events", eventsResult.getValue());
        model.addAttribute("seatings", seatingsResult.getValue());
        return "reservation/reserve";
    }

    @PostMapping
    public String submit(@ModelAttribute("reservationRequest") ReservationRequest reservationRequest,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        var result = reservationRequestService.create(reservationRequest);

        if (result.isError()) {
            model.addAttribute("message", "Error submitting reservation request");
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(bindingResult, result, "reservationRequest");
            var eventsResult = reservationRequestService.getActiveEventsForReserve();
            var seatingsResult = reservationRequestService.getActiveSeatingsForReserve();
            if (!eventsResult.isError()) model.addAttribute("events", eventsResult.getValue());
            if (!seatingsResult.isError()) model.addAttribute("seatings", seatingsResult.getValue());
            return "reservation/reserve";
        }

        redirectAttributes.addFlashAttribute("reservationSubmitted", true);
        redirectAttributes.addFlashAttribute("reservationUuid", result.getValue().getUuid());
        return "redirect:/reserve";
    }

    @GetMapping("/track")
    public String track(@RequestParam(required = false) String uuid, Model model) {
        if (uuid == null || uuid.isBlank()) {
            return "reservation/track";
        }
        UUID parsed;
        try {
            parsed = UUID.fromString(uuid.trim());
        } catch (IllegalArgumentException e) {
            model.addAttribute("invalidUuid", true);
            model.addAttribute("submittedUuid", uuid.trim());
            return "reservation/track";
        }
        var detail = reservationRequestService.findGuestDetailByUuid(parsed);
        if (detail.isEmpty()) {
            model.addAttribute("notFound", true);
            model.addAttribute("submittedUuid", uuid.trim());
            return "reservation/track";
        }
        model.addAttribute("detail", detail.get());
        return "reservation/track";
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

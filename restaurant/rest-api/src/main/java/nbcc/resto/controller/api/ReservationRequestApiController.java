package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.domain.dto.ReservationRequest;
import nbcc.resto.domain.dto.ReservationRequestStatus;
import nbcc.resto.domain.dto.ReservationRequestSummary;
import nbcc.resto.domain.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.service.ReservationRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static nbcc.common.results.ResultHandler.handleResult;

@Tag(name = "Reservation request API", description = "Submit reservation requests (e.g. mobile app)")
@RestController
@RequestMapping("/api/reservation-request")
public class ReservationRequestApiController {

    private final ReservationRequestService reservationRequestService;
    private final SeatingRepository seatingRepository;

    public ReservationRequestApiController(ReservationRequestService reservationRequestService,
                                           SeatingRepository seatingRepository) {
        this.reservationRequestService = reservationRequestService;
        this.seatingRepository = seatingRepository;
    }

    @Operation(summary = "Create a reservation request")
    @PostMapping
    public ResponseEntity<ValidatedResult<ReservationRequest>> create(@RequestBody ReservationRequestCreateBody body) {
        List<ValidationError> errors = new ArrayList<>();

        if (body == null) {
            return handleResult(ValidationResults.invalid(null, new ValidationError("Request body is required")),
                    HttpStatus.BAD_REQUEST);
        }
        if (body.getEventId() == null) {
            errors.add(new ValidationError("Event id is required", "eventId"));
        }
        if (body.getSeatingId() == null) {
            errors.add(new ValidationError("Seating id is required", "seatingId"));
        }
        if (body.getFirstName() == null || body.getFirstName().isBlank()) {
            errors.add(new ValidationError("First name is required", "firstName"));
        }
        if (body.getLastName() == null || body.getLastName().isBlank()) {
            errors.add(new ValidationError("Last name is required", "lastName"));
        }
        if (body.getEmail() == null || body.getEmail().isBlank()) {
            errors.add(new ValidationError("Email is required", "email"));
        } else if (!body.getEmail().contains("@")) {
            errors.add(new ValidationError("Please enter a valid email address", "email"));
        }
        if (body.getGroupSize() == null) {
            errors.add(new ValidationError("Group size is required", "groupSize"));
        } else if (body.getGroupSize() < 1) {
            errors.add(new ValidationError("Group size must be at least 1", "groupSize"));
        }

        if (!errors.isEmpty()) {
            return handleResult(ValidationResults.invalid(null, errors), HttpStatus.BAD_REQUEST);
        }

        var seatingOpt = seatingRepository.get(body.getSeatingId());
        if (seatingOpt.isEmpty()) {
            return handleResult(
                    ValidationResults.invalid(null, new ValidationError("Selected seating is not available", "seatingId")),
                    HttpStatus.BAD_REQUEST);
        }
        Seating seating = seatingOpt.get();
        if (seating.isArchived()) {
            return handleResult(
                    ValidationResults.invalid(null, new ValidationError("Selected seating is not available", "seatingId")),
                    HttpStatus.BAD_REQUEST);
        }
        if (!Objects.equals(seating.getEventId(), body.getEventId())) {
            return handleResult(
                    ValidationResults.invalid(null, new ValidationError("Seating is not valid for the specified event", "seatingId")),
                    HttpStatus.BAD_REQUEST);
        }

        ReservationRequest request = new ReservationRequest();
        request.setSeatingId(body.getSeatingId());
        request.setFirstName(body.getFirstName().trim());
        request.setLastName(body.getLastName().trim());
        request.setEmail(body.getEmail().trim());
        request.setGroupSize(body.getGroupSize());

        var result = reservationRequestService.create(request);
        return handleResult(result, HttpStatus.CREATED);
    }

    // To retrieve all reservations (only employee can see this)
    @Operation(summary = "Get confirmed reservations for an event")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/event/{eventId}/confirmed")
    public ResponseEntity<ValidatedResult<List<ReservationRequestSummary>>> getConfirmedByEvent(
            @PathVariable Long eventId) {

        var result = reservationRequestService.listForEmployee(eventId, ReservationRequestStatus.APPROVED);
        return handleResult(result, HttpStatus.OK, HttpStatus.UNAUTHORIZED);
    }
}

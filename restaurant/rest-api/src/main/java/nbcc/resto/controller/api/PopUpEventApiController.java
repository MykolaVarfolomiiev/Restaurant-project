package nbcc.resto.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.domain.dto.PopUpEvent;
import nbcc.resto.service.PopUpEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static nbcc.common.results.ResultHandler.handleResult;

@Tag(name = "Event API")
@RestController
@RequestMapping("/api/event")
public class PopUpEventApiController {

    private final PopUpEventService popUpEventService;

    public PopUpEventApiController(PopUpEventService popUpEventService) {
        this.popUpEventService = popUpEventService;
    }

    @Operation(summary = "Get all events")
    @GetMapping
    public ResponseEntity<Result<Collection<PopUpEvent>>> getAll() {
        var result = popUpEventService.getAll(true, true);
        return handleResult(result, HttpStatus.OK);
    }

    @Operation(summary = "Get event by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ValidatedResult<PopUpEvent>> get(@PathVariable Long id) {
        var result = popUpEventService.get(id, true, true);
        return handleResult(result, HttpStatus.OK, HttpStatus.NOT_FOUND);
    }
}

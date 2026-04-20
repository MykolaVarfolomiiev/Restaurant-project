package nbcc.resto.controller;

import jakarta.servlet.http.HttpServletRequest;
import nbcc.resto.domain.dto.DiningTable;
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

import static nbcc.common.validation.ModelErrorConverter.addErrorsToBindingResults;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/tables")
public class TableController {

    private final TableService tableService;
    private final Logger logger = LoggerFactory.getLogger(TableController.class);

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

// @PreAuthorize("permitAll()") only login users can access this page
@PreAuthorize("isAuthenticated()") // only login users can access this page
    @GetMapping
    public String getAll(Model model) {
        var result = tableService.getAll();

        if (result.isError()) {
            model.addAttribute("message", "Error retrieving tables");
            return "error/errorPage";
        }

        model.addAttribute("tables", result.getValue());
        return "table/list";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("table", new DiningTable());
        return "table/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("table") DiningTable table, BindingResult br) {
        var result = tableService.create(table);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "table");
            return "table/create";
        }

        return "redirect:/tables";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        var result = tableService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to edit was not found");
            return "error/errorPage";
        }

        model.addAttribute("table", result.getValue());
        return "table/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute("table") DiningTable table, BindingResult br, Model model) {
        table.setId(id);
        var result = tableService.update(table);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isInvalid()) {
            addErrorsToBindingResults(br, result, "table");
            model.addAttribute("table", table);
            return "table/edit";
        }

        return "redirect:/tables";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, Model model) {
        var result = tableService.get(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        if (result.isEmpty()) {
            model.addAttribute("message", "The table you are trying to delete was not found");
            return "error/errorPage";
        }

        model.addAttribute("table", result.getValue());
        return "table/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        var result = tableService.delete(id);

        if (result.isError()) {
            return "error/errorPage";
        }

        return "redirect:/tables";
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

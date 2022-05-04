package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static hexlet.code.app.controller.LabelController.LABEL_CONTROLLER_PATH;

@RestController
@RequestMapping("${base-url}" + LABEL_CONTROLLER_PATH)
public class LabelController {

    public final static String LABEL_CONTROLLER_PATH = "/labels";

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("")
    @Operation(summary = "Get list of all Labels")
    @ApiResponse(responseCode = "200", description = "List of all Labels",
        content = @Content( mediaType = "application/json",
                schema = @Schema(implementation = Label.class) )
    )
    public Iterable<Label> getAllLabels() {
        return labelService.getAllLabels();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Label by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label's data",
                content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = Label.class) )),
            @ApiResponse(responseCode = "404", description = "No Label with such id")
    })
    public Label getLabel(
            @Parameter(description = "Label's id to be found", required = true)
            @PathVariable Long id) {
        return labelService.getLabel(id);
    }

    @PostMapping("")
    @Operation(summary = "Create new Label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label created",
                content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = Label.class) )),
            @ApiResponse(responseCode = "422", description = "Invalid data given",
                content = @Content( mediaType = "application/json",
                    schema = @Schema(implementation = Label.class) ))
    })
    public Label createLabel(
            @Parameter(description = "Label's data to be created", required = true)
            @RequestBody @Valid LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Label with given id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label updated",
                    content = @Content( mediaType = "application/json",
                        schema = @Schema(implementation = Label.class) )),
            @ApiResponse(responseCode = "422", description = "Invalid data given"),
            @ApiResponse(responseCode = "404", description = "No Label with such id")
    })
    public Label updateLabel(
            @Parameter(description = "Label's id", required = true)
            @PathVariable Long id,
            @Parameter(description = "Label's data", required = true)
            @RequestBody @Valid LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label deleted"),
            @ApiResponse(responseCode = "403", description = "Unauthorized request"),
            @ApiResponse(responseCode = "404", description = "No Label with such id")
    })
    public void deleteLabel(
            @Parameter(description = "Label's id to be deleted", required = true)
            @PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}

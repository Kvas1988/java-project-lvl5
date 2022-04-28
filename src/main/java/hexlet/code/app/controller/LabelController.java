package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
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
    public Iterable<Label> getAllLabels() {
        return labelService.getAllLabels();
    }

    @GetMapping("/{id}")
    public Label getLabel(@PathVariable Long id) {
        return labelService.getLabel(id);
    }

    @PostMapping("")
    public Label createLabel(@RequestBody @Valid LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable Long id, @RequestBody @Valid LabelDto labelDto) {
        return labelService.updateLabel(id, labelDto);
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
    }
}

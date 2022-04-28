package hexlet.code.app.service;

import hexlet.code.app.dto.LabelDto;
import hexlet.code.app.model.Label;

public interface LabelService {
    Label createLabel(LabelDto givenLabel);
    Label updateLabel(Long id, LabelDto newData);
    void deleteLabel(Long id);
    Label getLabel(Long id);
    Iterable<Label> getAllLabels();
}

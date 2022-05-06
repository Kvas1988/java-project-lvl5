package hexlet.code.service;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;

public interface LabelService {
    Label createLabel(LabelDto givenLabel);
    Label updateLabel(Long id, LabelDto newData);
    void deleteLabel(Long id);
    Label getLabel(Long id);
    Iterable<Label> getAllLabels();
}

package hexlet.code.service.impl;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import hexlet.code.service.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public Label createLabel(LabelDto givenLabel) {
        Label label = new Label();
        label.setName(givenLabel.getName());
        log.info("Saving new label: " + label.getName());
        // TODO check if name already exists ???
        return labelRepository.save(label);
    }

    @Override
    public Label updateLabel(Long id, LabelDto newData) {
        Label label = this.getLabel(id);
        label.setName(newData.getName());
        return labelRepository.save(label);
    }

    @Override
    public void deleteLabel(Long id) {
        Label label = this.getLabel(id);
        log.info("Deleting label: " + label.getName());
        labelRepository.deleteById(id); // SqlExceptionHelper
    }

    @Override
    public Label getLabel(Long id) {
        return labelRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("No such label"));
    }

    @Override
    public Iterable<Label> getAllLabels() {
        return labelRepository.findAll();
    }
}

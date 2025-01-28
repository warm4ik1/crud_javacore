package org.warm4ik.crud.controller;

import lombok.RequiredArgsConstructor;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.repository.LabelRepository;
import org.warm4ik.crud.status.Status;

import java.util.List;

@RequiredArgsConstructor
public class LabelController {
    private final LabelRepository labelRepository;

    public Label addNewLabel(String name, Status status){
        Label label = new Label(name, status);
        return labelRepository.update(label);
    }

    public Label getLabel(Long id){
        return labelRepository.getById(id);
    }
    public List<Label> getLabels(){
        return labelRepository.getAll();
    }
    public Label updateLabel(Long id, String name){
        Label label = labelRepository.getById(id);
        label.setName(name);
        return labelRepository.update(label);
    }
    public void deleteLabel(Long id){
        labelRepository.deleteById(id);
    }
}

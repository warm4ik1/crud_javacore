package org.warm4ik.crud.repository.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Builder;
import org.warm4ik.crud.GenID.IdGenerator;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.repository.LabelRepository;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class GsonLabelRepository implements LabelRepository, IdGenerator {
    private final String BASE_PATH = "src/main/resources/labels.json";
    private final Gson gson = new Gson();

    @Override
    public Label getById(Long id) {
        List<Label> labels = loadLabels();
        return labels.stream().filter(label -> label.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<Label> getAll() {
        return loadLabels();
    }


    @Override
    public Label save(Label label) {
        List<Label> labels = loadLabels();
        labels.add(label);
        saveAll(labels);
        return label;
    }


    @Override
    public Label update(Label updatedLabel) {
        List<Label> labels = loadLabels();
        if (updatedLabel.getId() == null){
            updatedLabel.setId(generateId());
            labels.add(updatedLabel);
            saveAll(labels);
            return updatedLabel;
        } else {
            return labels.stream()
                    .filter(label -> label.getId().equals(updatedLabel.getId()))
                    .findFirst()
                    .map(existingLabel -> {
                        int index = labels.indexOf(existingLabel);
                        labels.set(index, updatedLabel);
                        saveAll(labels);
                        return updatedLabel;
                    })
                    .orElse(null);
        }
    }

    @Override
    public void deleteById(Long id) {
        List<Label> labels = loadLabels();
        boolean removed = labels.removeIf(label -> label.getId().equals(id));
        if (!removed) {
            System.out.println("Метка с id {" + id + "} не найдена.");
        }
        saveAll(labels);
    }


    @Override
    public Long generateId() {
        List<Label> labels = loadLabels();
        if (labels.isEmpty()) {
            return 1L;
        }
        return labels.stream().mapToLong(Label::getId).max().orElse(0L) + 1L;
    }

    private List<Label> loadLabels() {
        try (FileReader reader = new FileReader(BASE_PATH)) {
            Type listType = new TypeToken<List<Label>>() {
            }.getType();
            List<Label> list = gson.fromJson(reader, listType);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }


    private void saveAll(List<Label> labels) {
        try (FileWriter writer = new FileWriter(BASE_PATH)) {
            gson.toJson(labels, writer);
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }
}

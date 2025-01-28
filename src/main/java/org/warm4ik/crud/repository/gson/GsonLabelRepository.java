package org.warm4ik.crud.repository.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.repository.LabelRepository;
import org.warm4ik.crud.status.Status;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonLabelRepository implements LabelRepository {
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

        // Генерация ID для метки, если его нет
        if (label.getId() == null) {
            long nextId = labels.isEmpty() ? 1 : labels.stream().mapToLong(Label::getId).max().orElse(0L) + 1;
            label.setId(nextId);
        }

        labels.add(label);
        saveAll(labels);
        return label;
    }

    @Override
    public Label update(Label updatedLabel) {
        List<Label> labels = loadLabels();

        // Если у метки нет ID, генерируем его
        if (updatedLabel.getId() == null) {
            long nextId = labels.isEmpty() ? 1 : labels.stream().mapToLong(Label::getId).max().orElse(0L) + 1;
            updatedLabel.setId(nextId);
            labels.add(updatedLabel);
            saveAll(labels);
            return updatedLabel;
        } else {
            // Ищем метку с таким же ID для обновления
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
        labels.stream()
                .filter(label -> label.getId().equals(id))
                .findFirst()
                .ifPresent(label -> {
                    label.setStatus(Status.DELETED);  // Меняем статус на DELETED
                    saveAll(labels);  // Сохраняем обновленный список
                });
    }

    // Метод для загрузки меток из файла
    private List<Label> loadLabels() {
        try (FileReader reader = new FileReader(BASE_PATH)) {
            Type listType = new TypeToken<List<Label>>() {}.getType();
            List<Label> list = gson.fromJson(reader, listType);
            return list == null ? new ArrayList<>() : list;
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Метод для сохранения всех меток в файл
    private void saveAll(List<Label> labels) {
        try (FileWriter writer = new FileWriter(BASE_PATH)) {
            gson.toJson(labels, writer);
        } catch (IOException e) {
            System.out.println("Ошибка записи файла: " + e.getMessage());
        }
    }
}

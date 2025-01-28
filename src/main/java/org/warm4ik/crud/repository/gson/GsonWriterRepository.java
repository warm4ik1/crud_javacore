package org.warm4ik.crud.repository.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.warm4ik.crud.model.Writer;
import org.warm4ik.crud.model.Post;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.repository.WriterRepository;
import org.warm4ik.crud.status.Status;
import org.warm4ik.crud.typeDataAdaptor.LocalDateTimeAdapter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GsonWriterRepository implements WriterRepository {
    private final String BASE_PATH = "src/main/resources/writers.json";
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private long postIdCounter = 1;
    private long labelIdCounter = 1;

    private List<Writer> loadWriters() {
        Path path = Paths.get(BASE_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Ошибка при создании файла: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        try (FileReader reader = new FileReader(BASE_PATH)) {
            Type listType = new TypeToken<List<Writer>>() {}.getType();
            List<Writer> writers = gson.fromJson(reader, listType);
            return writers == null ? new ArrayList<>() : writers;
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Сохранение авторов в JSON-файл
    private void saveWriters(List<Writer> writers) {
        try (FileWriter writer = new FileWriter(BASE_PATH)) {
            gson.toJson(writers, writer);
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    @Override
    public Writer getById(Long id) {
        return loadWriters().stream()
                .filter(writer -> writer.getId() != null && writer.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Writer> getAll() {
        return loadWriters();
    }

    @Override
    public Writer save(Writer writer) {
        List<Writer> writers = loadWriters();
        long nextId = writers.isEmpty() ? 1 : writers.stream()
                .filter(w -> w.getId() != null)
                .mapToLong(Writer::getId)
                .max()
                .orElse(0) + 1;

        List<Post> posts = writer.getPosts();
        if (posts != null) {
            for (Post post : posts) {
                if (post.getId() == null) {
                    post.setId(postIdCounter++);
                }
                if (post.getCreated() == null) {
                    post.setCreated(LocalDateTime.now());
                }
                post.setUpdated(LocalDateTime.now());

                // Обработка меток
                List<Label> labels = post.getLabels();
                if (labels != null) {
                    for (Label label : labels) {
                        if (label.getId() == null) {
                            label.setId(labelIdCounter++);
                        }
                    }
                }
            }
        }

        Writer newWriter = Writer.builder()
                .id(nextId)
                .firstName(writer.getFirstName())
                .lastName(writer.getLastName())
                .posts(posts != null ? posts : new ArrayList<>())
                .status(Status.ACTIVE)
                .build();

        writers.add(newWriter);
        saveWriters(writers);
        return newWriter;
    }

    @Override
    public Writer update(Writer writer) {
        List<Writer> writers = loadWriters();
        for (int i = 0; i < writers.size(); i++) {
            Writer existingWriter = writers.get(i);
            if (existingWriter.getId() != null && existingWriter.getId().equals(writer.getId())) {
                List<Post> posts = writer.getPosts();
                if (posts != null) {
                    for (Post post : posts) {
                        if (post.getId() == null) {
                            post.setId(postIdCounter++);
                        }
                        if (post.getCreated() == null) {
                            post.setCreated(LocalDateTime.now());
                        }
                        post.setUpdated(LocalDateTime.now());

                        List<Label> labels = post.getLabels();
                        if (labels != null) {
                            for (Label label : labels) {
                                if (label.getId() == null) {
                                    label.setId(labelIdCounter++);
                                }
                            }
                        }
                    }
                }

                Writer updatedWriter = Writer.builder()
                        .id(writer.getId())
                        .firstName(writer.getFirstName())
                        .lastName(writer.getLastName())
                        .posts(posts != null ? posts : new ArrayList<>())
                        .status(writer.getStatus() != null ? writer.getStatus() : Status.ACTIVE)
                        .build();

                writers.set(i, updatedWriter);
                saveWriters(writers);
                return updatedWriter;
            }
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        List<Writer> writers = loadWriters();
        for (int i = 0; i < writers.size(); i++) {
            Writer writer = writers.get(i);
            if (writer.getId() != null && writer.getId().equals(id)) {
                writer.setStatus(Status.DELETED);
                writers.set(i, writer);
                saveWriters(writers);
                return;
            }
        }
    }
}

package org.warm4ik.crud.controller;

import lombok.RequiredArgsConstructor;
import org.warm4ik.crud.model.Post;
import org.warm4ik.crud.model.Writer;
import org.warm4ik.crud.repository.WriterRepository;

import java.util.List;

@RequiredArgsConstructor
public class WriterController {
    private final WriterRepository writerRepository;

    public Writer createWriter(String firstName, String lastName, List<Post> posts) {
        Writer writer = Writer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .posts(posts)
                .build();
        return writerRepository.save(writer);
    }

    public Writer getWriterById(Long id) {
        return writerRepository.getById(id);
    }

    public List<Writer> getAllWriters() {
        return writerRepository.getAll();
    }

    public Writer updateWriter(Writer writer) {
        return writerRepository.update(writer);
    }

    public void deleteWriter(Long id) {
        writerRepository.deleteById(id);
    }
}

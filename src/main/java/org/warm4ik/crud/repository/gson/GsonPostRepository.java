package org.warm4ik.crud.repository.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.warm4ik.crud.model.Post;
import org.warm4ik.crud.repository.PostRepository;
import org.warm4ik.crud.status.PostStatus;
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

public class GsonPostRepository implements PostRepository {
    private final String BASE_PATH = "src/main/resources/posts.json";
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private List<Post> loadPosts() {
        Path path = Paths.get(BASE_PATH);
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        try (FileReader reader = new FileReader(BASE_PATH)) {
            Type listType = new TypeToken<List<Post>>() {
            }.getType();
            List<Post> posts = gson.fromJson(reader, listType);
            return posts == null ? new ArrayList<>() : posts;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void savePosts(List<Post> posts) {
        try (FileWriter writer = new FileWriter(BASE_PATH)) {
            gson.toJson(posts, writer);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    @Override
    public Post getById(Long id) {
        return loadPosts().stream()
                .filter(p -> p.getId() != null && p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Post> getAll() {
        return loadPosts();
    }

    @Override
    public Post save(Post post) {
        List<Post> posts = loadPosts();
        long nextId = posts.isEmpty() ? 1 : posts.stream()
                .filter(p -> p.getId() != null)
                .mapToLong(Post::getId)
                .max()
                .orElse(0) + 1;
        Post newPost = Post.builder()
                .id(nextId)
                .content(post.getContent())
                .labels(post.getLabels() != null ? post.getLabels() : new ArrayList<>())
                .postStatus(PostStatus.ACTIVE)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        posts.add(newPost);
        savePosts(posts);
        return newPost;
    }

    @Override
    public Post update(Post post) {
        List<Post> posts = loadPosts();
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i).getId() != null && posts.get(i).getId().equals(post.getId())) {
                Post updatedPost = Post.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .labels(post.getLabels() != null ? post.getLabels() : new ArrayList<>())
                        .postStatus(PostStatus.UNDER_REVIEW)
                        .created(posts.get(i).getCreated())
                        .updated(LocalDateTime.now())
                        .build();
                posts.set(i, updatedPost);
                savePosts(posts);
                return updatedPost;
            }
        }
        return null;
    }

    @Override
    public void deleteById(Long id) {
        List<Post> posts = loadPosts();
        for (int i = 0; i < posts.size(); i++) {
            Post post = posts.get(i);
            if (post.getId() != null && post.getId().equals(id)) {
                Post updatedPost = Post.builder()
                        .id(post.getId())
                        .content(post.getContent())
                        .labels(post.getLabels() != null ? post.getLabels() : new ArrayList<>())
                        .postStatus(PostStatus.DELETED)
                        .created(post.getCreated())
                        .updated(LocalDateTime.now())
                        .build();
                posts.set(i, updatedPost);
                savePosts(posts);
                return;
            }
        }
    }
}

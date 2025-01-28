package org.warm4ik.crud.controller;

import lombok.RequiredArgsConstructor;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.model.Post;
import org.warm4ik.crud.repository.PostRepository;
import org.warm4ik.crud.status.PostStatus;

import java.util.List;

@RequiredArgsConstructor
public class PostController {
    private final PostRepository postRepository;

    public Post createPost(String content, List<Label> labels, PostStatus postStatus) {
        Post post = Post.builder()
                .content(content)
                .labels(labels)
                .postStatus(postStatus)
                .build();
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.getAll();
    }

    public Post updatePost(Post post) {
        return postRepository.update(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post getPostById(Long id) {
        return postRepository.getById(id);
    }

}
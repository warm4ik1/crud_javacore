package org.warm4ik.crud.view;

import org.warm4ik.crud.controller.PostController;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.model.Post;
import org.warm4ik.crud.status.PostStatus;
import org.warm4ik.crud.status.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PostView {
    private final PostController postController;
    private final Scanner scanner = new Scanner(System.in);

    public PostView(PostController postController) {
        this.postController = postController;
    }

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    createPost();
                    break;
                case 2:
                    getPostById();
                    break;
                case 3:
                    getAllPosts();
                    break;
                case 4:
                    updatePost();
                    break;
                case 5:
                    deletePost();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Некорректный ввод. Пожалуйста, выберите пункт из меню.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\nМеню управления постами:");
        System.out.println("1. Создать пост");
        System.out.println("2. Получить пост по id");
        System.out.println("3. Получить все посты");
        System.out.println("4. Обновить пост");
        System.out.println("5. Удалить пост");
        System.out.println("0. Выход");
        System.out.print("Введите ваш выбор: ");
    }

    private int getChoice() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine();
            System.out.println("Некорректный ввод. Пожалуйста, введите число.");
            return -1;
        } finally {
            scanner.nextLine();
        }
    }

    private void createPost() {
        System.out.print("Введите контент поста: ");
        String content = scanner.nextLine();
        List<Label> labels = getLabels();
        PostStatus postStatus = PostStatus.ACTIVE;
        Post post = postController.createPost(content, labels, postStatus);
        if (post != null) {
            System.out.println("Пост создан успешно с ID: " + post.getId());
        } else {
            System.out.println("Ошибка создания поста.");
        }
    }


    private void getPostById() {
        System.out.print("Введите id поста: ");
        try {
            long id = scanner.nextLong();
            Post post = postController.getPostById(id);
            if (post != null) {
                System.out.println(post);
            } else {
                System.out.println("Пост с id {" + id + "} не найден.");
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        } finally {
            scanner.nextLine();
        }
    }

    private void getAllPosts() {
        postController.getAllPosts().forEach(System.out::println);
    }

    private void updatePost() {
        System.out.print("Введите id поста для обновления: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            Post post = postController.getPostById(id);
            if (post != null) {
                System.out.print("Введите новый контент поста: ");
                String newContent = scanner.nextLine();
                List<Label> newLabels = getLabels();
                post.setContent(newContent);
                post.setLabels(newLabels);
                Post updatedPost = postController.updatePost(post);
                if (updatedPost != null) {
                    System.out.println("Пост обновлен успешно.");
                } else {
                    System.out.println("Пост с id {" + id + "} не найден.");
                }
            } else {
                System.out.println("Пост с id {" + id + "} не найден.");
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }
    }

    private void deletePost() {
        System.out.print("Введите id поста для удаления: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            postController.deletePost(id);
            System.out.println("Пост удален успешно.");
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }
    }

    private List<Label> getLabels() {
        List<Label> labels = new ArrayList<>();
        System.out.print("Введите количество меток для поста: ");
        try {
            int numLabels = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < numLabels; i++) {
                System.out.print("Введите имя для метки " + (i + 1) + ": ");
                String labelName = scanner.nextLine();
                labels.add(Label.builder().name(labelName).status(Status.ACTIVE).build());
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }

        return labels;
    }
}
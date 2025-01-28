package org.warm4ik.crud.view;

import org.warm4ik.crud.controller.WriterController;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.model.Post;
import org.warm4ik.crud.model.Writer;
import org.warm4ik.crud.status.PostStatus;
import org.warm4ik.crud.status.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WriterView {
    private final WriterController writerController;
    private final Scanner scanner = new Scanner(System.in);

    public WriterView(WriterController writerController) {
        this.writerController = writerController;
    }

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    createWriter();
                    break;
                case 2:
                    getWriterById();
                    break;
                case 3:
                    getAllWriters();
                    break;
                case 4:
                    updateWriter();
                    break;
                case 5:
                    deleteWriter();
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
        System.out.println("\nМеню управления авторами:");
        System.out.println("1. Создать автора");
        System.out.println("2. Получить автора по id");
        System.out.println("3. Получить всех авторов");
        System.out.println("4. Обновить автора");
        System.out.println("5. Удалить автора");
        System.out.println("0. Выход");
        System.out.print("Введите ваш выбор: ");
    }

    private int getChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Некорректный ввод. Пожалуйста, введите число.");
            scanner.next();
        }
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    private void createWriter() {
        System.out.print("Введите имя автора: ");
        String firstName = scanner.nextLine();
        System.out.print("Введите фамилию автора: ");
        String lastName = scanner.nextLine();
        List<Post> posts = getPosts();
        Writer writer = writerController.createWriter(firstName, lastName, posts);
        if (writer != null) {
            System.out.println("Автор создан успешно с ID: " + writer.getId());
        } else {
            System.out.println("Ошибка создания автора.");
        }
    }

    private void getWriterById() {
        System.out.print("Введите id автора: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            Writer writer = writerController.getWriterById(id);
            if (writer != null) {
                System.out.println(writer);
            } else {
                System.out.println("Автор с id {" + id + "} не найден.");
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }
    }

    private void getAllWriters() {
        writerController.getAllWriters().forEach(System.out::println);
    }

    private void updateWriter() {
        System.out.print("Введите id автора для обновления: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            Writer writer = writerController.getWriterById(id);
            if (writer != null) {
                System.out.print("Введите новое имя автора: ");
                String newFirstName = scanner.nextLine();
                System.out.print("Введите новую фамилию автора: ");
                String newLastName = scanner.nextLine();
                writer.setFirstName(newFirstName);
                writer.setLastName(newLastName);
                writer.setPosts(getPosts());
                Writer updatedWriter = writerController.updateWriter(writer);
                if (updatedWriter != null) {
                    System.out.println("Автор обновлен успешно.");
                } else {
                    System.out.println("Автор с id {" + id + "} не найден.");
                }
            } else {
                System.out.println("Автор с id {" + id + "} не найден.");
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }
    }

    private void deleteWriter() {
        System.out.print("Введите id автора для удаления: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            Writer writer = writerController.getWriterById(id);
            if (writer != null && writer.getStatus() != Status.DELETED) {
                writerController.deleteWriter(id);
                System.out.println("Автора с id {" + id + "} удалён.");
            } else {
                System.out.println("Автор с id {" + id + "} не найден или уже удалён.");
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }
    }

    private List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        System.out.print("Введите количество постов автора: ");
        try {
            int numPosts = scanner.nextInt();
            scanner.nextLine();
            for (int i = 0; i < numPosts; i++) {
                System.out.print("Введите контент для поста " + (i + 1) + ": ");
                String content = scanner.nextLine();
                List<Label> labels = getLabels();
                posts.add(Post.builder()
                        .content(content)
                        .labels(labels)
                        .created(LocalDateTime.now())
                        .updated(LocalDateTime.now())
                        .postStatus(PostStatus.ACTIVE)
                        .build());
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        }
        return posts;
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

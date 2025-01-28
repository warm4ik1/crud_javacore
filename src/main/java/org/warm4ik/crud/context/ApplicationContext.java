package org.warm4ik.crud.context;

import org.warm4ik.crud.view.LabelView;
import org.warm4ik.crud.view.PostView;
import org.warm4ik.crud.view.WriterView;
import org.warm4ik.crud.controller.LabelController;
import org.warm4ik.crud.controller.PostController;
import org.warm4ik.crud.controller.WriterController;
import org.warm4ik.crud.repository.LabelRepository;
import org.warm4ik.crud.repository.PostRepository;
import org.warm4ik.crud.repository.WriterRepository;
import org.warm4ik.crud.repository.gson.GsonLabelRepository;
import org.warm4ik.crud.repository.gson.GsonPostRepository;
import org.warm4ik.crud.repository.gson.GsonWriterRepository;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplicationContext {
    private final Scanner scanner = new Scanner(System.in);
    private final LabelView labelView;
    private final PostView postView;
    private final WriterView writerView;

    public ApplicationContext() {
        LabelRepository labelRepository = new GsonLabelRepository();
        PostRepository postRepository = new GsonPostRepository();
        WriterRepository writerRepository = new GsonWriterRepository();

        LabelController labelController = new LabelController(labelRepository);
        PostController postController = new PostController(postRepository);
        WriterController writerController = new WriterController(writerRepository);

        this.labelView = new LabelView(labelController);
        this.postView = new PostView(postController);
        this.writerView = new WriterView(writerController);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("Выберите действие:");
            System.out.println("1. Запустить LabelView");
            System.out.println("2. Запустить PostView");
            System.out.println("3. Запустить WriterView");
            System.out.println("4. Выход");
            System.out.print("Введите номер действия: ");
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        labelView.run();
                        break;
                    case 2:
                        postView.run();
                        break;
                    case 3:
                        writerView.run();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Выход из программы...");
                        break;
                    default:
                        System.out.println("Неверный ввод, пожалуйста, выберите действительный номер.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Некорректный формат ввода. Пожалуйста, введите целое число.");
                scanner.next();
            }
        }
    }
}

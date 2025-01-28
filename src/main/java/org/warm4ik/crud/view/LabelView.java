package org.warm4ik.crud.view;

import org.warm4ik.crud.controller.LabelController;
import org.warm4ik.crud.model.Label;
import org.warm4ik.crud.status.Status;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LabelView {
    private final LabelController labelController;
    private final Scanner scanner;

    public LabelView(LabelController labelController) {
        this.labelController = labelController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("1. Добавить метку");
            System.out.println("2. Получить метку по id");
            System.out.println("3. Получить все метки");
            System.out.println("4. Обновить метку id");
            System.out.println("5. Удалить метку по id");
            System.out.println("0. Выход.");
            System.out.println("Введите нужное число");
            try {
                int input = scanner.nextInt();
                scanner.nextLine();
                switch (input) {
                    case 1:
                        addLabel();
                        break;
                    case 2:
                        getLabelById();
                        break;
                    case 3:
                        getAllLabels();
                        break;
                    case 4:
                        updateLabelById();
                        break;
                    case 5:
                        deleteLabelById();
                        break;
                    case 0:
                        running = false;
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: Некорректный формат ввода. Пожалуйста, введите целое число.");
                scanner.next();
            }
        }
    }

    public void addLabel() {
        System.out.println("Введите поле name для new label:");
        String name = scanner.nextLine();
        try {
            Label newLabel = labelController.addNewLabel(name, Status.ACTIVE);
            System.out.println("New label с id{" + newLabel.getId() + "} успено добавлен.");
        } catch (Exception e) {
            System.out.println("Ошибка добавления label: " + e.getMessage());
        }
    }

    public void getLabelById() {
        System.out.println("Введите id для поиска label:");
        Long id = scanner.nextLong();
        scanner.nextLine();

        System.out.println(labelController.getLabel(id));
    }

    public void getAllLabels() {
        System.out.println(labelController.getLabels());
    }

    public void updateLabelById() {
        System.out.println("Введите id для update label:");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            System.out.println("Введите поле name для обновления:");
            String name = scanner.nextLine();
            labelController.updateLabel(id, name);
            System.out.println("Label с id{" + id + "} был обновлен.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Некорректные данные, введите число.");
        } catch (Exception e) {
            System.out.println("Ошибка обновления label: " + e.getMessage());
        }

    }

    public void deleteLabelById() {
        System.out.println("Введите id label для удаления:");
        Long id = scanner.nextLong();
        scanner.nextLine();
        labelController.deleteLabel(id);
        System.out.println("Удаление успешно завершенно.");
    }

}

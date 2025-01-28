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

    public void run() {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
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
                default:
                    System.out.println("Некорректный ввод. Пожалуйста, выберите пункт из меню.");
            }
        }
    }

    private void showMenu() {
        System.out.println("\nМеню управления метками:");
        System.out.println("1. Добавить метку");
        System.out.println("2. Получить метку по id");
        System.out.println("3. Получить все метки");
        System.out.println("4. Обновить метку");
        System.out.println("5. Удалить метку");
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

    private void addLabel() {
        System.out.print("Введите имя метки: ");
        String name = scanner.nextLine();
        try {
            Label newLabel = labelController.addNewLabel(name, Status.ACTIVE);
            System.out.println("Метка успешно добавлена с ID: " + newLabel.getId());
        } catch (Exception e) {
            System.out.println("Ошибка добавления метки: " + e.getMessage());
        }
    }

    private void getLabelById() {
        System.out.print("Введите id метки: ");
        try {
            long id = scanner.nextLong();
            Label label = labelController.getLabel(id);
            if (label != null) {
                System.out.println("Найденная метка: " + label);
            } else {
                System.out.println("Метка с id {" + id + "} не найдена.");
            }
        } catch (Exception e) {
            System.out.println("Некорректный ввод.");
            scanner.next();
        } finally {
            scanner.nextLine();
        }
    }

    private void getAllLabels() {
        System.out.println("\nВсе метки:");
        labelController.getLabels().forEach(label -> System.out.println(label));
    }

    private void updateLabelById() {
        System.out.print("Введите id метки для обновления: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            Label label = labelController.getLabel(id);
            if (label != null) {
                System.out.print("Введите новое имя метки: ");
                String newName = scanner.nextLine();
                label.setName(newName);
                Label updatedLabel = labelController.updateLabel(id, newName);
                System.out.println("Метка обновлена успешно: " + updatedLabel);
            } else {
                System.out.println("Метка с id {" + id + "} не найдена.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка обновления метки: " + e.getMessage());
        }
    }

    private void deleteLabelById() {
        System.out.print("Введите id метки для удаления: ");
        try {
            long id = scanner.nextLong();
            scanner.nextLine();
            labelController.deleteLabel(id);
            System.out.println("Метка с id {" + id + "} успешно удалена.");
        } catch (Exception e) {
            System.out.println("Некорректный ввод. Ошибка удаления.");
            scanner.next();
        }
    }
}

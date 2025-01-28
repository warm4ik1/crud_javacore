package org.warm4ik.crud;


import org.warm4ik.crud.controller.LabelController;
import org.warm4ik.crud.repository.LabelRepository;
import org.warm4ik.crud.repository.gson.GsonLabelRepository;
import org.warm4ik.crud.view.LabelView;



public class Main {
    public static void main(String[] args) {
        LabelRepository labelRepository = new GsonLabelRepository();
        LabelController labelController = new LabelController(labelRepository);
        LabelView labelView = new LabelView(labelController);

        labelView.start();
    }
}
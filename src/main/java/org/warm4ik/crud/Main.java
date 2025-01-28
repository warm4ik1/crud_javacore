package org.warm4ik.crud;


import org.warm4ik.crud.controller.LabelController;
import org.warm4ik.crud.controller.PostController;
import org.warm4ik.crud.repository.LabelRepository;
import org.warm4ik.crud.repository.gson.GsonLabelRepository;
import org.warm4ik.crud.repository.gson.GsonPostRepository;
import org.warm4ik.crud.view.LabelView;
import org.warm4ik.crud.view.PostView;


public class Main {
    public static void main(String[] args) {
//        LabelRepository labelRepository = new GsonLabelRepository();
//        LabelController labelController = new LabelController(labelRepository);
//        LabelView labelView = new LabelView(labelController);
//
//        labelView.start();

        GsonPostRepository postRepository = new GsonPostRepository();
        PostController postController = new PostController(postRepository);
        PostView postView = new PostView(postController);

        postView.run();

    }
}
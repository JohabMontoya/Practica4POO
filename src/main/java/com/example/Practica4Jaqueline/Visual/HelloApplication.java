package com.example.Practica4Jaqueline.Visual;

import com.example.Practica4Jaqueline.ShobuUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) {
        ShobuUI root = new ShobuUI();
        Scene scene = new Scene(root, 980, 760);
        stage.setTitle("Shobu - Practica 4");
        stage.setScene(scene);
        stage.show();
    }
}
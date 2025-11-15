package com.aressoftware;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane(new Label("Hola JavaFX con Maven y PostgreSQL!"));
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.setTitle("Ares Software");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

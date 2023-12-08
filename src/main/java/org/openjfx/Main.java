package org.openjfx;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        // System.out.println("Hello world!");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



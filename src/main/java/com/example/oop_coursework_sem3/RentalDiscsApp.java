package com.example.oop_coursework_sem3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RentalDiscsApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(RentalDiscsApp.class.getResource("rentalDiscs.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 825, 559);
        stage.setTitle("Пункт проката видео дисков");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
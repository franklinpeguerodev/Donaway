package com.donaway.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
        	Parent root = FXMLLoader.load(
        		    getClass().getResource("/vistas_personal_medico/plantillaInicio_PersonalMedico.fxml")
        		);Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("DonaWay RD");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
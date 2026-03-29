package com.donaway.main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

import com.donaway.dev.db.ConexionBdNube;
import com.donaway.dev.db.WebServerBD;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/view/user/form.fxml");
        
        if (fxmlLocation == null) {
            System.out.println("Error: No se encontró el archivo FXML en la ruta especificada.");
            System.exit(1);
        }
        Parent root = FXMLLoader.load(fxmlLocation);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("DonaWay - Gestión de Donaciones");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
    	WebServerBD.inicializar();
    	ConexionBdNube.getConnection();
        launch(args); 
    }
}
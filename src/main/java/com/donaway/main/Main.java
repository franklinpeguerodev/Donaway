package com.donaway.main;
//importaciones para la creacion de la ventana javafx
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;


import com.donaway.utils.db.ConexionDbDonaway;

import com.donaway.model.persona.Donante;
import com.donaway.service.DonanteServices;
// Servidor web de la BD para comodidad para el desorrollo  


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL fxmlLocation = getClass().getResource("/Vistas_FXML/Plantilla_inicio.fxml");
        
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
 
    	try {
    		ConexionDbDonaway.getConnection();
    	}catch (SQLException e) {
    		System.out.println("no sirvio");
    	}
        launch(args); 
    }
}
package com.donaway.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.SQLException;
import com.donaway.utils.db.ConexionDbDonaway;
import com.donaway.model.persona.Donante;
import com.donaway.service.DonanteServices;
// Servidor web de la BD para comodidad para el desorrollo  

/*
 * try {
        	Parent root = FXMLLoader.load(
        		    getClass().getResource("/views/vistas_genericas/Plantilla_personal_medico.fxml")
        		);Scene scene = new Scene(root);
    	
 */

public class Main extends Application {

    @Override
    public void start(Stage stage) {
    	try {
        	Parent root = FXMLLoader.load(
        		    getClass().getResource("/views/vistas_formularios/vistas_inventario/Plantilla_personal_inventario.fxml")
        		);Scene scene = new Scene(root);
    	

            stage.setScene(scene);
            stage.setTitle("DonaWay RD");
            stage.getIcons().add(new Image("/img/img_generales/Logo.png"));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
    public static void main(String[] args) {
        launch();
    }}

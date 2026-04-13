package com.donaway.main;

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



public class Main extends Application {

    @Override
    public void start(Stage stage) {
    	try {
        	Parent root = FXMLLoader.load(
        		    getClass().getResource("/views/vistas_genericas/plantilla_personal_medico.fxml") ///views/vistas_genericas/plantilla_personal_medico.fxml
        		);Scene scene = new Scene(root);													////views/vistas_formularios/vistas_generales/login.fxml
        		

            stage.setScene(scene);
            stage.setTitle("DonaWay RD");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
            
    public static void main(String[] args) {
        launch();
    }}

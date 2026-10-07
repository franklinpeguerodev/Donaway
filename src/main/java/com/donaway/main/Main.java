package com.donaway.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Main extends Application {

    @Override
    public void start(Stage stage) {
    	try {
        	Parent root = FXMLLoader.load(

        		    getClass().getResource("/views/vistas_formularios/vistas_generales/login.fxml")
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

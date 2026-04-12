package com.donaway.controller.controllerpersonalmedico;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import org.controlsfx.control.PopOver;



import java.io.IOException;
import java.util.List;

public class VistasEmergentes {

	public void cargarCentroGestionFormularios(ScrollPane contenedor, List<ModuloFormularios> listaModulos) {
	    try {
	        // 1. Cargar la estructura base del BorderPane
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_formularios/vistas_personal_medico/vistas_bioanalista/panel_formularios_analab.fxml"));
	        BorderPane estructuraBase = loader.load();

	        // 2. Referenciar los contenedores internos del BorderPane
	        // Usamos lookup con los IDs que pusiste en el FXML
	        VBox listaLateral = (VBox) estructuraBase.getLeft();
	        Pane areaCentral = (Pane) estructuraBase.getCenter();

	       
	        listaLateral.getChildren().clear();
	        listaLateral.setSpacing(10);

	        for (ModuloFormularios modulo : listaModulos) {
	            Button btn = new Button(modulo.getNombreFormulario());
	            
	            
	            btn.setMaxWidth(Double.MAX_VALUE); 
	            btn.setAlignment(Pos.CENTER_LEFT);
	            btn.getStyleClass().add("botones_navegacion_interna"); 

	            
	            btn.setOnAction(e -> inyectarFormulario(areaCentral, modulo.getRutaFXML()));

	            listaLateral.getChildren().add(btn);
	        }

	        if (!listaModulos.isEmpty()) {
	            inyectarFormulario(areaCentral, listaModulos.get(0).getRutaFXML());
	        }

	        contenedor.setContent(estructuraBase);

	    } catch (IOException e) {
	        System.err.println("Error Institucional: No se pudo cargar el panel de formularios.");
	        e.printStackTrace();
	    }
	}

	
	private void inyectarFormulario(Pane contenedor, String ruta) {
	    try {
	        Parent form = FXMLLoader.load(getClass().getResource(ruta));
	        contenedor.getChildren().setAll(form);
	    } catch (IOException e) {
	        System.err.println("Error al inyectar formulario: " + ruta);
	    }
	}
	
	
	
	    private PopOver popOverUbicacion;

	    public void mostrarPopOverUbicacion(Button selector) {
	   
	        if (popOverUbicacion != null && popOverUbicacion.isShowing()) {
	            popOverUbicacion.hide();
	            return;
	        }

	        try {
	            
	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_formularios/vistas_generales/form_ubicacion.fxml"));
	            Parent root = loader.load();

	            popOverUbicacion = new PopOver(root);
	            popOverUbicacion.setArrowLocation(PopOver.ArrowLocation.LEFT_TOP);
	            popOverUbicacion.setCornerRadius(10);
	            popOverUbicacion.setAnimated(true);
	            popOverUbicacion.setTitle("Ubicación Geográfica");

	            popOverUbicacion.show(selector);

	        } catch (IOException e) {
	            System.err.println("Error: No se pudo cargar el FXML de ubicacion.");
	            e.printStackTrace();
	        }
	    }
	}

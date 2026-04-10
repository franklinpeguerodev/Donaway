package com.donaway.controller.controllerpersonalmedico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

public class ControllerGraficosIndependiente {
	 @FXML private Button btn_analisisEsperaCentral;
	    @FXML private Button btn_pacientesRecibidosCentral;
	    @FXML private Button btn_sangreReservadaCentral;

	 
	    private ControllerPrincipal principalController;
	   
public void setPrincipalController(ControllerPrincipal controller) {
	 this.principalController = controller;
}

	    @FXML
	    public void initialize() {
	        btn_analisisEsperaCentral.setOnAction(e -> 
	            cargarGraficaIndependiente("/vistas_personal_medico/graficaCentral_AnalisisEspera.fxml", "ANALISIS"));

	        btn_pacientesRecibidosCentral.setOnAction(e -> 
	            cargarGraficaIndependiente("/vistas_personal_medico/graficaCentral_PacientesRecibidos.fxml", "PACIENTES"));

	        btn_sangreReservadaCentral.setOnAction(e -> 
	            cargarGraficaIndependiente("/vistas_personal_medico/graficaCentral_SangreReservada.fxml", "SANGRE"));
	    }
	    

	    public void cargarGraficaIndependiente(String fxmlFile, String tipo) {
	    	    try {
	    	        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
	    	        Parent vistaGrafica = loader.load(); 

	    	        GraficasController gController = loader.getController();

	    	        if (gController != null) {
	    	        	
		                gController.setGestor(this);
	    	            gController.inicializarNavegacion(tipo);
	    	            
	    	       
	    	            VBox vBoxDetalleInterno = (VBox) vistaGrafica.lookup("#contenedor_detalles_grafica");
	    	            if (vBoxDetalleInterno != null) {
	    	                gController.setContenedorDetalles(vBoxDetalleInterno);
	    	            }

	    	            
	    	            switch (tipo) {
	    	                case "SANGRE"    -> gController.prepararGraficaSangre();
	    	                case "PACIENTES" -> gController.prepararGraficaPacientes();
	    	                case "ANALISIS"  -> gController.prepararGraficaAnalisis();
	    	            }
	    	            

	    	           
	    	            BorderPane layoutInstitucional = new BorderPane();
	    	            
	    	            
	    	            layoutInstitucional.setLeft(gController.menuNavegacionGraficas); 
	    	            
	    	          
	    	            layoutInstitucional.setCenter(vistaGrafica); 

	    	           
	    	            if (principalController != null && principalController.getContenedorPrincipal() != null) {
	    	                principalController.getContenedorPrincipal().setContent(layoutInstitucional);
	    	            }
	    	        }
	    	    } catch (IOException e) {
	    	        System.err.println("Error Crítico de Interfaz: No se pudo reestructurar el módulo gráfico.");
	    	        e.printStackTrace();
	    	    }
	    	}

}

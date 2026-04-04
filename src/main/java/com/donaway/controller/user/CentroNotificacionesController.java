package com.donaway.controller.user;

import com.donaway.model.user.Notificacion;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.geometry.Pos;  

public class CentroNotificacionesController {

    @FXML
    private VBox listaCompleta; 
    @FXML
    private VBox contenidoDetallado;
    
    public void cargardatos(Notificacion inicial, List<Notificacion> todas) {
        listaCompleta.getChildren().clear();
        for (Notificacion obj : todas) {
            Button btn = new Button(obj.getTitulo());
            btn.getStyleClass().add("botones_notificacion");
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.setOnAction(e -> mostrarNoti_detalle(obj));
            listaCompleta.getChildren().add(btn);
        }
        mostrarNoti_detalle(inicial);
    }

    private void mostrarNoti_detalle(Notificacion actual) {
        contenidoDetallado.getChildren().clear();
        
        contenidoDetallado.setFillWidth(true);
        contenidoDetallado.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(contenidoDetallado, Priority.ALWAYS);

        VBox cabecera = new VBox(5);
        cabecera.setAlignment(Pos.CENTER_RIGHT);
        cabecera.setMaxWidth(Double.MAX_VALUE);
        cabecera.setPadding(new Insets(0, 10, 10, 0));

        Label title = new Label(actual.getTitulo());
        title.getStyleClass().add("titulo-notifi");
        
        Label date = new Label("Hace: " + actual.getTiempo()); 
        date.getStyleClass().add("fecha-noti");

        cabecera.getChildren().addAll(title, date);

        Text desc = new Text( "\n\n" + 
            "Detalles institucionales de la solicitud de Donaway...\n" +
            "ID de Transfusión: #12345\n" +
            "Prioridad: ALTA\n" +
            "-------------------------------------------\n" +
            "Este es un registro oficial del sistema."); 
        
        desc.getStyleClass().add("detalle-texto");
        desc.setStyle("-fx-font-size: 16px; -fx-fill: white;");
        
        VBox panelRojo = new VBox(desc); 
        panelRojo.getStyleClass().add("detalle-panel"); 
        panelRojo.setPadding(new Insets(25));
        panelRojo.setMaxWidth(Double.MAX_VALUE);
        panelRojo.setMinHeight(Region.USE_PREF_SIZE); 

        ScrollPane scrollContenido = new ScrollPane(panelRojo);
        scrollContenido.setFitToWidth(true); 
        scrollContenido.setPannable(true);
        scrollContenido.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollContenido.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollContenido.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        
        VBox.setVgrow(scrollContenido, Priority.ALWAYS);

        Separator separador = new Separator();
        separador.setPadding(new Insets(5, 0, 15, 0));
        
        contenidoDetallado.getChildren().addAll(cabecera, separador, scrollContenido);
        
        desc.wrappingWidthProperty().bind(panelRojo.widthProperty().subtract(50));
    }
}
package com.donaway.controller.user;

import com.donaway.model.user.Notificacion;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import java.io.IOException;
import java.util.List;

import org.controlsfx.control.PopOver;

public class MainController {
    
    @FXML 
    private BorderPane panelPrincipal; // ID del FXML principal
    
    @FXML 
    private VBox menu; 

    @FXML
    private Button btn_notificacion;

    @FXML
    private Label lblContador;

    private int notificaciones = 3; 
    private PopOver popupNotificaciones; 

    @FXML 
    public void manejarColapso() {
        boolean yaEstaColapsado = menu.getStyleClass().contains("menu-colapsado");
        double anchoFinal = yaEstaColapsado ? 169 : 80;

        if (yaEstaColapsado) {
            menu.getStyleClass().remove("menu-colapsado");
        }

        Timeline timeline = new Timeline();
        KeyValue ancho = new KeyValue(menu.prefWidthProperty(), anchoFinal);
        KeyFrame duracion = new KeyFrame(Duration.millis(300), ancho);
        timeline.getKeyFrames().add(duracion);

        timeline.setOnFinished(e -> {
            if (anchoFinal == 80) {
                menu.getStyleClass().add("menu-colapsado");
            }
        });

        timeline.play();
    }
    
    @FXML
    private void mostrarNotificacion() {
        ListView<Notificacion> lista = new ListView<>();
        
        lista.getItems().addAll(
            new Notificacion("Nueva solicitud de transfusión", "Hace 2 mins"),
            new Notificacion("Donación aprobada", "Hace 1 hora"),
            new Notificacion("Nueva solicitud de transfusión", "Hace 2 mins"),
            new Notificacion("Donación aprobada", "Hace 1 hora")
        );

        lista.setCellFactory(param -> new ListCell<Notificacion>() {
            protected void updateItem(Notificacion noti, boolean vacio) {
                super.updateItem(noti, vacio);
                if (vacio || noti == null) {
                    setGraphic(null);
                } else {
                    VBox contenedorV = new VBox(5);
                    
                    Label lblTitulo = new Label(noti.getTitulo());
                    lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
                    
                    HBox contenedorH = new HBox();
                    Label lblTiempo = new Label(noti.getTiempo());
                    lblTiempo.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 11px;");
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS); 
                    
                    Hyperlink linkVer = new Hyperlink("Ver notificación completa ^");
                    linkVer.setStyle("-fx-font-size: 11px; -fx-text-fill: #0056b3;");
                    linkVer.setOnAction(e -> {
                        if (popupNotificaciones != null) popupNotificaciones.hide();
                        cargarCentroNotifi(noti, lista.getItems());
                    });

                    contenedorH.getChildren().addAll(lblTiempo, spacer, linkVer);
                    contenedorV.getChildren().addAll(lblTitulo, contenedorH);
                    
                    setGraphic(contenedorV);
                }
            }
        });
        
        lista.getStyleClass().add("lista-notificaciones");
        lista.setFixedCellSize(70);
        lista.prefHeightProperty().bind(
            lista.fixedCellSizeProperty().multiply(Bindings.size(lista.getItems()).add(0.5))
        );
        lista.setMaxHeight(300);

        if (popupNotificaciones != null && popupNotificaciones.isShowing()) {
            popupNotificaciones.hide();
            return;
        }

        popupNotificaciones = new PopOver(lista);
        popupNotificaciones.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popupNotificaciones.show(btn_notificacion);
        
        if (notificaciones > 0) {
            notificaciones--;
            lblContador.setText(String.valueOf(notificaciones));
        }

        if (notificaciones == 0) {
            if (lblContador != null) lblContador.setVisible(false); 
        }
    }
    
    private void cargarCentroNotifi(Notificacion actual, List<Notificacion> todas) {
        try {
            // Ruta absoluta apuntando a la carpeta de resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user/PanelNotificacionesView.fxml"));
            Parent vistaNotificaciones = loader.load();

            CentroNotificacionesController hijo = loader.getController();
            hijo.cargardatos(actual, todas);

            // Inyectar en el centro del BorderPane
            panelPrincipal.setCenter(vistaNotificaciones);
            
        } catch (IOException e) {
            System.out.println("Error crítico cargando la vista de notificaciones:");
            e.printStackTrace();
        }
    }
}
package com.donaway.controller.controllerpersonal_inventario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.beans.binding.Bindings;
import org.controlsfx.control.PopOver;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.donaway.controller.controllerpersonalmedico.ControladorCentroNotificaciones;
import com.donaway.controller.controllerpersonalmedico.ControllerGraficosIndependiente;
import com.donaway.controller.controllerpersonalmedico.ModuloFormularios;
import com.donaway.controller.controllerpersonalmedico.NotificacionObjeto;

public class ControllerInventario implements Initializable {
    
    @FXML private ScrollPane contenedorPrincipal;
    @FXML private VBox menu;
    @FXML private VBox panel_left;
    @FXML private Button btn_notificacion;
    @FXML private Button btn_perfil;
    @FXML private Label lblContador;

    private int notificaciones = 3;
    private PopOver popupNotificaciones;
    private PopOver popupPerfil;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Carga inicial: Solo la tabla, sin bucles.
        cargarModuloEnCentro("/views/vistas_formularios/vistas_inventario/grafica_cant_SangreReservada.fxml");
    }

    public void cargarModuloEnCentro(String rutaFXML) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();
            
            if (vista instanceof Region) {
                ((Region) vista).prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            }
            contenedorPrincipal.setContent(vista);
        } catch (IOException e) {
            System.err.println("Error al cargar módulo: " + rutaFXML);
            e.printStackTrace();
        }
    }

    // --- NAVEGACIÓN ---
    @FXML public void irAInicio() { cargarModuloEnCentro("/views/vistas_formularios/vistas_inventario/vistas_modulos/grafica_cant_Donante.fxml"); }
    @FXML public void irALaboratorio() { cargarModuloEnCentro("/views/vistas_formularios/vistas_inventario/form_unidad_hermoderivados.fxml"); }
    @FXML public void irASolicitud() { cargarModuloEnCentro("/views/vistas_formularios/vistas_inventario/vistas_modulos/form_solicitud.fxml"); }
    @FXML public void irAInspector() {abrirGestiongraficas(); }
    @FXML public void irABusquedas() { cargarModuloEnCentro("/views/vistas_formularios/vistas_inventario/vistas_modulos/form_buscar_solicitud.fxml"); }
    
    
    public void abrirGestiongraficas() {
        List<ModuloFormularios> misModulos = List.of(
            new ModuloFormularios("Historial Donaciones", "/views/vistas_formularios/vistas_inventario/vistas_modulos/grafica_cant_Donante.fxml"),
            new ModuloFormularios("Historial De Almacenamiento", "/views/vistas_formularios/vistas_inventario/vistas_modulos/grafica_cant_volumenSangre.fxml"),
            new ModuloFormularios("Stock Actual", "/views/vistas_formularios/vistas_inventario/grafica_cant_SangreReservada.fxml")
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/panel_modulo.fxml"));
            Parent root = loader.load();

            ControladorCentroNotificaciones controller = loader.getController();
            controller.inicializarNavegacion(misModulos);
            controller.configurarEncabezado("Módulo de Inspeccion & Historial", "/img/img_personal_medico/img_informacion_usuario/grafico.png");

            contenedorPrincipal.setContent(root); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // --- LÓGICA DE INTERFAZ (MENÚS Y POPOVERS) ---
    @FXML
    public void manejarColapso() {
        boolean yaEstaColapsado = menu.getStyleClass().contains("menu-colapsado");
        double anchoFinal = yaEstaColapsado ? 182 : 80;
        if (yaEstaColapsado) menu.getStyleClass().remove("menu-colapsado");
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(menu.prefWidthProperty(), anchoFinal)));
        timeline.setOnFinished(e -> { if (anchoFinal == 80) menu.getStyleClass().add("menu-colapsado"); });
        timeline.play();
    }

    @FXML
    public void manejarColapsoColumna() {
        boolean yaEstaColapsado = panel_left.getStyleClass().contains("columna-colapsada");
        double anchoFinal = yaEstaColapsado ? 217 : 80;
        if (yaEstaColapsado) panel_left.getStyleClass().remove("columna-colapsada");
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(300), new KeyValue(panel_left.prefWidthProperty(), anchoFinal)));
        timeline.setOnFinished(e -> { if (anchoFinal == 80) panel_left.getStyleClass().add("columna-colapsada"); });
        timeline.play();
    }

    @FXML
    private void mostrarNotificacion() {
        ListView<NotificacionObjeto> lista = new ListView<>();
        
        lista.getItems().addAll(
            new NotificacionObjeto("Nueva solicitud de transfusión", "Hace 2 mins"),
            new NotificacionObjeto("Donación aprobada", "Hace 1 hora"),
            new NotificacionObjeto("Nueva solicitud de transfusión", "Hace 2 mins"),
            new NotificacionObjeto("Donación aprobada", "Hace 1 hora")
            //Aqui es que van las funcionalidades del backend
        );

        lista.setCellFactory(param -> new ListCell<NotificacionObjeto>() {
            protected void updateItem(NotificacionObjeto noti, boolean vacio) {
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
        lista.fixedCellSizeProperty().multiply(
        Bindings.size(lista.getItems()).add(0.5)
            )
        );

        lista.setMaxHeight(300);
        if (popupNotificaciones != null && popupNotificaciones.isShowing()) {
	        popupNotificaciones.hide();
	        return;
	    }
        PopOver popOver = new PopOver(lista);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popOver.show(btn_notificacion);
        
        if (notificaciones > 0) {
            notificaciones--;
            lblContador.setText(String.valueOf(notificaciones));
        }

        if (notificaciones == 0) {
            lblContador.setVisible(false); 
        }
    
    }
  
    
    
    @FXML
    private VBox contenidoDetallado;
    
    private void cargarCentroNotifi(NotificacionObjeto actual, List<NotificacionObjeto> todas) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/centroNotificaciones.fxml"));
            Parent vistaDetalle = loader.load(); 

            ControladorCentroNotificaciones hijo = loader.getController();
            
            hijo.cargardatos(actual, todas);

           
            mostrarDetalleConNotificaciones(vistaDetalle, hijo.listaCompleta);

        } catch (IOException e) {
            System.err.println("Error al cargar el centro de notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

   @FXML
    private void mostrarPopOverPerfil() {
        if (popupNotificaciones != null && popupNotificaciones.isShowing()) {
            popupNotificaciones.hide();
            return;
        }
        if (popupPerfil != null && popupPerfil.isShowing()) {
            popupPerfil.hide();
            return;
        }

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(350); 
         root.getStyleClass().add("fondo_descripcion");
        ImageView fotoPerfil = new ImageView(); 
        Circle clip = new Circle(40, 40, 40); 
        
        try {
            Image imagen_usuario = new Image(getClass().getResourceAsStream("/img/img_generales/usuario.png"));
            fotoPerfil.setImage(imagen_usuario);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen desde los recursos.");
        }

        fotoPerfil.setFitWidth(80);
        fotoPerfil.setFitHeight(80);
        fotoPerfil.setPreserveRatio(false);
        fotoPerfil.setClip(clip);

        
        GridPane grid = new GridPane();
        grid.setHgap(10); 
        grid.setVgap(10); 
        grid.setAlignment(Pos.CENTER);
        grid.setStyle("-fx-padding: 15; -fx-background-color: #F9F9F9; -fx-background-radius: 10; -fx-border-color: #DCDCDC; -fx-border-radius: 10;");

        
        añadirFilaGrid(grid, "Nombre:", "Alexa Greilenny Bello Guzmán", 0);
        añadirFilaGrid(grid, "Cargo:", "Bioanalista Principal", 1);
        añadirFilaGrid(grid, "Depto:", "Unidad de Extracción", 2);
        añadirFilaGrid(grid, "ID:", "DW-2026-0404", 3);
        añadirFilaGrid(grid, "Horario:", "08:00 AM - 04:00 PM", 4);
        añadirFilaGrid(grid, "Estado:", "Activo", 5);
        

        // --- BOTÓN DE ACCIÓN ---
        Button btnVerPerfil = new Button("Ver Perfil Completo");
        btnVerPerfil.getStyleClass().add("botones_general");
        btnVerPerfil.setOnAction(e -> {
            if (popupPerfil != null) popupPerfil.hide();
             cargarPanelCentralPerfil(); 
        });
        root.getChildren().addAll(fotoPerfil, grid, btnVerPerfil);

      
        popupPerfil = new PopOver(root);
        popupPerfil.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        popupPerfil.setCornerRadius(10);
        
        if (btn_perfil != null) {
            popupPerfil.show(btn_perfil);
        } else {
            System.out.println("Error: btn_perfil es null. Revisa el fx:id en Scene Builder.");
        }
    }
   
    private void cargarPanelCentralPerfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_formularios/vistas_inventario/vistas_modulos/panelDetallesPerfil.fxml"));
            Parent vista=loader.load();
            ControllerGraficosIndependiente userController = loader.getController();
            userController.setInventarioController(this);

            
            if (vista instanceof BorderPane) {
                ((BorderPane) vista).setLeft(null); 
            }

            contenedorPrincipal.setContent(vista);
            
            if (vista instanceof Region) {
                ((Region) vista).setMaxWidth(Double.MAX_VALUE);
            }

        } catch (IOException e) {
            System.err.println("Error al cargar el panel de perfil: " + e.getMessage());
        }
    
    
    }
    
   

    public void mostrarDetalleConNotificaciones(Parent vistaDetalle, VBox listaBotonesNotificativas) {
       
        listaBotonesNotificativas.setPrefWidth(200); 
        listaBotonesNotificativas.setSpacing(13);
        listaBotonesNotificativas.setPadding(new Insets(15));
        listaBotonesNotificativas.getStyleClass().add("lista-maestra-detalle");
        
        ScrollPane scrollLateral = new ScrollPane(listaBotonesNotificativas);
        scrollLateral.setFitToWidth(true);
        scrollLateral.setPrefWidth(210);
        scrollLateral.setStyle("-fx-background-color: #f0b47bd4; -fx-border-color: #f0b47bd4; -fx-border-width: 0 1 0 0;");

        
        if (vistaDetalle instanceof BorderPane) {
            ((BorderPane) vistaDetalle).setLeft(scrollLateral);
        } else {
            
            BorderPane contenedorEstructural = new BorderPane();
            contenedorEstructural.setCenter(vistaDetalle);
            contenedorEstructural.setLeft(scrollLateral);
            
            
            vistaDetalle = contenedorEstructural;
        }

       
        contenedorPrincipal.setContent(vistaDetalle);
        
        if (vistaDetalle instanceof Region) {
            ((Region) vistaDetalle).setMaxWidth(Double.MAX_VALUE);
        }
    }

private void añadirFilaGrid(GridPane grid, String labelText, String valueText, int fila) {
    Label lblTitulo = new Label(labelText);
   
    lblTitulo.getStyleClass().add("descripcion_breve");
    Label lblValor = new Label(valueText);
    
    lblValor.getStyleClass().add("descripcion_breve_respuesta");

    grid.add(lblTitulo, 0, fila); 
    grid.add(lblValor, 1, fila);  
}}
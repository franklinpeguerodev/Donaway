package com.donaway.controller.controllerpersonalmedico;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.util.Duration;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.donaway.dto.login.SessionManager;
import com.donaway.dto.login.UserSessionDto;

import com.donaway.utils.time.Time;
import org.controlsfx.control.PopOver;

public class ControllerPrincipal implements Initializable {

	@FXML private ScrollPane contenedorPrincipal;
	@FXML private VBox contenedor_menu;
	@FXML private VBox menu;
	@FXML private Label lb_dia_hoy;
	@FXML private Label lb_nombre_hospital;
	@FXML private Label lb_cantidad_citas;
	@FXML private Label lb_cantidad_analisis;
	@FXML private Label lb_cantidad_pacientes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ejecutarCargaModuloInicial();
        
        lb_dia_hoy.setText(Time.dateNow());
        
        
    }
    public ScrollPane getContenedorPrincipal() {
        return contenedorPrincipal;
    }
    private final VistasEmergentes ubicacion = new VistasEmergentes();

    @FXML private Button btn_abrirMapa; 

    @FXML
    private void irAInicio() {
        ejecutarCargaModuloInicial();
        
    }
    @FXML
    private void irAnalisis() {
    	abrirGestionFormularios_analisis();
    }

    @FXML
    private void irALaboratorio() {
    	abrirGestionFormularios_laboratorio();
    }
    @FXML
    private void irABusquedas() {
    	abrirGestionFormularios_busquedas();    
    }
    @FXML
    private void irASolicitud() {
    	abrirGestionFormularios_solicitudes();
    }
    
    public void abrirGestionFormularios_busquedas() {
        List<ModuloFormularios> misModulos = List.of(
            new ModuloFormularios("Historial De Solicitudes", "/views/vistas_formularios/vistas_personal_medico/vistas_solicitud/form_buscar_solicitud.fxml"),
            new ModuloFormularios("Buscar Donante", "/views/vistas_formularios/vistas_personal_medico/vistas_solicitud/form_busqueda_donante.fxml"),
            new ModuloFormularios("Buscar Donación", "/views/vistas_formularios/vistas_personal_medico/vistas_solicitud/form_busqueda_donacion.fxml")
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/panel_modulo.fxml"));
            Parent root = loader.load();

            ControladorCentroNotificaciones controller = loader.getController();
            controller.inicializarNavegacion(misModulos);
            controller.configurarEncabezado("Módulo de Búsqueda E Historial", "/img/img_generales/historial-medico.png");

            contenedorPrincipal.setContent(root); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void abrirGestionFormularios_solicitudes() {
        List<ModuloFormularios> misModulos = List.of(
            new ModuloFormularios("Registro De Solicitud", "/views/vistas_formularios/vistas_personal_medico/vistas_solicitud/form_solicitud.fxml")
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/panel_modulo.fxml"));
            Parent root = loader.load();

            ControladorCentroNotificaciones controller = loader.getController();
            controller.inicializarNavegacion(misModulos);
            controller.configurarEncabezado("Módulo de Solicitudes", "/img/img_generales/icono_solicitud.png");

            contenedorPrincipal.setContent(root); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    public void abrirGestionFormularios_analisis() {
        List<ModuloFormularios> misModulos = List.of(
            new ModuloFormularios("Registro De Extraccion", "/views/vistas_formularios/vistas_personal_medico/vistas_bioanalista/vistas_analisis/registro_extraccion.fxml"),
            new ModuloFormularios("Registro De Resultados", "/views/vistas_formularios/vistas_personal_medico/vistas_bioanalista/vistas_analisis/registro_resultado_lab.fxml")
        );

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/panel_modulo.fxml"));
            Parent root = loader.load();

            ControladorCentroNotificaciones controller = loader.getController();
            controller.inicializarNavegacion(misModulos);
            controller.configurarEncabezado("Módulo de Análisis", "/img/img_personal_medico/img_registros/analisis-de-sangre.png");

            contenedorPrincipal.setContent(root); 

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void abrirGestionFormularios_laboratorio() {
    	 List<ModuloFormularios> misModulos = List.of(
    	            new ModuloFormularios("Registro De Transfusion", "/views/vistas_formularios/vistas_personal_medico/vistas_bioanalista/vistas_laboratorio/form_datos_transfusion.fxml"),
    	            new ModuloFormularios("Registro De Triaje", "/views/vistas_formularios/vistas_personal_medico/vistas_bioanalista/vistas_laboratorio/registro_datos_triaje.fxml")
    	        );

    	        try {
    	            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/panel_modulo.fxml"));
    	            Parent root = loader.load();

    	            ControladorCentroNotificaciones controller = loader.getController();
    	            controller.inicializarNavegacion(misModulos);

    	            controller.configurarEncabezado("Módulo Resultados De Laboratorio", "/img/img_personal_medico/img_registros/resultado.png");
    	            contenedorPrincipal.setContent(root); 

    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    }
    @FXML
    public void manejarColapso() {
        boolean yaEstaColapsado = menu.getStyleClass().contains("menu-colapsado");
        double anchoFinal = yaEstaColapsado ? 182 : 80;

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
    
    public void cargarModulo(String rutaFXML) {
        try {
            Node vista = FXMLLoader.load(getClass().getResource(rutaFXML));
            
            if (vista instanceof Region) {
                ((Region) vista).prefWidthProperty().bind(contenedorPrincipal.widthProperty());
            }
            
            contenedorPrincipal.setContent(vista);
        } catch (IOException e) {
            System.err.println("Error al cargar módulo en: " + rutaFXML);
            e.printStackTrace();
        }
    }
    
    public void ejecutarCargaModuloInicial() {
        try {
            FXMLLoader loaderFormulario = new FXMLLoader(getClass().getResource("/views/vistas_formularios/vistas_personal_medico/panelRegistroDonante.fxml"));
            Parent formulario = loaderFormulario.load();

            Button btnSangre = (Button) formulario.lookup("#btn_sangreReservada_inicio");
            Button btnPacientes = (Button) formulario.lookup("#btn_pacientesRecibidos_inicio");
            Button btnAnalisis = (Button) formulario.lookup("#btn_analisisEspera_inicio");
            Button btnMapa = (Button) formulario.lookup("#btn_abrirMapa");
            if (btnMapa != null) {
                // Le asignamos la acción directamente aquí
                btnMapa.setOnAction(e -> ubicacion.mostrarPopOverUbicacion(btnMapa));
            }
            
            Pane panelGrafica = (Pane) formulario.lookup("#graficas_inicio");

           
            if (panelGrafica != null) {
                if (btnSangre != null) {
                    btnSangre.setOnAction(e -> procesarVisualizacionGrafica("/views/vistas_genericas/graficaSangreReservada_inicio.fxml", "SANGRE", panelGrafica));
                }
                if (btnPacientes != null) {
                    btnPacientes.setOnAction(e -> procesarVisualizacionGrafica("/views/vistas_genericas/graficaPacientesRecibidos_inicio.fxml", "PACIENTES", panelGrafica));
                }
                if (btnAnalisis != null) {
                    btnAnalisis.setOnAction(e -> procesarVisualizacionGrafica("/views/vistas_genericas/graficaAnalisisEspera_inicio.fxml", "ANALISIS", panelGrafica));
                }
            }

            BorderPane layout = new BorderPane();
            layout.setCenter(formulario);

            if (contenedorPrincipal != null) {
                contenedorPrincipal.setContent(layout);
            } else {
                System.err.println("Notificación: Contenedor Principal no detectado.");
            }

        } catch (IOException e) {
            System.err.println("Error en la carga de componentes: " + e.getMessage());
        }
    }


    
    private void procesarVisualizacionGrafica(String fxmlFile, String tipo, Pane panelGrafica) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();

           
            GraficasController controller = loader.getController();

            
            VBox vBoxDetalles = (VBox) panelGrafica.getScene().lookup("#detalles_graficos_inicios");

            if (controller != null && vBoxDetalles != null) {
                
                controller.setContenedorDetalles(vBoxDetalles);

                
                switch (tipo) {
                    case "SANGRE"   -> controller.prepararGraficaSangre();
                    case "PACIENTES" -> controller.prepararGraficaPacientes();
                    case "ANALISIS"  -> controller.prepararGraficaAnalisis();
                }
            }

            
            panelGrafica.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Error Institucional: Fallo al vincular componentes analíticos.");
        }
    }
    

    @FXML
    private Button flecha_columna;
    @FXML
    private VBox panel_left;  

    @FXML 
    public void manejarColapsoColumna() {
        
        boolean yaEstaColapsado = panel_left.getStyleClass().contains("columna-colapsada");
        
        
        double anchoFinal = yaEstaColapsado ? 217 : 80;

        if (yaEstaColapsado) {
            panel_left.getStyleClass().remove("columna-colapsada");
        }

        Timeline timeline = new Timeline();
        KeyValue ancho = new KeyValue(panel_left.prefWidthProperty(), anchoFinal);
        KeyFrame duracion = new KeyFrame(Duration.millis(300), ancho);
        timeline.getKeyFrames().add(duracion);

        timeline.setOnFinished(e -> {
            if (anchoFinal == 80) {
                panel_left.getStyleClass().add("columna-colapsada");
            }
        });

        timeline.play();
    }
    
    
    @FXML
    private Button btn_notificacion;


    @FXML
    private Label lblContador;

    private int notificaciones = 3; // inicial
    private PopOver popupNotificaciones; 

    
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
    private Button btn_perfil; 
    

    private PopOver popupPerfil;
   @FXML
    private void mostrarPopOverPerfil() {
	   UserSessionDto usuario = SessionManager.getInstance().getUsuarioActual()
;        if (popupNotificaciones != null && popupNotificaciones.isShowing()) {
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

        
        añadirFilaGrid(grid, "Nombre:",usuario.nombre().toString(), 0);
        añadirFilaGrid(grid, "Cargo:", usuario.cargo().toString(), 1);
        añadirFilaGrid(grid, "Depto:", usuario.cargo().toString(), 2);
        añadirFilaGrid(grid, "ID:", usuario.carnet().toString(), 3);
        añadirFilaGrid(grid, "Horario:", usuario.jornada().toString(), 4);
        añadirFilaGrid(grid, "Estado:", usuario.estado().toString(), 5);
        

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/vistas_genericas/panelDetallesPerfil.fxml"));
            Parent vista=loader.load();
            ControllerGraficosIndependiente userController = loader.getController();
            userController.setPrincipalController(this);

            
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
}
}

package com.donaway.controller.controllerpersonalmedico;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.geometry.Pos;

public class ControladorCentroNotificaciones {
	 public VBox listaCompleta = new VBox(); 
	    
	    @FXML
	    private Label lbl_titulo_notificacion;
	    @FXML
	    private Label lbl_tiempo_notificacion;
	    @FXML
	    private TextFlow texto_notificaciones;

	    
	    public void cargardatos(NotificacionObjeto inicial, List<NotificacionObjeto> todas) {
	     
	        listaCompleta.getChildren().clear();
	        listaCompleta.setSpacing(10);
	        
	        for (NotificacionObjeto obj : todas) {
	            Button btn = new Button(obj.getTitulo());
	            
	            btn.setMaxWidth(Double.MAX_VALUE); 
	            btn.setAlignment(Pos.CENTER_LEFT);
	            
	            btn.setOnAction(e -> mostrarNoti_detalle(obj));
	            
	            listaCompleta.getChildren().add(btn);
	        }

	     
	        if (inicial != null) {
	            mostrarNoti_detalle(inicial);
	        }
	    }

	    private void mostrarNoti_detalle(NotificacionObjeto actual) {
	        if (texto_notificaciones == null) return;

	        texto_notificaciones.getChildren().clear();
	        lbl_titulo_notificacion.setText(actual.getTitulo());
	        lbl_tiempo_notificacion.setText("Hace: " + actual.getTiempo());

	        Text desc = new Text(
	            "REPORTE INSTITUCIONAL DONAWAY\n" +
	            "-------------------------------------------\n" +
	            "ID de Transfusión: #12345\n" +
	            "Prioridad: ALTA\n" +
	            "Estado: Pendiente de Revisión\n" +
	            "-------------------------------------------\n" +
	            "Este mensaje ha sido generado automáticamente por el sistema."
	        );
	        
	        desc.getStyleClass().add("detalles_text");
	        texto_notificaciones.getChildren().add(desc);
	    }
	    @FXML
	    private Pane contenedor_formularios; 
	     @FXML
	     private VBox contenedor_listaModulo;

	    public void inicializarNavegacion(List<ModuloFormularios> modulos) {
	        contenedor_listaModulo.getChildren().clear();

	        for (ModuloFormularios modulo : modulos) {
	            
	            Button btnModulo = new Button(modulo.getNombreFormulario());
	            btnModulo.setMaxWidth(Double.MAX_VALUE);
	            btnModulo.getStyleClass().add("botones_general"); 

	            
	            btnModulo.setOnAction(e -> cargarFormularioEnCentro(modulo.getRutaFXML()));

	            contenedor_listaModulo.getChildren().add(btnModulo);
	        }

	        if (!modulos.isEmpty()) {
	            cargarFormularioEnCentro(modulos.get(0).getRutaFXML());
	        }
	    }

	    private void cargarFormularioEnCentro(String rutaFXML) {
	        try {
	            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
	            Parent vista = loader.load();
	           
	            contenedor_formularios.getChildren().setAll(vista);
	            
	        } catch (IOException e) {
	            System.err.println("Error Institucional: No se pudo cargar el formulario en " + rutaFXML);
	            e.printStackTrace();
	        }
	    }
	    @FXML
	    private Label lblTituloModulo;
	    @FXML
	    private ImageView imgIconoModulo;

	    public void configurarEncabezado(String titulo, String rutaImagen) {
	        lblTituloModulo.setText("                " + titulo);
	        try {
	            Image icono = new Image(getClass().getResourceAsStream(rutaImagen));
	            imgIconoModulo.setImage(icono);
	        } catch (Exception e) {
	            System.err.println("Error Institucional: No se pudo cargar el icono: " + rutaImagen);
	        }
	    }
	}
	



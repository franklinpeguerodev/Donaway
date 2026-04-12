package com.donaway.controller.controllerpersonalmedico;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GraficasController {
	private VBox contenedorDetalles;
    private VBox contenedor_detalles_grafica;
    private ControllerGraficosIndependiente gestor; 

    public void setGestor(ControllerGraficosIndependiente gestor) {
        this.gestor = gestor;
    }

    public void setContenedorDetalles(VBox contenedor) {
        this.contenedorDetalles = contenedor;
    }
    
    public void setContenedorDetalleGeneral(VBox contenedor) {
        this.contenedor_detalles_grafica = contenedor;
    }
    

    public void actualizarDetalles(String titulo, String info) {
        if (contenedorDetalles != null) {
            contenedorDetalles.getChildren().clear();
            
            Label lblT = new Label(titulo.toUpperCase());
            lblT.setStyle("-fx-font-weight: bold; -fx-font-family:'Segoe UI Black'; -fx-text-fill: #460606; -fx-font-size: 13px;");
            
            Label lblI = new Label(info);
            lblI.setStyle("-fx-text-fill: #2c3e50; -fx-font-family: 'Segoe UI';");
            
            contenedorDetalles.getChildren().addAll(lblT, lblI);
        } else {
            System.out.println("Aviso: Contenedor de detalles no referenciado para: " + titulo);
        }
    }

    @FXML private AreaChart<String, Number> grafica_sangreReservada;
    @FXML private BarChart<String, Number> grafica_pacientesRecibidos;
    @FXML private PieChart grafica_AnalisisEspera;
    public VBox menuNavegacionGraficas = new VBox();
    
  

    public void inicializarNavegacion(String tipoActual) {
        menuNavegacionGraficas.getChildren().clear();
        menuNavegacionGraficas.setSpacing(12);
        menuNavegacionGraficas.setPrefWidth(155);
        menuNavegacionGraficas.setAlignment(Pos.TOP_CENTER);
        menuNavegacionGraficas.getStyleClass().add("menu_navegacion");

            MenuGraficasObjeto[] btn_navegacion = {
            new MenuGraficasObjeto("Análisis en Espera", "ANALISIS"),
            new MenuGraficasObjeto("Pacientes Recibidos", "PACIENTES"),
            new MenuGraficasObjeto("Sangre Reservada", "SANGRE")
        };

        for (MenuGraficasObjeto rep : btn_navegacion) {
            Button btn = new Button(rep.getTitulo().toUpperCase());
            btn.setMaxWidth(Double.MAX_VALUE);
            btn.getStyleClass().add("botones_general");
            
            
            if (rep.getTipo().equals(tipoActual)) {
                btn.setStyle("-fx-background-color: #460606; -fx-text-fill: white; -fx-font-weight: bold;");
            }

            btn.setOnAction(e -> {
                // Determinamos qué archivo FXML corresponde
            	String fxmlDestino = switch (rep.getTipo()) {
                case "SANGRE" -> "/views/vistas_genericas/graficaCentral_SangreReservada.fxml";
                case "PACIENTES" -> "/views/vistas_genericas/graficaCentral_PacientesRecibidos.fxml";
                case "ANALISIS" -> "/views/vistas_genericas/graficaCentral_AnalisisEspera.fxml";
                default -> null;
            };

                // LE PEDIMOS AL GESTOR QUE RECARGUE TODO EL BORDERPANE
                if (gestor != null) {
                    gestor.cargarGraficaIndependiente(fxmlDestino, rep.getTipo());
                }
            });

            menuNavegacionGraficas.getChildren().add(btn);
        }
        }
            
    

    private void cambiarContenidoGrafica(String tipo) {
        // Limpiamos datos previos para evitar superposición
        if(grafica_sangreReservada != null) grafica_sangreReservada.getData().clear();
        if(grafica_pacientesRecibidos != null) grafica_pacientesRecibidos.getData().clear();
        
        switch (tipo) {
            case "SANGRE"   -> prepararGraficaSangre();
            case "PACIENTES" -> prepararGraficaPacientes();
            case "ANALISIS"  -> prepararGraficaAnalisis();
        }
    }

    public void prepararGraficaSangre() {
        if (grafica_sangreReservada != null) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.getData().add(new XYChart.Data<>("A+", 45));
            serie.getData().add(new XYChart.Data<>("A-", 12));
            serie.getData().add(new XYChart.Data<>("O+", 65));
            serie.getData().add(new XYChart.Data<>("O-", 20));
            grafica_sangreReservada.getData().add(serie);
            actualizarDetalles("Unidades de Sangre Reservadas", "Reporte de inventario actual.");
        }
    }

    public void prepararGraficaPacientes() {
        if (grafica_pacientesRecibidos != null) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.getData().add(new XYChart.Data<>("Lun", 15));
            serie.getData().add(new XYChart.Data<>("Mar", 22));
            serie.getData().add(new XYChart.Data<>("Mie", 18));
            serie.getData().add(new XYChart.Data<>("Jue", 25));
            serie.getData().add(new XYChart.Data<>("Vie", 30));
            grafica_pacientesRecibidos.getData().add(serie);
            actualizarDetalles("Flujo de Pacientes", "Promedio semanal: 22 personas.");
        }
    }

    public void prepararGraficaAnalisis() {
        if (grafica_AnalisisEspera != null) {
            ObservableList<PieChart.Data> datos = FXCollections.observableArrayList(
                new PieChart.Data("Serología", 15),
                new PieChart.Data("Hemoglobina", 8),
                new PieChart.Data("Tipificación", 20),
                new PieChart.Data("Cribado", 12)
            );
            grafica_AnalisisEspera.setData(datos);
            // Mostrar datos sobre la gráfica
            grafica_AnalisisEspera.getData().forEach(d -> d.setName(d.getName() + ": " + (int)d.getPieValue()));
            grafica_AnalisisEspera.setLabelsVisible(true);
            grafica_AnalisisEspera.setLegendVisible(false);
            actualizarDetalles("Análisis en Espera", "Analisis en procesamiento: 45");
        }
    }

        
    }



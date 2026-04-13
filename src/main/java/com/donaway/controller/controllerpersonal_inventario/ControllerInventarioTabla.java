package com.donaway.controller.controllerpersonal_inventario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerInventarioTabla implements Initializable {

    @FXML private GridPane gridInventario;
    @FXML private PieChart grafica_sangre_inventario;
    
  
    @FXML private AreaChart<String, Number> grafica_volumenSangre;
    @FXML private BarChart<String, Number> grafica_donaciones;
    @FXML private VBox vBoxDetalles; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Carga de datos principales
    	
        ObservableList<InventarioUnidad> datos = obtenerDatosInventario();
        
        // Si esto no se ejecuta, la tabla nunca tendrá datos
        if (datos != null && !datos.isEmpty()) {
            cargarDatosEnGrid(datos);
        } else {
            System.out.println("La lista de datos está vacía.");
        }
        ObservableList<InventarioUnidad> datosInventario = obtenerDatosInventario();
        ObservableList<RegistroPacientes> datosPacientes = obtenerDatosPacientes();
        
        // 2. Distribuir a los componentes (Tabla y Gráficas)
        cargarDatosEnGrid(datosInventario);
        prepararGraficaInventario_sangreReservada(datosInventario);
        prepararGraficaSangre(datosInventario); 
        prepararGraficaPacientes(datosPacientes);
    }

     // --- FUENTES DE DATOS ---

        private ObservableList<RegistroPacientes> obtenerDatosPacientes() {
            return FXCollections.observableArrayList(
                new RegistroPacientes("Lun", 15),
                new RegistroPacientes("Mar", 22),
                new RegistroPacientes("Mie", 18),
                new RegistroPacientes("Jue", 25),
                new RegistroPacientes("Vie", 30)
            );
        }

        // --- LÓGICA DE CARGA DE GRÁFICAS ---

        public void prepararGraficaSangre(ObservableList<InventarioUnidad> datos) {
            if (grafica_volumenSangre != null) {
            	grafica_volumenSangre.getData().clear();
                XYChart.Series<String, Number> serie = new XYChart.Series<>();
                serie.setName("Unidades en Inventario");

                // Mapeamos los datos de InventarioUnidad a la gráfica de barras
                for (InventarioUnidad unidad : datos) {
                    serie.getData().add(new XYChart.Data<>(unidad.getTipoSangre() + " (" + unidad.getTipo() + ")", unidad.getCantidad()));
                }

                grafica_volumenSangre.getData().add(serie);
                actualizarDetalles("Inventario por Tipo", "Reporte actualizado de unidades críticas.");
            }
        }

        public void prepararGraficaPacientes(ObservableList<RegistroPacientes> datos) {
            if (grafica_donaciones != null) {
                // 1. Limpieza total de datos anteriores
                grafica_donaciones.getData().clear();
                
                // 2. IMPORTANTE: Limpiar las categorías del eje X para que no se solapen
                if (grafica_donaciones.getXAxis() instanceof CategoryAxis) {
                    ((CategoryAxis) grafica_donaciones.getXAxis()).getCategories().clear();
                }

                XYChart.Series<String, Number> seriePacientes = new XYChart.Series<>();
                // Ponemos un nombre dinámico para confirmar que se está cargando esta versión
                seriePacientes.setName("Pacientes Recibidos (Semanal)");

                for (RegistroPacientes registro : datos) {
                    // Usamos los getters que ya confirmamos que funcionan
                    seriePacientes.getData().add(new XYChart.Data<>(registro.getDia(), registro.getCantidad()));
                }

                // 3. Añadir la serie y desactivar animaciones para ver el cambio de golpe
                grafica_donaciones.setAnimated(false); 
                grafica_donaciones.getData().add(seriePacientes);
                
                System.out.println("DEBUG: Se han cargado " + datos.size() + " días en la gráfica de pacientes.");
                actualizarDetalles("Flujo Semanal", "Análisis de concurrencia en donaciones.");
            } else {
                System.err.println("FALLO CRÍTICO: El ID 'grafica_donaciones' no existe en este FXML.");
            }
        
        }

    private void actualizarDetalles(String titulo, String descripcion) {
        if (vBoxDetalles != null) {
            Label lblTitulo = new Label(titulo);
            lblTitulo.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            Label lblDesc = new Label(descripcion);
            lblDesc.setStyle("-fx-text-fill: #555555;");
            
            vBoxDetalles.getChildren().addAll(lblTitulo, lblDesc);
        }
    }

    // --- GESTIÓN DE TABLA E INVENTARIO ---

    private ObservableList<InventarioUnidad> obtenerDatosInventario() {
        return FXCollections.observableArrayList(
            new InventarioUnidad("Plasma Fresco", "DON-9921", 450, "A+", "PM-001", "12/04/2026", "12/05/2026"),
            new InventarioUnidad("Glóbulos Rojos", "DON-8842", 300, "O-", "PM-005", "10/04/2026", "20/05/2026"),
            new InventarioUnidad("Plaquetas", "DON-7753", 150, "B+", "PM-002", "13/04/2026", "18/04/2026")
        );
    }

    private void cargarDatosEnGrid(ObservableList<InventarioUnidad> datos) {
        if (gridInventario != null) {
            gridInventario.getChildren().clear();
            String[] titulos = {"HEMODERIVADO", "REFERENCIA", "CANTIDAD", "TIPO SANGRE", "ID PERSONAL", "REGISTRO", "EXPIRACIÓN"};
            for (int i = 0; i < titulos.length; i++) {
                gridInventario.add(crearCeldaEncabezado(titulos[i]), i, 0);
            }
            int fila = 1; 
            for (InventarioUnidad u : datos) {
                gridInventario.add(crearCelda(u.getTipo()), 0, fila);
                gridInventario.add(crearCelda(u.getCodigo()), 1, fila);
                gridInventario.add(crearCelda(u.getCantidad() + " ml"), 2, fila);
                gridInventario.add(crearCelda(u.getTipoSangre()), 3, fila);
                gridInventario.add(crearCelda(u.getIdPersonal()), 4, fila);
                gridInventario.add(crearCelda(u.getFechaRegistro()), 5, fila);
                gridInventario.add(crearCelda(u.getFechaExpiracion()), 6, fila);
                fila++;
            }
        }
    }

    private HBox crearCelda(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-padding: 10;");
        return new HBox(label);
    }

    private HBox crearCeldaEncabezado(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-weight: bold; -fx-padding: 10; -fx-background-color: #f4f4f4;");
        return new HBox(label);
    }

    public void prepararGraficaInventario_sangreReservada(ObservableList<InventarioUnidad> datos) {
        if (grafica_sangre_inventario != null) {
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (InventarioUnidad unidad : datos) {
                pieData.add(new PieChart.Data(unidad.getTipo(), unidad.getCantidad()));
            }
            grafica_sangre_inventario.setData(pieData);
        }
    }
}







/*package com.donaway.controller.controllerpersonal_inventario;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerInventarioTabla implements Initializable {

    @FXML private GridPane gridInventario;
    @FXML private PieChart grafica_sangre_inventario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Tarea única: Cargar datos visuales
        ObservableList<InventarioUnidad> datos = obtenerDatosInventario();
        cargarDatosEnGrid(datos);
        prepararGraficaInventario_sangreReservada(datos);
    }

    private ObservableList<InventarioUnidad> obtenerDatosInventario() {
        return FXCollections.observableArrayList(
            new InventarioUnidad("Plasma Fresco", "DON-9921", 450, "A+", "PM-001", "12/04/2026", "12/05/2026"),
            new InventarioUnidad("Glóbulos Rojos", "DON-8842", 300, "O-", "PM-005", "10/04/2026", "20/05/2026"),
            new InventarioUnidad("Plaquetas", "DON-7753", 150, "B+", "PM-002", "13/04/2026", "18/04/2026")
        );
    }

    private void cargarDatosEnGrid(ObservableList<InventarioUnidad> datos) {
        if (gridInventario != null) {
            gridInventario.getChildren().clear();
            String[] titulos = {"HEMODERIVADO", "REFERENCIA", "CANTIDAD", "TIPO SANGRE", "ID PERSONAL", "REGISTRO", "EXPIRACIÓN"};
            for (int i = 0; i < titulos.length; i++) {
                gridInventario.add(crearCeldaEncabezado(titulos[i]), i, 0);
            }
            int fila = 1; 
            for (InventarioUnidad u : datos) {
                gridInventario.add(crearCelda(u.getTipo()), 0, fila);
                gridInventario.add(crearCelda(u.getCodigo()), 1, fila);
                gridInventario.add(crearCelda(u.getCantidad() + " ml"), 2, fila);
                gridInventario.add(crearCelda(u.getTipoSangre()), 3, fila);
                gridInventario.add(crearCelda(u.getIdPersonal()), 4, fila);
                gridInventario.add(crearCelda(u.getFechaRegistro()), 5, fila);
                gridInventario.add(crearCelda(u.getFechaExpiracion()), 6, fila);
                fila++;
            }
        }
    }

    private HBox crearCelda(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-padding: 10;");
        return new HBox(label);
    }

    private HBox crearCeldaEncabezado(String texto) {
        Label label = new Label(texto);
        label.setStyle("-fx-font-weight: bold; -fx-padding: 10; -fx-background-color: #f4f4f4;");
        return new HBox(label);
    }

    public void prepararGraficaInventario_sangreReservada(ObservableList<InventarioUnidad> datos) {
        if (grafica_sangre_inventario != null) {
            ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
            for (InventarioUnidad unidad : datos) {
                pieData.add(new PieChart.Data(unidad.getTipo(), unidad.getCantidad()));
            }
            grafica_sangre_inventario.setData(pieData);
        }
    }
}
*/
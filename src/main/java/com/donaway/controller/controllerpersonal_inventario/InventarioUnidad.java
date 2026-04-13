package com.donaway.controller.controllerpersonal_inventario;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InventarioUnidad {

	private final SimpleStringProperty tipo;
    private final SimpleStringProperty codigo;
    private final SimpleIntegerProperty cantidad;
    private final SimpleStringProperty tipoSangre; // Nuevo
    private final SimpleStringProperty idPersonal; // Nuevo
    private final SimpleStringProperty fechaRegistro; // Nuevo
    private final SimpleStringProperty fechaExpiracion; // Nuevo

    public InventarioUnidad(String tipo, String codigo, int cantidad, String tipoSangre, String idPersonal, String fechaReg, String fechaExp) {
        this.tipo = new SimpleStringProperty(tipo);
        this.codigo = new SimpleStringProperty(codigo);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.tipoSangre = new SimpleStringProperty(tipoSangre);
        this.idPersonal = new SimpleStringProperty(idPersonal);
        this.fechaRegistro = new SimpleStringProperty(fechaReg);
        this.fechaExpiracion = new SimpleStringProperty(fechaExp);
    }

    // Getters
    public String getTipoSangre() { return tipoSangre.get(); }
    public String getIdPersonal() { return idPersonal.get(); }
    public String getFechaRegistro() { return fechaRegistro.get(); }
    public String getFechaExpiracion() { return fechaExpiracion.get(); }

	   
	    public String getTipo() { return tipo.get(); }
	    public String getCodigo() { return codigo.get(); }
	    public int getCantidad() { return cantidad.get(); }

}

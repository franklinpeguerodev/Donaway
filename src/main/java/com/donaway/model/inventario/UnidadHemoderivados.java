package com.donaway.model.inventario;

import java.time.LocalDate;

public class UnidadHemoderivados {

	private int idHemoderivados;
	private String codigoBarras;
	private int idDonacion;
	private String rnc;
	private enum tipoComponente {GLOBULOS_ROJOS, PLAQUETAS, PLASMA}
	private enum estadoUnidad {DISPONIBLE, RESERVADA, DESCARTADA, ESPERA, USADA}
	private LocalDate fechaCaducidad;
	
	
	//constructores
	public UnidadHemoderivados(int idHemoderivados, String codigoBarras, int idDonacion, String rnc,
			LocalDate fechaCaducidad) {
		super();
		this.idHemoderivados = idHemoderivados;
		this.codigoBarras = codigoBarras;
		this.idDonacion = idDonacion;
		this.rnc = rnc;
		this.fechaCaducidad = fechaCaducidad;
	}
	public UnidadHemoderivados() {
		super();
	}
	
	
	//getters and setters
	public int getIdHemoderivados() {
		return idHemoderivados;
	}
	public void setIdHemoderivados(int idHemoderivados) {
		this.idHemoderivados = idHemoderivados;
	}
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public int getIdDonacion() {
		return idDonacion;
	}
	public void setIdDonacion(int idDonacion) {
		this.idDonacion = idDonacion;
	}
	public String getRnc() {
		return rnc;
	}
	public void setRnc(String rnc) {
		this.rnc = rnc;
	}
	public LocalDate getFechaCaducidad() {
		return fechaCaducidad;
	}
	public void setFechaCaducidad(LocalDate fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}
	

	
}

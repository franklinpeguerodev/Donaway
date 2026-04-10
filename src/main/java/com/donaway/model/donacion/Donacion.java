package com.donaway.model.donacion;

import java.time.LocalDateTime;

public class Donacion {

	private int idDonacion;
	private String documentIdDonante;
	private String documentIdReceptor;
	private String documentIdPersonal;
	private String rnc;
	private LocalDateTime fechaExtracion;
	private enum tipoDonacion {SANGRE_TOTAL, AFERESIS}
	private enum tipoComponente {GLOBULOS_ROJOS, PLAQUETAS, PLASMA}
	private float cantidadPinta;
	
	
	//constructores 
	public Donacion(int idDonacion, String cedulaDonante, String cedulaReceptor, String cedulaPersonal, String rnc,
		LocalDateTime fechaExtracion, float cantidadPinta) {
		
		this.idDonacion = idDonacion;
		this.documentIdDonante = cedulaDonante;
		this.documentIdReceptor = cedulaReceptor;
		this.documentIdPersonal = cedulaPersonal;
		this.rnc = rnc;
		this.fechaExtracion = fechaExtracion;
		this.cantidadPinta = cantidadPinta;
	}

	public Donacion() {
		
	}

	//getters and setters
	
	public int getIdDonacion() {
		return idDonacion;
	}

	public void setIdDonacion(int idDonacion) {
		this.idDonacion = idDonacion;
	}

	public String getCedulaDonante() {
		return documentIdDonante;
	}

	public void setCedulaDonante(String cedulaDonante) {
		this.documentIdDonante = cedulaDonante;
	}

	public String getCedulaReceptor() {
		return documentIdReceptor;
	}

	public void setCedulaReceptor(String cedulaReceptor) {
		this.documentIdReceptor = cedulaReceptor;
	}

	public String getCedulaPersonal() {
		return documentIdPersonal;
	}

	public void setCedulaPersonal(String cedulaPersonal) {
		this.documentIdPersonal = cedulaPersonal;
	}

	public String getRnc() {
		return rnc;
	}

	public void setRnc(String rnc) {
		this.rnc = rnc;
	}

	public LocalDateTime getFechaExtracion() {
		return fechaExtracion;
	}

	public void setFechaExtracion(LocalDateTime fechaExtracion) {
		this.fechaExtracion = fechaExtracion;
	}

	public float getCantidadPinta() {
		return cantidadPinta;
	}

	public void setCantidadPinta(float cantidadPinta) {
		this.cantidadPinta = cantidadPinta;
	}
	
	
}

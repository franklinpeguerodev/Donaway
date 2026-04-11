package com.donaway.model.donacion;

import java.time.LocalDateTime;

import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoDonancion;

public class Donacion {

	private String idDonacion;
	private String codigoReferencia;
	private String documentIdDonante;
	private String documentIdPersonal;
	private String rnc;
	private LocalDateTime fechaExtracion;
	private TipoDonancion tipoDonacionObj;
	private TipoComponente tipoComponenteObj;
	private float cantidadPinta;
	
	
	//constructores 
	public Donacion(String idDonacion, String codigoReferencia, String cedulaDonante, String cedulaReceptor, String cedulaPersonal, String rnc,
		LocalDateTime fechaExtracion,TipoDonancion tipoDonacionObj, TipoComponente tipoComponenteObj, float cantidadPinta) {
		
		this.idDonacion = idDonacion;
		this.codigoReferencia = codigoReferencia;
		this.documentIdDonante = cedulaDonante;
		this.documentIdPersonal = cedulaPersonal;
		this.rnc = rnc;
		this.fechaExtracion = fechaExtracion;
		this.tipoDonacionObj = tipoDonacionObj;
		this.tipoComponenteObj = tipoComponenteObj;
		this.cantidadPinta = cantidadPinta;
	}

	public Donacion() {
		
	}

	
	//getters and setters
	
	public String getCodigoReferencia() {
		return codigoReferencia;
	}

	public void setCodigoReferencia(String codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}

	public String getIdDonacion() {
		return idDonacion;
	}

	public void setIdDonacion(String idDonacion) {
		this.idDonacion = idDonacion;
	}

	public String getDocumentIdDonante() {
		return documentIdDonante;
	}

	public void setDocumentIdDonante(String documentIdDonante) {
		this.documentIdDonante = documentIdDonante;
	}

	public String getDocumentIdPersonal() {
		return documentIdPersonal;
	}

	public void setDocumentIdPersonal(String documentIdPersonal) {
		this.documentIdPersonal = documentIdPersonal;
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

	public TipoDonancion getTipoDonacionObj() {
		return tipoDonacionObj;
	}

	public void setTipoDonacionObj(TipoDonancion tipoDonacionObj) {
		this.tipoDonacionObj = tipoDonacionObj;
	}

	public TipoComponente getTipoComponenteObj() {
		return tipoComponenteObj;
	}

	public void setTipoComponenteObj(TipoComponente tipoComponenteObj) {
		this.tipoComponenteObj = tipoComponenteObj;
	}

	public float getCantidadPinta() {
		return cantidadPinta;
	}

	public void setCantidadPinta(float cantidadPinta) {
		this.cantidadPinta = cantidadPinta;
	}
	
	
}

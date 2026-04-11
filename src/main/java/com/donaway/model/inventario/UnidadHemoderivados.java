package com.donaway.model.inventario;

import java.time.LocalDate;

import com.donaway.utils.constants.EstadoUnidad;
import com.donaway.utils.constants.TipoComponente;

public class UnidadHemoderivados {

	private String idHemoderivados;
	private String codigoBarras;
	private String idDonacion;
	private String rnc;
	private String codigoReferencia;
	private TipoComponente tipoComponenteObj;
	private EstadoUnidad estadoUnidadObj;
	private LocalDate fechaCaducidad;
	
	
	//constructores
	public UnidadHemoderivados(String idHemoderivados, String codigoBarras, String idDonacion, String rnc, String codigoReferencia,
			TipoComponente tipoComponenteObj, EstadoUnidad estadoUnidadObj, LocalDate fechaCaducidad) {
		super();
		this.idHemoderivados = idHemoderivados;
		this.codigoBarras = codigoBarras;
		this.idDonacion = idDonacion;
		this.rnc = rnc;
		this.codigoReferencia = codigoReferencia;
		this.tipoComponenteObj = tipoComponenteObj;
		this.estadoUnidadObj = estadoUnidadObj;
		this.fechaCaducidad = fechaCaducidad;
	}
	public UnidadHemoderivados() {
		super();
	}
	
	
	//getters and setters
	public String getIdHemoderivados() {
		return idHemoderivados;
	}
	public void setIdHemoderivados(String idHemoderivados) {
		this.idHemoderivados = idHemoderivados;
	}
	public String getCodigoBarras() {
		return codigoBarras;
	}
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	public String getIdDonacion() {
		return idDonacion;
	}
	public void setIdDonacion(String idDonacion) {
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
	public String getCodigoReferencia() {
		return codigoReferencia;
	}
	public void setCodigoReferencia(String codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}
	public TipoComponente getTipoComponenteObj() {
		return tipoComponenteObj;
	}
	public void setTipoComponenteObj(TipoComponente tipoComponenteObj) {
		this.tipoComponenteObj = tipoComponenteObj;
	}
	public EstadoUnidad getEstadoUnidadObj() {
		return estadoUnidadObj;
	}
	public void setEstadoUnidadObj(EstadoUnidad estadoUnidadObj) {
		this.estadoUnidadObj = estadoUnidadObj;
	}
	

	
}

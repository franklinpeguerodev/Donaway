package com.donaway.model.transfusion;

import java.time.LocalDateTime;

import com.donaway.utils.constants.EstadoSolicitud;
import com.donaway.utils.constants.PrioridadSolicitud;
import com.donaway.utils.constants.TipoComponente;

public class SolicitudTransfusion {

	private String idSolicitud;
	private String documentIdReceptor;
	private String rnc;
	private String codigoReferencia;
	private LocalDateTime fechaSolicitud;
	private EstadoSolicitud estadoSolicitudObj;
	private PrioridadSolicitud prioridadObj;
	private TipoComponente componentes;
	private LocalDateTime fechaEntrega;
	

	
	//Contructores
	public SolicitudTransfusion(String idSolicitud, String documentIdReceptor, String rnc, String codigoReferencia, LocalDateTime fechaSolicitud,
		EstadoSolicitud estadoSolicitud, PrioridadSolicitud prioridad, TipoComponente componentes, LocalDateTime fechaEntrega) {
		super();
		this.idSolicitud = idSolicitud;
		this.documentIdReceptor = documentIdReceptor;
		this.rnc = rnc;
		this.codigoReferencia = codigoReferencia;
		this.fechaSolicitud = fechaSolicitud;
		this.estadoSolicitudObj = estadoSolicitud;
		this.prioridadObj = prioridad;
		this.componentes = componentes;
		this.fechaEntrega = fechaEntrega;
	}


	public SolicitudTransfusion() {
		super();
	}


	//getters and setters
	
	public String getIdSolicitud() {
		return idSolicitud;
	}


	public void setIdSolicitud(String idSolicitud) {
		this.idSolicitud = idSolicitud;
	}


	public String getDocumentIdReceptor() {
		return documentIdReceptor;
	}


	public void setDocumentIdReceptor(String documentIdReceptor) {
		this.documentIdReceptor = documentIdReceptor;
	}


	public String getRnc() {
		return rnc;
	}


	public void setRnc(String rnc) {
		this.rnc = rnc;
	}


	public LocalDateTime getFechaSolicitud() {
		return fechaSolicitud;
	}


	public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}


	public LocalDateTime getFechaEntrega() {
		return fechaEntrega;
	}


	public void setFechaEntrega(LocalDateTime fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}


	public String getCodigoReferencia() {
		return codigoReferencia;
	}


	public void setCodigoReferencia(String codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}


	public EstadoSolicitud getEstadoSolicitud() {
		return estadoSolicitudObj;
	}


	public void setEstadoSolicitud(EstadoSolicitud estadoSolicitud) {
		this.estadoSolicitudObj = estadoSolicitud;
	}


	public PrioridadSolicitud getPrioridad() {
		return prioridadObj;
	}


	public void setPrioridad(PrioridadSolicitud prioridad) {
		this.prioridadObj = prioridad;
	}


	public EstadoSolicitud getEstadoSolicitudObj() {
		return estadoSolicitudObj;
	}


	public void setEstadoSolicitudObj(EstadoSolicitud estadoSolicitudObj) {
		this.estadoSolicitudObj = estadoSolicitudObj;
	}


	public PrioridadSolicitud getPrioridadObj() {
		return prioridadObj;
	}


	public void setPrioridadObj(PrioridadSolicitud prioridadObj) {
		this.prioridadObj = prioridadObj;
	}


	public TipoComponente getComponentes() {
		return componentes;
	}


	public void setComponentes(TipoComponente componentes) {
		this.componentes = componentes;
	}
	
	
	
	
	
}

package com.donaway.model.transfusion;

import java.time.LocalDateTime;

public class SolicitudTransfusion {

	private int idSolicitud;
	private String documentIdReceptor;
	private String rnc;
	private LocalDateTime fechaSolicitud;
	private LocalDateTime fechaEntrega;
	private enum estadoSolicitud {PENDIENTE, EN_CAMINO, ENTREGADA}
	private enum prioridad {EMERGENCIA, URGENTE, RUTINA}

	
	//Contructores
	public SolicitudTransfusion(int idSolicitud, String documentIdReceptor, String rnc, LocalDateTime fechaSolicitud,
			LocalDateTime fechaEntrega) {
		super();
		this.idSolicitud = idSolicitud;
		this.documentIdReceptor = documentIdReceptor;
		this.rnc = rnc;
		this.fechaSolicitud = fechaSolicitud;
		this.fechaEntrega = fechaEntrega;
	}


	public SolicitudTransfusion() {
		super();
	}


	//getters and setters
	
	public int getIdSolicitud() {
		return idSolicitud;
	}


	public void setIdSolicitud(int idSolicitud) {
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
	
	
	
	
	
}

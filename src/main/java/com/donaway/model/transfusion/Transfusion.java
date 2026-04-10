package com.donaway.model.transfusion;

import java.time.LocalDateTime;

public class Transfusion {

	private int idTransfusion;
	private int idSolicitud;
	private String rnc;
	private String codigoBarras;
	private enum tipoTransfusion {DIRECTA, AUTOLOGA, ALOGENETICA}
	private LocalDateTime fechaTransfusion;
	
	//constructores
	public Transfusion(int idTransfusion, int idSolicitud, String rnc, String codigoBarras,
			LocalDateTime fechaTransfusion) {
		super();
		this.idTransfusion = idTransfusion;
		this.idSolicitud = idSolicitud;
		this.rnc = rnc;
		this.codigoBarras = codigoBarras;
		this.fechaTransfusion = fechaTransfusion;
	}

	public Transfusion() {
		super();
	}
	
	//getters and setters

	public int getIdTransfusion() {
		return idTransfusion;
	}

	public void setIdTransfusion(int idTransfusion) {
		this.idTransfusion = idTransfusion;
	}

	public int getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(int idSolicitud) {
		this.idSolicitud = idSolicitud;
	}

	public String getRnc() {
		return rnc;
	}

	public void setRnc(String rnc) {
		this.rnc = rnc;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public LocalDateTime getFechaTransfusion() {
		return fechaTransfusion;
	}

	public void setFechaTransfusion(LocalDateTime fechaTransfusion) {
		this.fechaTransfusion = fechaTransfusion;
	}
	
	
	
	
	
	
}

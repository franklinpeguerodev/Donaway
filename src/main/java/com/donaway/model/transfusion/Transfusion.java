package com.donaway.model.transfusion;

import java.time.LocalDateTime;

import com.donaway.utils.constants.TipoTransfusion;

public class Transfusion {

	private String idTransfusion;
	private String idSolicitud;
	private String rnc;
	private String codigoBarras;
	private TipoTransfusion tipoTransfusionObj;
	private LocalDateTime fechaTransfusion;
	
	//constructores
	public Transfusion(String idTransfusion, String idSolicitud, String rnc, String codigoBarras,
			LocalDateTime fechaTransfusion, TipoTransfusion tipoTransfusionObj) {
		super();
		this.idTransfusion = idTransfusion;
		this.idSolicitud = idSolicitud;
		this.rnc = rnc;
		this.codigoBarras = codigoBarras;
		this.fechaTransfusion = fechaTransfusion;
		this.tipoTransfusionObj = tipoTransfusionObj;
	}

	public Transfusion() {
		super();
	}
	
	//getters and setters

	public String getIdTransfusion() {
		return idTransfusion;
	}

	public void setIdTransfusion(String idTransfusion) {
		this.idTransfusion = idTransfusion;
	}

	public String getIdSolicitud() {
		return idSolicitud;
	}

	public void setIdSolicitud(String idSolicitud) {
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

	public TipoTransfusion getTipoTransfusionObj() {
		return tipoTransfusionObj;
	}

	public void setTipoTransfusionObj(TipoTransfusion tipoTransfusionObj) {
		this.tipoTransfusionObj = tipoTransfusionObj;
	}
	
	
	
	
	
	
}

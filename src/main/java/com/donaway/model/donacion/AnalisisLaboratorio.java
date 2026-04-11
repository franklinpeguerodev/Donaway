package com.donaway.model.donacion;

public class AnalisisLaboratorio {

	private String idAnalisis;
	private String idDonacion;
	private String codigoReferencia;
	private boolean vih;
	private boolean hepatitisB;
	private boolean hepatitisC;
	private boolean sifilis;
	private boolean chagas;
	private boolean nat;
	private String otros;
	
	
	// constructores
	public AnalisisLaboratorio(String idAnalisis, String idDonacion, String codigoReferencia, boolean vih, boolean hepatitisB, boolean hepatitisC,
			boolean sifilis, boolean chagas, boolean nat, String otros) {
		super();
		this.idAnalisis = idAnalisis;
		this.idDonacion = idDonacion;
		this.codigoReferencia = codigoReferencia;
		this.vih = vih;
		this.hepatitisB = hepatitisB;
		this.hepatitisC = hepatitisC;
		this.sifilis = sifilis;
		this.chagas = chagas;
		this.nat = nat;
		this.otros = otros;
	}


	public AnalisisLaboratorio() {
		super();
	}


	//getters and setters
	
	public String getIdAnalisis() {
		return idAnalisis;
	}


	public void setIdAnalisis(String idAnalisis) {
		this.idAnalisis = idAnalisis;
	}


	public String getIdDonacion() {
		return idDonacion;
	}


	public void setIdDonacion(String idDonacion) {
		this.idDonacion = idDonacion;
	}


	public boolean isVih() {
		return vih;
	}


	public void setVih(boolean vih) {
		this.vih = vih;
	}


	public boolean isHepatitisB() {
		return hepatitisB;
	}


	public void setHepatitisB(boolean hepatitisB) {
		this.hepatitisB = hepatitisB;
	}


	public boolean isHepatitisC() {
		return hepatitisC;
	}


	public void setHepatitisC(boolean hepatitisC) {
		this.hepatitisC = hepatitisC;
	}


	public boolean isSifilis() {
		return sifilis;
	}


	public void setSifilis(boolean sifilis) {
		this.sifilis = sifilis;
	}


	public boolean isChagas() {
		return chagas;
	}


	public void setChagas(boolean chagas) {
		this.chagas = chagas;
	}


	public boolean isNat() {
		return nat;
	}


	public void setNat(boolean nat) {
		this.nat = nat;
	}


	public String getOtros() {
		return otros;
	}


	public void setOtros(String otros) {
		this.otros = otros;
	}


	public String getCodigoReferencia() {
		return codigoReferencia;
	}


	public void setCodigoReferencia(String codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}
	
	
	
	
}

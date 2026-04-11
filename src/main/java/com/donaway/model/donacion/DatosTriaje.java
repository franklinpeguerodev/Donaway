package com.donaway.model.donacion;

public class DatosTriaje {

	private String idTriaje;
	private String idDonacion;
	private String codigoReferencia;
	private double pesoLbs;
	private int presionSistolica;
	private int presionDiastolica;
	private int pulso;
	private double temperatura;
	private double hemoglobina;
	
	
	//constructores
	public DatosTriaje(String idTriaje, String idDonacion, String codigoReferencia, double pesoLbs, int presionSistolica, int presionDiastolica,
			int pulso, double temperatura, double hemoglobina) {
		super();
		this.idTriaje = idTriaje;
		this.idDonacion = idDonacion;
		this.codigoReferencia = codigoReferencia;
		this.pesoLbs = pesoLbs;
		this.presionSistolica = presionSistolica;
		this.presionDiastolica = presionDiastolica;
		this.pulso = pulso;
		this.temperatura = temperatura;
		this.hemoglobina = hemoglobina;
	}
	public DatosTriaje() {
		super();
	}
	
	//getters and setters
	public String getIdTriaje() {
		return idTriaje;
	}
	public void setIdTriaje(String idTriaje) {
		this.idTriaje = idTriaje;
	}
	public String getIdDonacion() {
		return idDonacion;
	}
	public void setIdDonacion(String idDonacion) {
		this.idDonacion = idDonacion;
	}
	public double getPesoLbs() {
		return pesoLbs;
	}
	public void setPesoLbs(double pesoLbs) {
		this.pesoLbs = pesoLbs;
	}
	public int getPresionSistolica() {
		return presionSistolica;
	}
	public void setPresionSistolica(int presionSistolica) {
		this.presionSistolica = presionSistolica;
	}
	public int getPresionDiastolica() {
		return presionDiastolica;
	}
	public void setPresionDiastolica(int presionDiastolica) {
		this.presionDiastolica = presionDiastolica;
	}
	public int getPulso() {
		return pulso;
	}
	public void setPulso(int pulso) {
		this.pulso = pulso;
	}
	public double getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}
	public double getHemoglobina() {
		return hemoglobina;
	}
	public void setHemoglobina(double hemoglobina) {
		this.hemoglobina = hemoglobina;
	}
	public String getCodigoReferencia() {
		return codigoReferencia;
	}
	public void setCodigoReferencia(String codigoReferencia) {
		this.codigoReferencia = codigoReferencia;
	}
	
	
	
	
}

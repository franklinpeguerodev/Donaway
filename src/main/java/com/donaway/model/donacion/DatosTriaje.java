package com.donaway.model.donacion;

public class DatosTriaje {

	private int idTriaje;
	private int idDonacion;
	private float pesoLbs;
	private int presionSistolica;
	private int presionDiastolica;
	private int pulso;
	private float temperatura;
	private float hemoglobina;
	
	
	//constructores
	public DatosTriaje(int idTriaje, int idDonacion, float pesoLbs, int presionSistolica, int presionDiastolica,
			int pulso, float temperatura, float hemoglobina) {
		super();
		this.idTriaje = idTriaje;
		this.idDonacion = idDonacion;
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
	public int getIdTriaje() {
		return idTriaje;
	}
	public void setIdTriaje(int idTriaje) {
		this.idTriaje = idTriaje;
	}
	public int getIdDonacion() {
		return idDonacion;
	}
	public void setIdDonacion(int idDonacion) {
		this.idDonacion = idDonacion;
	}
	public float getPesoLbs() {
		return pesoLbs;
	}
	public void setPesoLbs(float pesoLbs) {
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
	public float getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}
	public float getHemoglobina() {
		return hemoglobina;
	}
	public void setHemoglobina(float hemoglobina) {
		this.hemoglobina = hemoglobina;
	}
	
	
	
	
}

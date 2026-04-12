package com.donaway.utils.constants;

public enum EstadoUnidad {

	DISPONIBLE("Disponible"), 
	RESERVADA("Reservada"), 
	DESCARTADA("Descartada"), 
	ESPERA("Espera"), 
	USADA("Usada");
	private final String descripcionFront;
	EstadoUnidad(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
}

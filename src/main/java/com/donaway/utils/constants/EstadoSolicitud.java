package com.donaway.utils.constants;

public enum EstadoSolicitud {
	PENDIENTE("Pendiente"), 
	EN_CAMINO("En Camino"), 
	ENTREGADA("Entregada");
	private final String descripcionFront;
	EstadoSolicitud(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
}

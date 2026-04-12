package com.donaway.utils.constants;

public enum PrioridadSolicitud  {
	EMERGENCIA("Emergencia"), 
	URGENTE("Urgente"), 
	RUTINA("Rutina");
	private final String descripcionFront;
	PrioridadSolicitud(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}

	}

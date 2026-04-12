package com.donaway.utils.constants;

public enum TipoComponente {
	GLOBULOS_ROJOS ("Globulos Rojos"),
	PLAQUETAS("Plaquetas"),
	PLASMA("Plasma");
	
	private final String descripcionFront;
	TipoComponente(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;
}}

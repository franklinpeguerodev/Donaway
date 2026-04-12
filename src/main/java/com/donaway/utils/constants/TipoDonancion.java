package com.donaway.utils.constants;

public enum TipoDonancion {
	SANGRE_TOTAL ("Sangre Total"), 
	AFERESIS ("Aferesis");
	private final String descripcionFront;
	
	TipoDonancion(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
}

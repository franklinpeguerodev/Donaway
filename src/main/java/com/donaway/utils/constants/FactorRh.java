package com.donaway.utils.constants;

public enum FactorRh {	
	POSITIVO("+"),
	NEGATIVO("-");

	private final String descripcionFront;
	FactorRh(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
}
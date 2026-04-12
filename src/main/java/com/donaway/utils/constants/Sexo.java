package com.donaway.utils.constants;

public enum Sexo {
M ("Masculino"),
F ("Femenino");
	
	private final String descripcionFront;
	Sexo(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}

	}

package com.donaway.utils.constants;

public enum Departamento{
	UNIDAD_EXTRACCION("Unidad de Extraccion"),
	RECEPSIONISTA("Recepcionista"); 

private final String descripcionFront;
Departamento(String descripcionFront)
{this.descripcionFront = descripcionFront;}

@Override
public String toString() {
	return descripcionFront;}

}
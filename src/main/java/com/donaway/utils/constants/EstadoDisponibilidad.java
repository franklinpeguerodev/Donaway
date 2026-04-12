package com.donaway.utils.constants;

public enum EstadoDisponibilidad {
ACTIVO("Activo"),
DIFERIDO("Diferido"),
EXCLUIDO("Excluido");

private final String descripcionFront;
EstadoDisponibilidad(String descripcionFront)
{this.descripcionFront = descripcionFront;}

@Override
public String toString() {
	return descripcionFront;}

}
package com.donaway.utils.constants;

public enum EstadoPersonal {
	ACTIVO("Activo"),
	VACACIONES("Vacaciones"),
	DESPEDIDO("Despedido");
	private final String descripcionFront;

EstadoPersonal(String descripcionFront)
{this.descripcionFront = descripcionFront;}

@Override
public String toString() {
	return descripcionFront;}


}
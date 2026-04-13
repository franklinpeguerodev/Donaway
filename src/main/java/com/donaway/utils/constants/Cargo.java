package com.donaway.utils.constants;

public enum Cargo{
	ADMIN_INSTITUCIONAL("Admin Institucional"),
	ADMIN_PERSONAL_MEDICO("Admin Personal Medico"),
	BIOANALISTA_PRINCIPAL("Bionalista Principal"),
	ASISTENTE_BIONALISTA("Asistente Bionalista"),
	RECEPSIONISTA("Recepsionista"),
	GESTOR_INVENTARIO("Gestionador De Inventario");
	
	private final String descripcionFront;
	Cargo(String descripcionFront)
	{this.descripcionFront = descripcionFront;}
	
	@Override
	public String toString() {
		return descripcionFront;}
	
	
}

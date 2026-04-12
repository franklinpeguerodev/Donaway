package com.donaway.utils.constants;

public enum Rol{
 ADMIN_INSTITUCIONAL("Admin Institucional"),
 ADMIN_PERSONAL_MEDICO("Admin Personal Medico"),
 BIOANALISTA("Bionalista"),
 GESTOR_INVENTARIO("Gestor de inventario"),
 RECEPCIONISTA("Recepcionista");
	
	private final String descripcionFront;
	Rol(String descripcionFront)
	{this.descripcionFront = descripcionFront;}
	
	@Override
	public String toString() {
		return descripcionFront;}
	
	
}

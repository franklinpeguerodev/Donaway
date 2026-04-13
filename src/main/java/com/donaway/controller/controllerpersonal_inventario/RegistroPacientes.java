package com.donaway.controller.controllerpersonal_inventario;
	
	public class RegistroPacientes {
	    private String dia;
	    private int cantidad;

	    public RegistroPacientes(String dia, int cantidad) {
	        this.dia = dia;
	        this.cantidad = cantidad;
	    }

	    public String getDia() { return dia; }
	    public int getCantidad() { return cantidad; }
	
}

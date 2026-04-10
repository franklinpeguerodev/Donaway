package com.donaway.model.ubicacion;

public abstract class UbicacionDpaVista {

	// atributos
	protected int id;
	protected String nombre;
	
	
	// constructores
	public UbicacionDpaVista(int id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}


	public UbicacionDpaVista() {
		super();
	}


	// getters and setters
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
	
}

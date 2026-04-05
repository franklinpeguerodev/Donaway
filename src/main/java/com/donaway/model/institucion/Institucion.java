package com.donaway.model.institucion;
import java.time.LocalDate;
public class Institucion{
	private String rnc;
	private String nombre;
	private String contactoEmergencia;
	private LocalDate fechaRegistro;

	public Institucion (String rnc,String nombre, String contactoEmergencia,LocalDate fechaRegistro){
		this.rnc=rnc;
		this.nombre=nombre;
		this.contactoEmergencia=contactoEmergencia;
	}

}

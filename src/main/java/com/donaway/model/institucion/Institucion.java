package com.donaway.model.institucion;
import java.time.LocalDate;
public class Institucion{
	private String rnc;
	private String nombre;
	private String idUbicacion;
	private String contactoEmergencia;
	private LocalDate fechaRegistro;

	public Institucion (String rnc,String nombre, String idUbicacion, String contactoEmergencia,LocalDate fechaRegistro){
		this.rnc=rnc;
		this.idUbicacion = idUbicacion;
		this.nombre=nombre;
		this.contactoEmergencia=contactoEmergencia;
		this.fechaRegistro=fechaRegistro;
	}
	
public String getRnc(){
  return this.rnc;
}
public void setRnc(String rnc){
  this.rnc=rnc;
}


public String getNombre(){
  return this.nombre;
}
public void setNombre (String nombre){
  this.nombre=nombre;
}


public String getIdUbicacion() {
	return idUbicacion;
}

public void setIdUbicacion(String idUbicacion) {
	this.idUbicacion = idUbicacion;
}

public String getContactoEmergencia(){
  return this.contactoEmergencia;
}
public void setContactoEmergencia (String contactoEmergencia){
  this.contactoEmergencia=contactoEmergencia;
}

public LocalDate getFechaRegistro (){
  return this.fechaRegistro;
}
public void setFechaRegistro (LocalDate fechaRegistro){
  this.fechaRegistro =fechaRegistro;
}
}

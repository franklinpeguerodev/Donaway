package com.donaway.model.user;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.donaway.utils.constants.Sexo;
//clase abstracta responsble de todas las personas que usaran el software
public abstract class Persona {
	protected String documentId;
	protected String nombre;
	protected Sexo sexo;
	protected LocalDate fechaNacimiento;
	protected String telefono ;
	protected String email;
	protected LocalDateTime fechaRegistro;
	
	public Persona (String documentId,String nombre,Sexo sexo,LocalDate fechaNacimiento,String telefono,
			String email,LocalDateTime fechaRegistro){
			this.documentId =documentId;
			this.nombre =nombre;
			this.sexo =sexo;
			this.fechaNacimiento =fechaNacimiento;
			this.telefono =telefono;
			this.email =email;
			this.fechaRegistro =fechaRegistro;
			
	}
	public Persona () {}
	
	public String getDocumentoId() {
		return this.documentId;
	}
	public void setDocumentoId (String documentoId) {
		this.documentId=documentoId;
	}
	

	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre =nombre;
	}
	
	
	public Sexo getSexo() {
		return this.sexo;
	}
	public void setSexo(Sexo sexo) {
		this.sexo =sexo;
	}
	
	
	public LocalDate getFechaNacimiento() {
		return this.fechaNacimiento;
	}
	public void setFechaNacimiento (LocalDate fechaNacimiento) {
		this.fechaNacimiento=fechaNacimiento;
	}
	
	
	public String getTelefono() {
		return this.telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono=telefono;
	}
	
	
	public String getEmail() {
		return this.email;
	}
	public void setEmail (String email) {
		this.email= email;
	}
	
	public LocalDateTime getFechaRegistro() {
		return this.fechaRegistro;
	}
	public void setFechaRegistro (LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

}

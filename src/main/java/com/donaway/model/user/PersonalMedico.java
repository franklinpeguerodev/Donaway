package com.donaway.model.user;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class PersonalMedico extends Persona{
	
	protected String rnc;
	protected String rol;
	protected String credencialEncriptada;
	protected LocalDateTime ultimoAcceso;


	public PersonalMedico (String documentId, String nombre, String sexo, LocalDate fechaNacimiento,String telefono,
		String email,LocalDateTime fechaRegistro,String rnc, String rol,String credencialEncriptada, LocalDateTime ultimoAcceso) {
		super(documentId,nombre,sexo,fechaNacimiento,telefono,email,fechaRegistro);
		this.rnc=rnc;
		this.rol=rol;
		this.credencialEncriptada=credencialEncriptada;
		this.ultimoAcceso=ultimoAcceso;
		}
	
	public String getRnc() {
		return this.rnc;
	}

	public void setRnc(String rnc) {
		this.rnc=rnc;
	}
	
	
	public String getRol() {
		return this.rol;
	}
	public void setRol(String rol) {
		this.rol=rol;
	}

	
	public String getCredencialEncriptada() {
		return this.credencialEncriptada;
	}
	public void setCredencialEncriptada(String credencialEncriptada) {
		this.credencialEncriptada=credencialEncriptada;
	}
	
	
	public LocalDateTime getUltimoAcceso () {
		return this.ultimoAcceso;
	}
	public void setUltimoAcceso (LocalDateTime ultimoAcceso) {
		
	}
}

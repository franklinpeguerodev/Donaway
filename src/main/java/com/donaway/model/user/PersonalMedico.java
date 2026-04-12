package com.donaway.model.user;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.donaway.utils.constants.Rol;
import com.donaway.utils.constants.Sexo;
public class PersonalMedico extends Persona{
	
	protected String rnc;
	protected Rol rol;
	protected String credencialEncriptada;
	protected LocalDateTime ultimoAcceso;


	public PersonalMedico (String documentId, String nombre, Sexo sexo, LocalDate fechaNacimiento,String telefono,
		String email,LocalDateTime fechaRegistro,String rnc, Rol rol,String credencialEncriptada, LocalDateTime ultimoAcceso) {
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
	
	
	public Rol getRol() {
		return this.rol;
	}
	public void setRol(Rol rol) {
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
		this.ultimoAcceso = ultimoAcceso;
	}
}


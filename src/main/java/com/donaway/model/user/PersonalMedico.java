package com.donaway.model.user;

import java.time.LocalDateTime;
import java.time.LocalDate;

import com.donaway.utils.constants.Cargo;
import com.donaway.utils.constants.Departamento;
import com.donaway.utils.constants.EstadoPersonal;
import com.donaway.utils.constants.Jornada;
import com.donaway.utils.constants.Rol;
import com.donaway.utils.constants.Sexo;

public class PersonalMedico extends Persona{

	private String rnc;
	private Cargo cargo;
	private Departamento departamento;
	private Rol rol;
	private Jornada jornada;
	private EstadoPersonal estado;
	private String credencialEncriptada;
	private LocalDateTime ultimoAcceso;
	
	public PersonalMedico(String documentId, String nombre, Sexo sexo, LocalDate fechaNacimiento, String telefono,
			String email, LocalDateTime fechaRegistro, String rnc, Cargo cargo, Departamento departamento, Rol rol,
			Jornada jornada, EstadoPersonal estado, String credencialEncriptada, LocalDateTime ultimoAcceso) {
		super(documentId, nombre, sexo, fechaNacimiento, telefono, email, fechaRegistro);
		this.rnc = rnc;
		this.cargo = cargo;
		this.departamento = departamento;
		this.rol = rol;
		this.jornada = jornada;
		this.estado = estado;
		this.credencialEncriptada = credencialEncriptada;
		this.ultimoAcceso = ultimoAcceso;
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

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Jornada getJornada() {
		return jornada;
	}

	public void setJornada(Jornada jornada) {
		this.jornada = jornada;
	}

	public EstadoPersonal getEstado() {
		return estado;
	}

	public void setEstado(EstadoPersonal estado) {
		this.estado = estado;
	}
}


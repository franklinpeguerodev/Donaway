package com.donaway.model.persona;
import com.donaway.model.user.Persona;

import java.time.LocalDateTime;
import java.time.LocalDate;
public class Donante extends Persona{

	private String tipoSangre;
	private String factorRh;
	private LocalDateTime ultimaDonacion;
	private String estadoDisponibilidad;
	private Boolean autorizacionContacto;
	
	public Donante (String documentId,String nombre,String sexo,LocalDate fechaNacimiento,String telefono,
			String email,LocalDateTime fechaRegistro,String tipoSangre,String factorRh,LocalDateTime ultimaDonacion,
			String estadoDisponibilidad,Boolean autorizacionContacto) {
		super(documentId,nombre,sexo,fechaNacimiento,telefono,email,fechaRegistro);
		this.tipoSangre=tipoSangre;
		this.factorRh=factorRh;
		this.ultimaDonacion=ultimaDonacion;
		this.estadoDisponibilidad=estadoDisponibilidad;
		this.autorizacionContacto=autorizacionContacto;
	}	

	public String getTipoSangre () {
		return this.tipoSangre;
	}
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre=tipoSangre;
	}
	
	
	public String getFactorRh () {
		return this.factorRh;
	}
	public void setFactorRh(String factorRh) {
		this.factorRh=factorRh;
	}
	
	
	
	public LocalDateTime getUltimaDonacion () {
		return this.ultimaDonacion;
	}
	public void setUltimaDonacion(LocalDateTime ultimaDonacion) {
		this.ultimaDonacion=ultimaDonacion;
	}
	
	
	public String getEstadoDisponibilidad () {
		return this.estadoDisponibilidad;
	}
	public void setEstadoDisponibilidad(String estadoDisponibilidad) {
		this.estadoDisponibilidad=estadoDisponibilidad;
	}

	
	public Boolean getAutorizacionContacto () {
		return this.autorizacionContacto;
	}
	public void setAutorizacionContacto(Boolean autorizacionContacto) {
		this.autorizacionContacto=autorizacionContacto;
	}
}

package com.donaway.model.donacion;
import com.donaway.model.user.Persona;

import java.time.LocalDateTime;
import java.time.LocalDate;
public class Donante extends Persona{
	private float peso;
	private String tipoSangre;
	private String factorRh;
	private LocalDateTime ultimaDonacion;
	private String estadoDisponibilidad;
	private String direccion;
	private Boolean autorizacionContacto;
	
	public Donante (String documentoId,String nombre,String sexo,LocalDate fechaNacimiento,String telefono,
			String email,LocalDateTime fechaRegistro,float peso,String tipoSangre,String factorRh,LocalDateTime ultimaDonacion,
			String estadoDisponibilidad,String direccion,Boolean autorizacionContacto) {
		super(documentoId,nombre,sexo,fechaNacimiento,telefono,email,fechaRegistro);
		this.peso=peso;
		this.tipoSangre=tipoSangre;
		this.factorRh=factorRh;
		this.ultimaDonacion=ultimaDonacion;
		this.estadoDisponibilidad=estadoDisponibilidad;
		this.direccion=direccion;
		this.autorizacionContacto=autorizacionContacto;
	}
	public Donante() {
		super();
	}
		
	//get
	public float getPeso () {
		return this.peso;
	}
	public String getTipoSangre () {
		return this.tipoSangre;
	}
	public String getFactorRh () {
		return this.factorRh;
	}
	public LocalDateTime getUltimaDonacion () {
		return this.ultimaDonacion;
	}
	public String getEstadoDisponibilidad () {
		return this.estadoDisponibilidad;
	}
	public String getDireccion () {
		return this.direccion;
	}
	public Boolean getAutorizacionContacto () {
		return this.autorizacionContacto;
	}
	
	//set
	public void setPeso(float peso) {
		this.peso=peso;
	}
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre=tipoSangre;
	}
	public void setFactorRh(String factorRh) {
		this.factorRh=factorRh;
	}
	public void setUltimaDonacion(LocalDateTime ultimaDonacion) {
		this.ultimaDonacion=ultimaDonacion;
	}
	public void setEstadoDisponibilidad(String estadoDisponibilidad) {
		this.estadoDisponibilidad=estadoDisponibilidad;
	}
	public void setDireccion(String direccion) {
		this.direccion=direccion;
	}
	public void setAutorizacionContacto(Boolean autorizacionContacto) {
		this.autorizacionContacto=autorizacionContacto;
	}
}

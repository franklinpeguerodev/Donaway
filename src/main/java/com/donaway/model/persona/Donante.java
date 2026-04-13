package com.donaway.model.persona;

import com.donaway.model.user.Persona;
import com.donaway.utils.constants.EstadoDisponibilidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.Sexo;
import com.donaway.utils.constants.TipoSangre;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Donante extends Persona{

	private TipoSangre tipoSangre;
	private FactorRh factorRh;
	private String idUbicacion;
	private LocalDateTime ultimaDonacion;
	private EstadoDisponibilidad estadoDisponibilidad;
	private Boolean autorizacionContacto;
	private String dirreccionCompleta;
	
	public Donante (String documentId,String nombre,Sexo sexo,LocalDate fechaNacimiento,String telefono,
			String email,LocalDateTime fechaRegistro,TipoSangre tipoSangre,FactorRh factorRh,String idUbicacion, LocalDateTime ultimaDonacion,
			EstadoDisponibilidad estadoDisponibilidad,Boolean autorizacionContacto) {
		super(documentId,nombre,sexo,fechaNacimiento,telefono,email,fechaRegistro);
		this.tipoSangre=tipoSangre;
		this.factorRh=factorRh;
		this.idUbicacion = idUbicacion;
		this.ultimaDonacion=ultimaDonacion;
		this.estadoDisponibilidad=estadoDisponibilidad;
		this.autorizacionContacto=autorizacionContacto;
	}	

	//constructor temporal para que el controller no de error
	public Donante(String documentId, String nombre, String sexo, LocalDate fechaNacimiento, String telefono,
			String email, LocalDateTime fechaRegistro, String tipoSangre2, String factorRh2,
			LocalDateTime ultimaDonacion2, String estadoDisponibilidad2, boolean autorizacionContacto2) {
		// TODO Auto-generated constructor stub
	}

	public TipoSangre getTipoSangre () {
		return this.tipoSangre;
	}
	public void setTipoSangre(TipoSangre tipoSangre) {
		this.tipoSangre=tipoSangre;
	}
	
	
	public FactorRh getFactorRh () {
		return this.factorRh;
	}
	public void setFactorRh(FactorRh factorRh) {
		this.factorRh=factorRh;
	}
	
	
	
	public String getIdUbicacion() {
		return idUbicacion;
	}

	public void setIdUbicacion(String idUbicacion) {
		this.idUbicacion = idUbicacion;
	}

	public LocalDateTime getUltimaDonacion () {
		return this.ultimaDonacion;
	}
	public void setUltimaDonacion(LocalDateTime ultimaDonacion) {
		this.ultimaDonacion=ultimaDonacion;
	}
	
	
	public EstadoDisponibilidad getEstadoDisponibilidad () {
		return this.estadoDisponibilidad;
	}
	public void setEstadoDisponibilidad(EstadoDisponibilidad estadoDisponibilidad) {
		this.estadoDisponibilidad=estadoDisponibilidad;
	}

	
	public Boolean getAutorizacionContacto () {
		return this.autorizacionContacto;
	}
	public void setAutorizacionContacto(Boolean autorizacionContacto) {
		this.autorizacionContacto=autorizacionContacto;
	}

	public String getDirreccionCompleta() {
		return dirreccionCompleta;
	}

	public void setDirreccionCompleta(String dirreccionCompleta) {
		this.dirreccionCompleta = dirreccionCompleta;
	}
}

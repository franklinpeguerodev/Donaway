
package com.donaway.model.ubicacion;

public class Ubicacion {

	protected int idRegion;
	protected int idProvincia;
	protected int idMunicipio;
	protected int idDistrito;
	protected int idSeccion;
	protected int idBarrio;
	protected int idSubBarrio;
	protected String direccion;
	
	
	public Ubicacion(int idRegion, int idProvincia, int idMunicipio, int idDistrito, int idSeccion, int idBarrio,
			int idSubBarrio, String direccion) {
		super();
		this.idRegion = idRegion;
		this.idProvincia = idProvincia;
		this.idMunicipio = idMunicipio;
		this.idDistrito = idDistrito;
		this.idSeccion = idSeccion;
		this.idBarrio = idBarrio;
		this.idSubBarrio = idSubBarrio;
		this.direccion = direccion;
	}
	
	
	public int getIdRegion() {
		return idRegion;
	}


	public void setIdRegion(int idRegion) {
		this.idRegion = idRegion;
	}


	public int getIdProvincia() {
		return idProvincia;
	}


	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
	}


	public int getIdMunicipio() {
		return idMunicipio;
	}


	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
	}


	public int getIdDistrito() {
		return idDistrito;
	}


	public void setIdDistrito(int idDistrito) {
		this.idDistrito = idDistrito;
	}


	public int getIdSeccion() {
		return idSeccion;
	}


	public void setIdSeccion(int idSeccion) {
		this.idSeccion = idSeccion;
	}


	public int getIdBarrio() {
		return idBarrio;
	}


	public void setIdBarrio(int idBarrio) {
		this.idBarrio = idBarrio;
	}


	public int getIdSubBarrio() {
		return idSubBarrio;
	}


	public void setIdSubBarrio(int idSubBarrio) {
		this.idSubBarrio = idSubBarrio;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
}

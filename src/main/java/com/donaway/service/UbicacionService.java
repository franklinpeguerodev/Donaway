package com.donaway.service;

import com.donaway.dao.ubicacion.IUbicacionDao;
import com.donaway.model.ubicacion.Barrio;
import com.donaway.model.ubicacion.Distrito;
import com.donaway.model.ubicacion.Municipio;
import com.donaway.model.ubicacion.Provincia;
import com.donaway.model.ubicacion.Region;
import com.donaway.model.ubicacion.Seccion;
import com.donaway.model.ubicacion.SubBarrio;
import com.donaway.model.ubicacion.Ubicacion;

import java.util.ArrayList;
import java.util.List;

import com.donaway.dao.impl.ubicacion.UbicacionImpl;

public class UbicacionService {

	private IUbicacionDao ubiDao = new UbicacionImpl();
	
	
	
	////////////////////////////////////////////////////////////////////////////
	// metodo para validar un registro de ubicacicion 
	public int registrarNuevaUbicacion (Ubicacion ubi) {
		
		//verificacion de que no haya campos obligatorios vacios
		if( ubi.getIdRegion() == 0 ||
			ubi.getIdProvincia() == 0 || 
			ubi.getIdMunicipio() == 0 || 
			ubi.getIdSeccion() == 0 || 
			ubi.getIdBarrio() == 0 || 
			ubi.getDireccion() == null 	||
			ubi.getDireccion().trim().isEmpty()) {
			
			System.out.println("Error del service: No se han llenado campos de datos obligatorios");
			return 0;
			 
		} 
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//esto retonara la pk del ide para poder registrarlo
		int devolverId = ubiDao.registrarUbicacion(ubi);
		
		if (devolverId > 0) {
			
			System.out.println("Se registro la ubicacion con exito. ID " + devolverId);

		} else {
			
			System.out.println("Error del service: No se pudo completar el registro");
			
		}
				
		return devolverId;
	}
	////////////////////////////////////////////////////////////////////////////
	

	
	////////////////////////////////////////////////////////////////////////////
	//metodo para acceder a las regiones 
	public List<Region> obtenerTodasLasRegiones() {
		
		
		return ubiDao.listaRegiones();
	}
	////////////////////////////////////////////////////////////////////////////
	
	
	
	////////////////////////////////////////////////////////////////////////////
	//metodo para obtner la filtracion de las provincias por regiones
	public List<Provincia> obtenerProvinciasPorRegion(int idRegion) {
		
		if (idRegion <= 0) {
			
			System.out.println("Error de service: id de region no valido, no se puede ingresar una provincia");
			
			return new ArrayList<>();
		}
		
		return ubiDao.listaProvinciasPorRegion(idRegion);
	}
	////////////////////////////////////////////////////////////////////////////
	

	
	////////////////////////////////////////////////////////////////////////////
	//metodo para obtner la filtracion de las municipios por provincias
		public List<Municipio> obtenerMunicipioPorProvincia(int idProvincia) {
			
			if (idProvincia <= 0) {
				
				System.out.println("Error de service: id de provincia no valido, no se puede ingresar un municipio");
				
				return new ArrayList<>();
			}
			
			return ubiDao.listaMunicipioPorProvincia(idProvincia);
		}
		////////////////////////////////////////////////////////////////////////////
		
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtner la filtracion de los distritos por municipios
		public List<Distrito> obtenerDistritoPorMunicipio(int idMunicipio) {
			
			if (idMunicipio <= 0) {
				
				System.out.println("Servise: No se estan opteniendo distritos");
				
				return new ArrayList<>();
			}
			
			return ubiDao.listaDistritoPorMunicipio(idMunicipio);
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtner la filtracion de las secciones por municipios
		public List<Seccion> obtenerSeccionPorMunicipio(int idMunicipio) {
					
			if (idMunicipio <= 0) {
				
				System.out.println("Error de service: id de municipio no valido, no se puede ingresar una seccion");
				
				return new ArrayList<>();
			}
					
			return ubiDao.listaSeccionPorMunicipio(idMunicipio);
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtner la filtracion de las secciones por distrito
		public List<Seccion> obtenerSeccionPorDistrito(int idDistrito) {
			
			if (idDistrito <= 0) {
				
				System.out.println("Servise: No se estan opteniendo secciones por distritos");
				
				return new ArrayList<>();
			}
			
			return ubiDao.listaSeccionPorDistrito(idDistrito);
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtener la filtraccion de los barrios por las secciones
		public List<Barrio> obtenerBarrioPorSeccion(int idSeccion) {
			
			if (idSeccion <= 0 ) {
				
				System.out.println("Error de service: id de seccion no valido, no se puede ingresar un barrio");
				
				return new ArrayList<>();
			}
			
			return ubiDao.listaBarrioPorSeccion(idSeccion);
		}		
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtener la filtraccion de los barrios por las secciones
		public List<SubBarrio> obtenerSubBarrioPorBarrio(int idBarrio) {
			
			if (idBarrio <= 0 ) {
				
				System.out.println("Servise: No se estan opteniendo subBarrios");
				
				return new ArrayList<>();
			}
			
			return ubiDao.listaSubBarrioPorBarrio(idBarrio);
		}		
		////////////////////////////////////////////////////////////////////////////
}

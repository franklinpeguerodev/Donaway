package com.donaway.service;

import com.donaway.dao.ubicacion.IUbicacionDao;
import com.donaway.exception.ValidacionException;
import com.donaway.model.ubicacion.Barrio;
import com.donaway.model.ubicacion.Distrito;
import com.donaway.model.ubicacion.Municipio;
import com.donaway.model.ubicacion.Provincia;
import com.donaway.model.ubicacion.Region;
import com.donaway.model.ubicacion.Seccion;
import com.donaway.model.ubicacion.SubBarrio;
import com.donaway.model.ubicacion.Ubicacion;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.donaway.dao.impl.ubicacion.UbicacionImpl;

public class UbicacionService {

	private IUbicacionDao ubiDao = new UbicacionImpl();
	
	
	
	////////////////////////////////////////////////////////////////////////////
	// metodo para validar un registro de ubicacicion 
	public String registrarNuevaUbicacion (Ubicacion ubi) {
		
		//verificacion de que no haya campos obligatorios vacios
		if( ubi.getIdRegion() == 0 ||
			ubi.getIdProvincia() == 0 || 
			ubi.getIdMunicipio() == 0 || 
			ubi.getIdSeccion() == 0 || 
			ubi.getIdBarrio() == 0 || 
			ubi.getDireccion() == null 	||
			ubi.getDireccion().trim().isEmpty()) {
			
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
			 
		} 
		
		try {
		//esto retonara la pk del ide para poder registrarlo
		
		String devolverId = ubiDao.registrarUbicacion(ubi);
		
			if (devolverId == null) {
				
				throw new ValidacionException("No se pudo completar el registro de la ubicacion");
			}
				return devolverId;
			
		} catch (SQLException e) {
			
	        throw new ValidacionException(Mensaje.ERR_CONEXION);
	    }
	}
				
	////////////////////////////////////////////////////////////////////////////
	

	
	////////////////////////////////////////////////////////////////////////////
	
	//metodo para acceder a las regiones 
	public List<Region> obtenerTodasLasRegiones() {
		
		try {
			
		return ubiDao.listaRegiones();
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
			
		}
	}
	////////////////////////////////////////////////////////////////////////////
	
	
	
	////////////////////////////////////////////////////////////////////////////
	//metodo para obtner la filtracion de las provincias por regiones
	public List<Provincia> obtenerProvinciasPorRegion(int idRegion) {
		
		try {
			if (idRegion <= 0) {
	
				throw new ValidacionException(Mensaje.ERR_UBICACION_INCOMPLETA);
				
			}
			
			return ubiDao.listaProvinciasPorRegion(idRegion);
			
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
			
		}

	}
	////////////////////////////////////////////////////////////////////////////
	

	
	////////////////////////////////////////////////////////////////////////////
	//metodo para obtner la filtracion de las municipios por provincias
		public List<Municipio> obtenerMunicipioPorProvincia(int idProvincia) {
			try {
				if (idProvincia <= 0) {
					
					throw new ValidacionException(Mensaje.ERR_UBICACION_INCOMPLETA);
				}
				
				return ubiDao.listaMunicipioPorProvincia(idProvincia);
			
			} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		
			}
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtner la filtracion de los distritos por municipios
		public List<Distrito> obtenerDistritoPorMunicipio(int idMunicipio) {
			
			
			try {
				if (idMunicipio <= 0) {
					
					throw new ValidacionException(Mensaje.ERR_UBICACION_INCOMPLETA);
				}
				
				return ubiDao.listaDistritoPorMunicipio(idMunicipio);
				
			} catch (SQLException e) {
				throw new ValidacionException(Mensaje.ERR_CONEXION);
			}
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtner la filtracion de las secciones por municipios
		public List<Seccion> obtenerSeccionPorMunicipio(int idMunicipio) {
			try {		
				if (idMunicipio <= 0) {
					
					throw new ValidacionException(Mensaje.ERR_UBICACION_INCOMPLETA);
				}
						
				return ubiDao.listaSeccionPorMunicipio(idMunicipio);
			} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
			}
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtner la filtracion de las secciones por distrito
		public List<Seccion> obtenerSeccionPorDistrito(int idDistrito) {
			try {
				if (idDistrito <= 0) {
					return new ArrayList<>();
				}
				
				return ubiDao.listaSeccionPorDistrito(idDistrito);
			} catch (SQLException e) {
				throw new ValidacionException(Mensaje.ERR_CONEXION);
			}
		}	
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtener la filtraccion de los barrios por las secciones
		public List<Barrio> obtenerBarrioPorSeccion(int idSeccion) {
			try {
				if (idSeccion <= 0 ) {
					
					throw new ValidacionException(Mensaje.ERR_UBICACION_INCOMPLETA);
				}
				
				return ubiDao.listaBarrioPorSeccion(idSeccion);
			} catch (SQLException e) {
				throw new ValidacionException(Mensaje.ERR_CONEXION);
			}
		}		
		////////////////////////////////////////////////////////////////////////////
		
		
		
		////////////////////////////////////////////////////////////////////////////
		//metodo para obtener la filtraccion de los barrios por las secciones
		public List<SubBarrio> obtenerSubBarrioPorBarrio(int idBarrio) {
			
			try {
				if (idBarrio <= 0 ) {
					return new ArrayList<>();
				}
				
				return ubiDao.listaSubBarrioPorBarrio(idBarrio);
				
			} catch (SQLException e) {
				throw new ValidacionException(Mensaje.ERR_CONEXION);
			}
		}		
		////////////////////////////////////////////////////////////////////////////
}

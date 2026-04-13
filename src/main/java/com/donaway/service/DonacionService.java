package com.donaway.service;

import com.donaway.dao.donacion.IDonacionDao;
import com.donaway.dao.impl.donacion.DonacionDaoImpl;
import com.donaway.exception.ValidacionException;
import com.donaway.model.donacion.Donacion;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class DonacionService {

	private IDonacionDao donacionDao = new DonacionDaoImpl();

	////////////////////////////////////////////////////////////////////////////
	// Metodo para validar y registrar una nueva donacion de sangre
	
	public void registrarDonacion(Donacion dona) {
		
		if (dona.getDocumentIdDonante() == null || dona.getDocumentIdPersonal() == null ||
			dona.getRnc() == null || dona.getCodigoReferencia() == null || 
			dona.getCantidadPinta() <= 0) {
			
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			if (dona.getIdDonacion() == null || dona.getIdDonacion().isEmpty()) {
				dona.setIdDonacion(UUID.randomUUID().toString());
			}

			if (donacionDao.obtenerPorCodigoReferencia(dona.getCodigoReferencia()) != null) {
				throw new ValidacionException("El codigo de referencia de esta pinta ya existe.");
			}

			donacionDao.registrar(dona);

		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	// Metodo para buscar una donacion por su codigo de barra/referencia
	
	public Donacion buscarPorReferencia(String codigo) {
		
		if (codigo == null || codigo.trim().isEmpty()) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			Donacion d = donacionDao.obtenerPorCodigoReferencia(codigo);
			if (d == null) {
				throw new ValidacionException(Mensaje.ERR_BUSQUEDA);
			}
			return d;
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////

	
	public List<Donacion> historialGeneral() {
		try {
			return donacionDao.obtenerHistorial();
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////
	/// 
	// Metodo para obtener el historial de un donante 
	public List<Donacion> historialPorDonante(String cedula) {
		
		if (cedula == null || cedula.trim().isEmpty()) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			return donacionDao.obtenerHistorialDeDonante(cedula);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	////////////////////////////////////////////////////////////////////////////
}
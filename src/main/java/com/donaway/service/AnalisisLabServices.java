package com.donaway.service;

import com.donaway.dao.donacion.IAnalisisLabDao;
import com.donaway.dao.impl.donacion.AnalisisLabDaoImpl;
import com.donaway.exception.ValidacionException;
import com.donaway.model.donacion.AnalisisLaboratorio;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AnalisisLabServices {

	private IAnalisisLabDao analisisDao = new AnalisisLabDaoImpl();

	////////////////////////////////////////////////////////////////////////////
	// Metodo para registrar los resultados
	
	
	public void registrarResultados(AnalisisLaboratorio analisis) {
		
		if (analisis.getIdDonacion() == null || analisis.getCodigoReferencia() == null) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			if (analisis.getIdAnalisis() == null || analisis.getIdAnalisis().isEmpty()) {
				analisis.setIdAnalisis(UUID.randomUUID().toString());
			}

	
			if (analisisDao.obtenerDonacion(analisis.getIdDonacion()) != null) {
				throw new ValidacionException("Esta donacion ya cuenta con resultados de laboratorio registrados.");
			}

			analisisDao.registrar(analisis);

		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}/////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////
	// Metodo critico: Determina si la sangre es segura o reactiva (peligrosa)
	public boolean esSangreSegura(String idDonacion) {
		
		if (idDonacion == null || idDonacion.isEmpty()) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			// Si esReactivo devuelve true, significa que se detecto alguna enfermedad
			boolean reactivo = analisisDao.esReactivo(idDonacion);
			
			// Retornamos lo opuesto: segura = !reactivo
			return !reactivo;
			
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	////////////////////////////////////////////////////////////////////////////


	////////////////////////////////////////////////////////////////////////////
	// Buscar analisis por codigo de referencia de la bolsa
	public AnalisisLaboratorio buscarPorBolsa(String codigo) {
		try {
			AnalisisLaboratorio a = analisisDao.obtenerPorCodigoReferencia(codigo);
			if (a == null) {
				throw new ValidacionException(Mensaje.ERR_BUSQUEDA);
			}
			return a;
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Historial de analisis de un donante (util para bloqueos permanentes)
	public List<AnalisisLaboratorio> historialPorDonante(String cedula) {
		try {
			return analisisDao.obtenerHistorialDeDonante(cedula);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	////////////////////////////////////////////////////////////////////////////
}
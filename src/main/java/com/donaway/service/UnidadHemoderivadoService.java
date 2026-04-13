package com.donaway.service;

import com.donaway.dao.impl.inventario.UnidadHemoderivadoDaoImpl;
import com.donaway.dao.inventario.IUnidadHemoderivadoDao;
import com.donaway.exception.ValidacionException;
import com.donaway.model.inventario.UnidadHemoderivados;
import com.donaway.utils.constants.EstadoUnidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoSangre;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class UnidadHemoderivadoService {

	private IUnidadHemoderivadoDao unidadDao = new UnidadHemoderivadoDaoImpl();

	////////////////////////////////////////////////////////////////////////////
	/// 
	// Metodo para ingresar una unidad al inventario
	public void ingresarAlInventario(UnidadHemoderivados unidad) {
		
		// 1. Validacion de datos basicos
		if (unidad.getIdDonacion() == null || unidad.getCodigoBarras() == null || 
			unidad.getRnc() == null || unidad.getFechaCaducidad() == null) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		//No permitir ingreso de sangre ya vencida
		if (unidad.getFechaCaducidad().isBefore(LocalDate.now())) {
			throw new ValidacionException("No se puede registrar una unidad que ya ha caducado");
		}

		try {

			if (unidad.getIdHemoderivados() == null || unidad.getIdHemoderivados().isEmpty()) {
				unidad.setIdHemoderivados(UUID.randomUUID().toString());
			}

			
			if (unidadDao.obtenerPorCodigoBarras(unidad.getCodigoBarras()) != null) {
				throw new ValidacionException("Este codigo de barras ya esta registrado en el inventario");
			}

			
			unidadDao.registrar(unidad);

		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	// Metodo para buscar disponibilidad real (Stock apto para uso)
	
	public List<UnidadHemoderivados> consultarDisponibilidad(TipoComponente tipo, TipoSangre sangre, FactorRh rh) {
		try {
		
			return unidadDao.buscarDisponibles(tipo, sangre, rh);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////


	// Metodo para dar de baja a una unidad (Uso, Descarte o Vencimiento)
	
	public void actualizarEstado(String idUnidad, EstadoUnidad nuevoEstado) {
		if (idUnidad == null || idUnidad.isEmpty()) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			unidadDao.cambiarEstado(idUnidad, nuevoEstado);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	// Sistema de Alerta: Unidades proximas a vencer
	
	public List<UnidadHemoderivados> alertasVencimiento(int diasMargen) {
		try {
			// Retorna unidades disponibles que vencen en los proximos X dias
			return unidadDao.obtenerUnidadesPorVencer(diasMargen);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////

	public UnidadHemoderivados buscarPorCodigoBarras(String codigo) {
		try {
			UnidadHemoderivados u = unidadDao.obtenerPorCodigoBarras(codigo);
			if (u == null) throw new ValidacionException(Mensaje.ERR_BUSQUEDA);
			return u;
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
}
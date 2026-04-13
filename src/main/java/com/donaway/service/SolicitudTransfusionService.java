package com.donaway.service;

import com.donaway.dao.impl.transfusion.SolicitudTransfusionDaoImpl;
import com.donaway.dao.transfusion.ISolicitudTransfusionDao;
import com.donaway.exception.ValidacionException;
import com.donaway.model.transfusion.SolicitudTransfusion;
import com.donaway.utils.constants.EstadoSolicitud;
import com.donaway.utils.constants.PrioridadSolicitud;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class SolicitudTransfusionService {

	private ISolicitudTransfusionDao solicitudDao = new SolicitudTransfusionDaoImpl();

	////////////////////////////////////////////////////////////////////////////
	
	// Metodo para registrar una nueva solicitud de sangre
	
	public void crearSolicitud(SolicitudTransfusion solicitud) {
		
		// 1. Validacion de datos esenciales
		if (solicitud.getDocumentIdReceptor() == null || solicitud.getRnc() == null ||
			solicitud.getCantidadSolicitada() <= 0 || solicitud.getComponentes() == null) {
			
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			
			if (solicitud.getIdSolicitud() == null || solicitud.getIdSolicitud().isEmpty()) {
				solicitud.setIdSolicitud(UUID.randomUUID().toString());
			}
			
			if (solicitud.getFechaSolicitud() == null) {
				solicitud.setFechaSolicitud(LocalDateTime.now());
			}
			
			if (solicitud.getEstadoSolicitudObj() == null) {
				solicitud.setEstadoSolicitudObj(EstadoSolicitud.PENDIENTE);
			}

			
			solicitudDao.registrar(solicitud);

		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	// Metodo para gestionar el triaje de solicitudes (Prioridad)
	
	public List<SolicitudTransfusion> verPendientesPorPrioridad(PrioridadSolicitud prioridad) {
		try {
			// El DAO ya ordena por fecha para respetar el orden de llegada dentro de la prioridad
			return solicitudDao.obtenerPendientes(prioridad);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	// Metodo para finalizar o cancelar una solicitud
	
	public void actualizarEstadoSolicitud(String idSolicitud, EstadoSolicitud nuevoEstado) {
		
		if (idSolicitud == null || idSolicitud.isEmpty()) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		try {
			
			solicitudDao.actualizarEstado(idSolicitud, nuevoEstado);
			
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	
	
	////////////////////////////////////////////////////////////////////////////


	// Busqueda especifica para seguimiento de instituciones
	public List<SolicitudTransfusion> historialPorInstitucion(String rnc) {
		if (rnc == null || rnc.isEmpty()) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}
		
		try {
			return solicitudDao.obtenerPorInstitucion(rnc);
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	////////////////////////////////////////////////////////////////////////////

	public SolicitudTransfusion buscarSolicitud(String id) {
		try {
			SolicitudTransfusion s = solicitudDao.obtenerPorId(id);
			if (s == null) throw new ValidacionException(Mensaje.ERR_BUSQUEDA);
			return s;
		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
}
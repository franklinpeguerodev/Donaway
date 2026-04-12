package com.donaway.dao.transfusion;

import java.sql.SQLException;
import java.util.List;

import com.donaway.model.transfusion.SolicitudTransfusion;
import com.donaway.model.transfusion.Transfusion;
import com.donaway.utils.constants.EstadoSolicitud;
import com.donaway.utils.constants.PrioridadSolicitud;

public interface ISolicitudTransfusionDao {

	
	//escritura
	void registrar(SolicitudTransfusion solicitud) throws SQLException;
	void actualizarEstado(String idSolicitud, EstadoSolicitud nuevoEstado) throws SQLException;
	
	
	//busqueda y filtro
	List<SolicitudTransfusion> obtenerPendientes(PrioridadSolicitud prioridad) throws SQLException;
	List<SolicitudTransfusion> obtenerPorInstitucion(String rnc) throws SQLException;
    SolicitudTransfusion obtenerPorId(String idSolicitud) throws SQLException;
    List<SolicitudTransfusion> obtenerHistorial() throws SQLException;
}

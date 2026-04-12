package com.donaway.dao.donacion;

import java.sql.SQLException;
import java.util.List;

import com.donaway.model.donacion.DatosTriaje;

public interface IDatosTriajeDao {

	
	//escritura
	void registrar(DatosTriaje triaje) throws SQLException;
	
	
	//buscar y filtrar
	List<DatosTriaje> obtenerHistorial() throws SQLException;
	DatosTriaje obtenerPorId(String idDonacion) throws SQLException;
	DatosTriaje obtenerPorCodigoReferencia (String codigoReferencia) throws SQLException;
	DatosTriaje obtenerDonacion (String idDonacion) throws SQLException;
	List<DatosTriaje> obtenerHistorialDeDonante (String documentoId) throws SQLException;
}

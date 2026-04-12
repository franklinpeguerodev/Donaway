package com.donaway.dao.donacion;

import java.sql.SQLException;
import java.util.List;

import com.donaway.model.donacion.AnalisisLaboratorio;

public interface IAnalisisLabDao {

		//escritura
		void registrar(AnalisisLaboratorio analisi) throws SQLException;
		
		
		//buscar y filtrar
		List<AnalisisLaboratorio> obtenerHistorial() throws SQLException;
		AnalisisLaboratorio obtenerPorId(String idDonacion) throws SQLException;
		AnalisisLaboratorio obtenerPorCodigoReferencia (String codigoReferencia) throws SQLException;
		AnalisisLaboratorio obtenerDonacion (String idDonacion) throws SQLException;
		List<AnalisisLaboratorio> obtenerHistorialDeDonante (String documentoId) throws SQLException;
	
		//metodo de exclusion
		boolean esReactivo (String idDonacion) throws SQLException;
}

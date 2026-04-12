package com.donaway.dao.transfusion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.donaway.model.transfusion.Transfusion;

public interface ITransfusionDao {

	//escritura
	void registrar(Transfusion transfusion) throws SQLException;

	Transfusion obtenerPorCodigoBarras(String codigoBarras) throws SQLException;
	List<Transfusion> obtenerPorReceptor(String documentoId) throws SQLException;
	Transfusion obtenerPorUnidad(String codigoBarras) throws SQLException;
    
    // Historial del paciente
    List<Transfusion> obtenerHistorialReceptor(String documentIdReceptor) throws SQLException;
	
    List<Transfusion> obtenerPorInstitucion(String rnc, LocalDate inicio, LocalDate fin) throws SQLException;
}

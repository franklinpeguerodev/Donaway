package com.donaway.dao.transfusion;

import java.sql.SQLException;
import java.util.List;

import com.donaway.model.transfusion.Transfusion;

public interface ITransfusionDao {

	//escritura
	void registrar(Transfusion transfusion) throws SQLException;

	List<Transfusion> obtenerPorReceptor(String documentoId) throws SQLException;

	Transfusion obtenerPorUnidad(String codigoBarras) throws SQLException;
}

package com.donaway.dao.donacion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.donaway.model.donacion.Donacion;

public interface IDonacionDao {

	//escritura
	void registrar(Donacion dona) throws SQLException;
	
	
	//buscar y filtrar
	List<Donacion> obtenerHistorial() throws SQLException;
	Donacion obtenerPorId(String idDonacion) throws SQLException;
	Donacion obtenerPorCodigoReferencia (String codigoReferencia) throws SQLException;
	List<Donacion> obtenerHistorialDeDonante (String documentoId) throws SQLException;
	List<Donacion> obtenerPorFecha(LocalDate fechaDonacion) throws SQLException;
	
	String obtenerDocumentIdDonantePorDonacion (String idDonacion) throws SQLException;
	
}

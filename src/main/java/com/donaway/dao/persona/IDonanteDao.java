package com.donaway.dao.persona;

import java.sql.SQLException;


import java.util.List;

import com.donaway.model.persona.Donante;
import com.donaway.utils.constants.EstadoDisponibilidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.TipoSangre;

public interface IDonanteDao {
	
	//escritura
	void registrarDonante(Donante donante) throws SQLException;
	void modificarDonante (Donante donante) throws SQLException;
	void eliminarDonante (String documentoId) throws SQLException;
	void actualizarEstado (String documentoId, EstadoDisponibilidad nuevoEstado) throws SQLException;
	
	
	//lectura
	Donante obtenerPorId(String documentId) throws SQLException;
	List<Donante> obtenerTodo() throws SQLException;
	
	
	//Busqueda especifica
	List<Donante> obtenerPorSangre(TipoSangre tipo, FactorRh factor) throws SQLException;
	List<Donante> obtenerPorEstado(EstadoDisponibilidad estado) throws SQLException;
	
}

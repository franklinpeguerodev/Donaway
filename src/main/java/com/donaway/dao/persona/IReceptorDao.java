package com.donaway.dao.persona;

import java.sql.SQLException;
import java.util.List;

import com.donaway.model.persona.Donante;
import com.donaway.model.persona.Receptor;

public interface IReceptorDao {
	
	//escritura
	void registrarReceptor(Receptor receptor) throws SQLException;
	void modificarReceptor(Receptor receptor) throws SQLException;
		
	//Busque y filtrado
	List<Donante> obtenerTodo() throws SQLException;
	List<Receptor> buscarPorNombre(String nombre) throws SQLException;
	Receptor obtenerPorId(String documentId) throws SQLException; 

}

package com.donaway.dao.donacion;

import java.sql.SQLException;


import java.util.List;

import com.donaway.model.persona.Donante;




public interface IDonanteDao {
	
	void registrarDonante(Donante donante) throws SQLException;
	
	void eliminarDonante (String documentoId);
	
	void modificarDonante (Donante donante);
	
	List<Donante> obtenerTodo();
	

}

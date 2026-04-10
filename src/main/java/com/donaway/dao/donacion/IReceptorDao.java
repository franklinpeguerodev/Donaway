package com.donaway.dao.donacion;



import java.sql.SQLException;

import com.donaway.model.persona.Receptor;



public interface IReceptorDao {
	void registrarReceptorDonante(Receptor receptor) throws SQLException;
		
	
}

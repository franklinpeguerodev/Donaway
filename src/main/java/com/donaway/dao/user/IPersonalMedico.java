package com.donaway.dao.user;

import com.donaway.model.user.PersonalMedico;
import java.sql.SQLException;
import java.util.List;

public interface IPersonalMedico {
	
	
	//estritura
	void registrarPersonalMedico (PersonalMedico personalmedico) throws SQLException;
	void modificarPersonalMedico (PersonalMedico personalmedico) throws SQLException;
	void actualizarUltimoAccesoPersonalMedico (String docuemntId) throws SQLException;

	
	//busqueda
	PersonalMedico obtenerPorId(String docuemntId) throws SQLException;
	List<PersonalMedico> obtenerInstitucion(String rnc) throws SQLException;

	
	//validacion
	boolean validarCredencuiales(String email, String password) throws SQLException;

}

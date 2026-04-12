package com.donaway.dao.user;

import com.donaway.model.user.PersonalMedico;
import com.donaway.utils.constants.Departamento;
import com.donaway.utils.constants.Jornada;

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
	List<PersonalMedico> obtenerPorDepartamento(Departamento depto) throws SQLException;
    List<PersonalMedico> obtenerPorJornada(Jornada jornada) throws SQLException;
    List<PersonalMedico> obtenerActivos() throws SQLException;
    
    // Para el login y auditoría
    PersonalMedico obtenerPorCodigoCarnet(String codigo) throws SQLException;
    PersonalMedico devolverPorCarnet(String carnet) throws SQLException;
	
	//validacion
	boolean validarCredenciales(String email, String password) throws SQLException;

}

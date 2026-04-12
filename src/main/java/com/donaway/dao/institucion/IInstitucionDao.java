package com.donaway.dao.institucion;

import com.donaway.model.institucion.Institucion;
import java.sql.SQLException;
import java.util.List;


public interface IInstitucionDao {
	
	//escritura
	void registrarInstitucion(Institucion institucion) throws SQLException;
	void modificarInstitucion(Institucion institucion) throws SQLException;
	
	//busqueda
	Institucion obtenerPorRnc(String rnc) throws SQLException;
	List<Institucion> obtenerInstituciones () throws SQLException;
	
}

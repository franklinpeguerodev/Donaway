package com.donaway.dao.institucion;
import com.donaway.model.institucion.Institucion;
import java.sql.SQLException;
public interface IInstitucionDao {
	void registrarInstitucion(Institucion institucion) throws SQLException;
}

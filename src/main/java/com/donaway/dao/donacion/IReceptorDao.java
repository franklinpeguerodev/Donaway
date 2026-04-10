package com.donaway.dao.donacion;
import com.donaway.model.donacion.Receptor;
import java.sql.SQLException;
public interface IReceptorDao {
	void registrarReceptorDonante(Receptor receptor) throws SQLException;
		
	
}

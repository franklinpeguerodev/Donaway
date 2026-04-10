package com.donaway.dao.donacion;
import com.donaway.model.donacion.Donante;
import java.util.List;
import java.sql.SQLException;
public interface IDonanteDao {
	
	void registrarDonante(Donante donante) throws SQLException;
	
	void eliminarDonante (String documentoId);
	
	void modificarDonante (Donante donante);
	
	List<Donante> obtenerTodo();
	

}

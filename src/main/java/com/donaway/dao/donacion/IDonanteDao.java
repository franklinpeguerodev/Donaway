package com.donaway.dao.donacion;
import com.donaway.model.donacion.Donante;
import java.util.List;
public interface IDonanteDao {
	void registrarDonante(Donante donante);
	
	void eliminarDonante (String documentoId);
	
	void modificarDonante (Donante donante);
	
	List<Donante> obtenerTodo();
	

}

package com.donaway.dao.inventario;

import java.sql.SQLException;
import java.util.List;

import com.donaway.model.inventario.UnidadHemoderivados;
import com.donaway.utils.constants.EstadoUnidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoSangre;

public interface IUnidadHemoderivadoDao {

	//escritura
	void registrar(UnidadHemoderivados unidad) throws SQLException;
	void cambiarEstado(String idUnidad, EstadoUnidad nuevoEstado) throws SQLException;
	
	
	//busqueda y filtro
	UnidadHemoderivados obtenerPorCodigoBarras(String codigoBarras) throws SQLException;
	List<UnidadHemoderivados> buscarStock(TipoComponente tipo, TipoSangre sangre, FactorRh rh) throws SQLException;
	
}

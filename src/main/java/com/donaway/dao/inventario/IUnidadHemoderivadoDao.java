package com.donaway.dao.inventario;

import java.sql.SQLException;
import java.util.List;
import com.donaway.model.inventario.UnidadHemoderivados;
import com.donaway.utils.constants.EstadoUnidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoSangre;

public interface IUnidadHemoderivadoDao {

    // Escritura
    void registrar(UnidadHemoderivados unidad) throws SQLException;
    void cambiarEstado(String idUnidad, EstadoUnidad nuevoEstado) throws SQLException;
    
    // Búsqueda y Filtro General
    UnidadHemoderivados obtenerPorCodigoBarras(String codigoBarras) throws SQLException;
    UnidadHemoderivados obtenerPorDonacion(String idDonacion) throws SQLException;
    List<UnidadHemoderivados> buscarStock(TipoComponente tipo, TipoSangre sangre, FactorRh rh) throws SQLException;
    
    // Búsqueda operativa (Solo DISPONIBLES y NO VENCIDAS)
    List<UnidadHemoderivados> buscarDisponibles(TipoComponente tipo, TipoSangre sangre, FactorRh rh) throws SQLException;
    
    // Control de caducidad
    List<UnidadHemoderivados> obtenerUnidadesPorVencer(int dias) throws SQLException;
}
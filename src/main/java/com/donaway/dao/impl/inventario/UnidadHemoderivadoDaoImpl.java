package com.donaway.dao.impl.inventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.donaway.dao.inventario.IUnidadHemoderivadoDao;
import com.donaway.model.inventario.UnidadHemoderivados;
import com.donaway.utils.constants.EstadoUnidad;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoSangre;
import com.donaway.utils.db.ConexionDbDonaway;

public class UnidadHemoderivadoDaoImpl implements IUnidadHemoderivadoDao {

    @Override
    public void registrar(UnidadHemoderivados unidad) throws SQLException {
        String sql = "INSERT INTO unidad_hemoderivados (id_unidad, id_donacion, codigo_barras, tipo_componente, "
                   + "tipo_sangre, factor_rh, fecha_vencimiento, estado_unidad) "
                   + "VALUES (UUID_TO_BIN(?,1), UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, unidad.getIdHemoderivados());
            ps.setString(2, unidad.getIdDonacion());
            ps.setString(3, unidad.getCodigoBarras());
            ps.setString(4, unidad.getTipoComponenteObj().name());
            ps.setString(5, unidad.getTipoSangreObj().name());
            ps.setString(6, unidad.getFactorRhObj().name());
            ps.setDate(7, Date.valueOf(unidad.getFechaCaducidad()));
            ps.setString(8, unidad.getEstadoUnidadObj().name());

            ps.executeUpdate();
        }
    }

    @Override
    public void cambiarEstado(String idUnidad, EstadoUnidad nuevoEstado) throws SQLException {
        String sql = "UPDATE unidad_hemoderivados SET estado_unidad = ? WHERE id_unidad = UUID_TO_BIN(?,1)";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, idUnidad);
            ps.executeUpdate();
        }
    }

    @Override
    public UnidadHemoderivados obtenerPorCodigoBarras(String codigoBarras) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_unidad,1) as id_uni, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
                   + "FROM unidad_hemoderivados u WHERE codigo_barras = ?";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoBarras);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUnidad(rs);
            }
        }
        return null;
    }

    @Override
    public UnidadHemoderivados obtenerPorDonacion(String idDonacion) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_unidad,1) as id_uni, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
                   + "FROM unidad_hemoderivados u WHERE id_donacion = UUID_TO_BIN(?,1)";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearUnidad(rs);
            }
        }
        return null;
    }

    @Override
    public List<UnidadHemoderivados> buscarStock(TipoComponente tipo, TipoSangre sangre, FactorRh rh) throws SQLException {
        List<UnidadHemoderivados> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_unidad,1) as id_uni, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
                   + "FROM unidad_hemoderivados u WHERE tipo_componente = ? AND tipo_sangre = ? AND factor_rh = ?";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ps.setString(2, sangre.name());
            ps.setString(3, rh.name());
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearUnidad(rs));
            }
        }
        return lista;
    }

    @Override
    public List<UnidadHemoderivados> buscarDisponibles(TipoComponente tipo, TipoSangre sangre, FactorRh rh) throws SQLException {
        List<UnidadHemoderivados> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_unidad,1) as id_uni, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
                   + "FROM unidad_hemoderivados u WHERE tipo_componente = ? AND tipo_sangre = ? AND factor_rh = ? "
                   + "AND estado_unidad = 'DISPONIBLE' AND fecha_vencimiento >= CURRENT_DATE";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ps.setString(2, sangre.name());
            ps.setString(3, rh.name());
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearUnidad(rs));
            }
        }
        return lista;
    }

    @Override
    public List<UnidadHemoderivados> obtenerUnidadesPorVencer(int dias) throws SQLException {
        List<UnidadHemoderivados> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_unidad,1) as id_uni, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
                   + "FROM unidad_hemoderivados u WHERE fecha_vencimiento <= DATE_ADD(CURRENT_DATE, INTERVAL ? DAY) "
                   + "AND estado_unidad = 'DISPONIBLE'";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, dias);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearUnidad(rs));
            }
        }
        return lista;
    }

    // Método de mapeo centralizado para evitar código repetido
    private UnidadHemoderivados mapearUnidad(ResultSet rs) throws SQLException {
        UnidadHemoderivados unidad = new UnidadHemoderivados();
        unidad.setIdHemoderivados(rs.getString("id_uni"));
        unidad.setIdDonacion(rs.getString("id_dona"));
        unidad.setCodigoBarras(rs.getString("codigo_barras"));
        unidad.setTipoComponenteObj(TipoComponente.valueOf(rs.getString("tipo_componente")));
        unidad.setTipoSangreObj(TipoSangre.valueOf(rs.getString("tipo_sangre")));
        unidad.setFactorRhObj(FactorRh.valueOf(rs.getString("factor_rh")));
        unidad.setFechaCaducidad(rs.getDate("fecha_vencimiento").toLocalDate());
        unidad.setEstadoUnidadObj(EstadoUnidad.valueOf(rs.getString("estado_unidad")));
        return unidad;
    }
}

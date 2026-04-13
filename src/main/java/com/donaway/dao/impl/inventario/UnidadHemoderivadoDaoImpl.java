package com.donaway.dao.impl.inventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.donaway.dao.inventario.IUnidadHemoderivadoDao;
import com.donaway.model.inventario.UnidadHemoderivados;
import com.donaway.utils.constants.*;
import com.donaway.utils.db.ConexionDbDonaway;

public class UnidadHemoderivadoDaoImpl implements IUnidadHemoderivadoDao {

    @Override
    public void registrar(UnidadHemoderivados unidad) throws SQLException {
        // Incluimos cantidad_pintas y rnc_institucion en el registro
        String sql = "INSERT INTO unidad_hemoderivados (id_hemorivada, id_donacion, rnc_institucion, codigo_barras, "
                   + "codigo_referencia, tipo_componente, total_sangre, tipo_sangre, factor_rh, "
                   + "estado_unidad, fecha_vencimiento) "
                   + "VALUES (UUID_TO_BIN(?,1), UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, unidad.getIdHemoderivados());
            ps.setString(2, unidad.getIdDonacion());
            ps.setString(3, unidad.getRnc());
            ps.setString(4, unidad.getCodigoBarras());
            ps.setString(5, unidad.getCodigoReferencia());
            ps.setString(6, unidad.getTipoComponenteObj().name());
            ps.setDouble(7, unidad.getCantidadTotalPintas()); // Nueva lógica de cantidad
            ps.setString(8, unidad.getTipoSangreObj().name());
            ps.setString(9, unidad.getFactorRhObj().name());
            ps.setString(10, unidad.getEstadoUnidadObj().name());
            ps.setDate(11, Date.valueOf(unidad.getFechaCaducidad()));

            ps.executeUpdate();
        }
    }

    @Override
    public void cambiarEstado(String idUnidad, EstadoUnidad nuevoEstado) throws SQLException {
        String sql = "UPDATE unidad_hemoderivados SET estado_unidad = ? WHERE id_hemorivada = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, idUnidad);
            ps.executeUpdate();
        }
    }

    @Override
    public UnidadHemoderivados obtenerPorCodigoBarras(String codigoBarras) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_hemorivada,1) as id_sangre, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
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
    public List<UnidadHemoderivados> buscarStock(TipoComponente tipo, TipoSangre sangre, FactorRh rh) throws SQLException {
        List<UnidadHemoderivados> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_hemorivada,1) as id_sangre, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
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
        String sql = "SELECT BIN_TO_UUID(id_hemorivada,1) as id_sangre, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
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
        String sql = "SELECT BIN_TO_UUID(id_hemorivada,1) as id_sangre, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
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

    @Override
    public UnidadHemoderivados obtenerPorDonacion(String idDonacion) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_hemorivada,1) as id_sangre, BIN_TO_UUID(id_donacion,1) as id_dona, u.* "
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

    private UnidadHemoderivados mapearUnidad(ResultSet rs) throws SQLException {
        UnidadHemoderivados u = new UnidadHemoderivados();
        u.setIdHemoderivados(rs.getString("id_sangre"));
        u.setIdDonacion(rs.getString("id_dona"));
        u.setRnc(rs.getString("rnc_institucion"));
        u.setCodigoBarras(rs.getString("codigo_barras"));
        u.setCodigoReferencia(rs.getString("codigo_referencia"));
        u.setTipoComponenteObj(TipoComponente.valueOf(rs.getString("tipo_componente")));
        u.setCantidadTotalPintas(rs.getDouble("total_sangre")); // Mapeo de la cantidad
        u.setTipoSangreObj(TipoSangre.valueOf(rs.getString("tipo_sangre")));
        u.setFactorRhObj(FactorRh.valueOf(rs.getString("factor_rh")));
        u.setEstadoUnidadObj(EstadoUnidad.valueOf(rs.getString("estado_unidad")));
        u.setFechaCaducidad(rs.getDate("fecha_vencimiento").toLocalDate());
        return u;
    }
}
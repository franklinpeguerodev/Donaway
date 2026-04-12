package com.donaway.dao.impl.transfusion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.donaway.dao.transfusion.ITransfusionDao;
import com.donaway.model.transfusion.Transfusion;
import com.donaway.utils.constants.TipoTransfusion;
import com.donaway.utils.db.ConexionDbDonaway;

public class TransfusionDaoImpl implements ITransfusionDao {

    @Override
    public void registrar(Transfusion t) throws SQLException {
        // Usamos una transacción para mantener la integridad del inventario
        Connection conn = null;
        String sqlTransfusion = "INSERT INTO transfusion (id_transfusion, id_solicitud, rnc_institucion, "
                              + "codigo_barras, tipo_transfusion, cantidad_administrada, fecha_transfusion) "
                              + "VALUES (UUID_TO_BIN(?,1), UUID_TO_BIN(?,1), ?, ?, ?, ?, ?)";
        
        String sqlUpdateUnidad = "UPDATE unidad_hemoderivados SET estado_unidad = 'USADA' WHERE codigo_barras = ?";
        String sqlUpdateSolicitud = "UPDATE solicitud_transfusion SET estado_solicitud = 'COMPLETADA', fecha_entrega = ? "
                                  + "WHERE id_solicitud = UUID_TO_BIN(?,1)";

        try {
            conn = ConexionDbDonaway.getConnection();
            conn.setAutoCommit(false); // Iniciamos transacción

            // 1. Registrar la transfusión
            try (PreparedStatement ps = conn.prepareStatement(sqlTransfusion)) {
                ps.setString(1, t.getIdTransfusion());
                ps.setString(2, t.getIdSolicitud());
                ps.setString(3, t.getRnc());
                ps.setString(4, t.getCodigoBarras());
                ps.setString(5, t.getTipoTransfusionObj().name());
                ps.setDouble(6, t.getCantidadAdministrada());
                ps.setTimestamp(7, Timestamp.valueOf(t.getFechaTransfusion()));
                ps.executeUpdate();
            }

            // 2. Marcar unidad como USADA
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateUnidad)) {
                ps.setString(1, t.getCodigoBarras());
                ps.executeUpdate();
            }

            // 3. Marcar solicitud como COMPLETADA
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateSolicitud)) {
                ps.setTimestamp(1, Timestamp.valueOf(t.getFechaTransfusion()));
                ps.setString(2, t.getIdSolicitud());
                ps.executeUpdate();
            }

            conn.commit(); // Todo bien, guardamos cambios
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) conn.close();
        }
    }

    @Override
    public Transfusion obtenerPorCodigoBarras(String codigoBarras) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_transfusion,1) as id_t, BIN_TO_UUID(id_solicitud,1) as id_s, t.* "
                   + "FROM transfusion t WHERE codigo_barras = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoBarras);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearTransfusion(rs);
            }
        }
        return null;
    }

    @Override
    public List<Transfusion> obtenerPorReceptor(String documentoId) throws SQLException {
        // Join con la solicitud para filtrar por el receptor
        String sql = "SELECT BIN_TO_UUID(t.id_transfusion,1) as id_t, BIN_TO_UUID(t.id_solicitud,1) as id_s, t.* "
                   + "FROM transfusion t "
                   + "INNER JOIN solicitud_transfusion s ON t.id_solicitud = s.id_solicitud "
                   + "WHERE s.document_id_receptor = ?";
        List<Transfusion> lista = new ArrayList<>();
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearTransfusion(rs));
            }
        }
        return lista;
    }

    @Override
    public Transfusion obtenerPorUnidad(String codigoBarras) throws SQLException {
        return obtenerPorCodigoBarras(codigoBarras);
    }

    @Override
    public List<Transfusion> obtenerHistorialReceptor(String documentIdReceptor) throws SQLException {
        return obtenerPorReceptor(documentIdReceptor);
    }

    @Override
    public List<Transfusion> obtenerPorInstitucion(String rnc, LocalDate inicio, LocalDate fin) throws SQLException {
        List<Transfusion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_transfusion,1) as id_t, BIN_TO_UUID(id_solicitud,1) as id_s, t.* "
                   + "FROM transfusion t WHERE rnc_institucion = ? AND DATE(fecha_transfusion) BETWEEN ? AND ?";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rnc);
            ps.setDate(2, Date.valueOf(inicio));
            ps.setDate(3, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearTransfusion(rs));
            }
        }
        return lista;
    }

    private Transfusion mapearTransfusion(ResultSet rs) throws SQLException {
        Transfusion t = new Transfusion();
        t.setIdTransfusion(rs.getString("id_t"));
        t.setIdSolicitud(rs.getString("id_s"));
        t.setRnc(rs.getString("rnc_institucion"));
        t.setCodigoBarras(rs.getString("codigo_barras"));
        t.setTipoTransfusionObj(TipoTransfusion.valueOf(rs.getString("tipo_transfusion")));
        t.setCantidadAdministrada(rs.getDouble("cantidad_administrada"));
        t.setFechaTransfusion(rs.getTimestamp("fecha_transfusion").toLocalDateTime());
        return t;
    }
}
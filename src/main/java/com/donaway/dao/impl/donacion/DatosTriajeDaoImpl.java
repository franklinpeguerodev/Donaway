package com.donaway.dao.impl.donacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.donaway.dao.donacion.IDatosTriajeDao;
import com.donaway.model.donacion.DatosTriaje;
import com.donaway.utils.db.ConexionDbDonaway;

public class DatosTriajeDaoImpl implements IDatosTriajeDao {

    @Override
    public void registrar(DatosTriaje t) throws SQLException {
        String sql = "INSERT INTO datos_triaje (id_triaje, id_donacion, codigo_referencia, peso, presion_sistolica, "
                   + "presion_diastolica, pulso, temperatura_celsius, hemoglobina) "
                   + "VALUES (UUID_TO_BIN(?,1), UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getIdTriaje());
            ps.setString(2, t.getIdDonacion());
            ps.setString(3, t.getCodigoReferencia());
            ps.setDouble(4, t.getPesoLbs());
            ps.setInt(5, t.getPresionSistolica());
            ps.setInt(6, t.getPresionDiastolica());
            ps.setInt(7, t.getPulso());
            ps.setDouble(8, t.getTemperatura());
            ps.setDouble(9, t.getHemoglobina());
            ps.executeUpdate();
        }
    }

    @Override
    public List<DatosTriaje> obtenerHistorial() throws SQLException {
        List<DatosTriaje> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_triaje,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, t.* "
                   + "FROM datos_triaje t ORDER BY id_triaje DESC";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearTriaje(rs));
        }
        return lista;
    }

    @Override
    public DatosTriaje obtenerPorId(String idTriaje) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_triaje,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, t.* "
                   + "FROM datos_triaje t WHERE id_triaje = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idTriaje);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearTriaje(rs);
            }
        }
        return null;
    }

    @Override
    public DatosTriaje obtenerPorCodigoReferencia(String codigo) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_triaje,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, t.* "
                   + "FROM datos_triaje t WHERE codigo_referencia = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearTriaje(rs);
            }
        }
        return null;
    }

    @Override
    public DatosTriaje obtenerDonacion(String idDonacion) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_triaje,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, t.* "
                   + "FROM datos_triaje t WHERE id_donacion = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearTriaje(rs);
            }
        }
        return null;
    }

    @Override
    public List<DatosTriaje> obtenerHistorialDeDonante(String documentoId) throws SQLException {
        List<DatosTriaje> lista = new ArrayList<>();
        // Unimos con la tabla donación para filtrar por el documento del donante
        String sql = "SELECT BIN_TO_UUID(t.id_triaje,1) as uuid, BIN_TO_UUID(t.id_donacion,1) as dona_uuid, t.* "
                   + "FROM datos_triaje t "
                   + "INNER JOIN donacion d ON t.id_donacion = d.id_donacion "
                   + "WHERE d.document_id_donante = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearTriaje(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean esAptoParaDonar(String idDonacion) throws SQLException {
        String sql = "SELECT id_triaje FROM datos_triaje WHERE id_donacion = UUID_TO_BIN(?,1) "
                   + "AND (presion_sistolica BETWEEN 90 AND 180) AND (presion_diastolica BETWEEN 50 AND 100) "
                   + "AND (pulso BETWEEN 50 AND 100) AND (temperatura_celsius BETWEEN 36.0 AND 37.5) "
                   + "AND (hemoglobina >= 12.5)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private DatosTriaje mapearTriaje(ResultSet rs) throws SQLException {
        return new DatosTriaje(
            rs.getString("uuid"), rs.getString("dona_uuid"), rs.getString("codigo_referencia"),
            rs.getDouble("peso"), rs.getInt("presion_sistolica"), rs.getInt("presion_diastolica"),
            rs.getInt("pulso"), rs.getDouble("temperatura_celsius"), rs.getDouble("hemoglobina")
        );
    }
}
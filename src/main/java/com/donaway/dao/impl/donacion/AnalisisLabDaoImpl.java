package com.donaway.dao.impl.donacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.donaway.dao.donacion.IAnalisisLabDao;
import com.donaway.model.donacion.AnalisisLaboratorio;
import com.donaway.utils.db.ConexionDbDonaway;

public class AnalisisLabDaoImpl implements IAnalisisLabDao {

    @Override
    public void registrar(AnalisisLaboratorio a) throws SQLException {
        String sql = "INSERT INTO analisis_lab (id_analisis, id_donacion, codigo_referencia, vih, hepatitis_b, "
                   + "hepatitis_c, sifilis, chacas, nat, otros) VALUES (UUID_TO_BIN(?,1), UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getIdAnalisis());
            ps.setString(2, a.getIdDonacion());
            ps.setString(3, a.getCodigoReferencia());
            ps.setBoolean(4, a.isVih());
            ps.setBoolean(5, a.isHepatitisB());
            ps.setBoolean(6, a.isHepatitisC());
            ps.setBoolean(7, a.isSifilis());
            ps.setBoolean(8, a.isChagas());
            ps.setBoolean(9, a.isNat());
            ps.setString(10, a.getOtros());
            ps.executeUpdate();
        }
    }

    @Override
    public List<AnalisisLaboratorio> obtenerHistorial() throws SQLException {
        List<AnalisisLaboratorio> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_analisis,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, a.* FROM analisis_lab a";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearAnalisis(rs));
        }
        return lista;
    }

    @Override
    public AnalisisLaboratorio obtenerPorId(String idAnalisis) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_analisis,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, a.* "
                   + "FROM analisis_lab a WHERE id_analisis = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idAnalisis);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearAnalisis(rs);
            }
        }
        return null;
    }

    @Override
    public AnalisisLaboratorio obtenerPorCodigoReferencia(String codigo) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_analisis,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, a.* "
                   + "FROM analisis_lab a WHERE codigo_referencia = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearAnalisis(rs);
            }
        }
        return null;
    }

    @Override
    public AnalisisLaboratorio obtenerDonacion(String idDonacion) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_analisis,1) as uuid, BIN_TO_UUID(id_donacion,1) as dona_uuid, a.* "
                   + "FROM analisis_lab a WHERE id_donacion = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearAnalisis(rs);
            }
        }
        return null;
    }

    @Override
    public List<AnalisisLaboratorio> obtenerHistorialDeDonante(String documentoId) throws SQLException {
        List<AnalisisLaboratorio> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(a.id_analisis,1) as uuid, BIN_TO_UUID(a.id_donacion,1) as dona_uuid, a.* "
                   + "FROM analisis_lab a "
                   + "INNER JOIN donacion d ON a.id_donacion = d.id_donacion "
                   + "WHERE d.document_id_donante = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearAnalisis(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean esReactivo(String idDonacion) throws SQLException {
        String sql = "SELECT id_analisis FROM analisis_lab WHERE id_donacion = UUID_TO_BIN(?,1) "
                   + "AND (vih = 1 OR hepatitis_b = 1 OR hepatitis_c = 1 OR sifilis = 1 OR chacas = 1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private AnalisisLaboratorio mapearAnalisis(ResultSet rs) throws SQLException {
        return new AnalisisLaboratorio(
            rs.getString("uuid"), rs.getString("dona_uuid"), rs.getString("codigo_referencia"),
            rs.getBoolean("vih"), rs.getBoolean("hepatitis_b"), rs.getBoolean("hepatitis_c"),
            rs.getBoolean("sifilis"), rs.getBoolean("chacas"), rs.getBoolean("nat"), rs.getString("otros")
        );
    }
}

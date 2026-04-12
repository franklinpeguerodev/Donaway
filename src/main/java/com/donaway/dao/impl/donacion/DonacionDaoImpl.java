package com.donaway.dao.impl.donacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.donaway.dao.donacion.IDonacionDao;
import com.donaway.model.donacion.Donacion;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.constants.TipoDonancion;
import com.donaway.utils.db.ConexionDbDonaway;

public class DonacionDaoImpl implements IDonacionDao {

    @Override
    public void registrar(Donacion dona) throws SQLException {
        String sql = "INSERT INTO donacion (id_donacion, document_id_donante, document_id_personal, rnc_institucion, "
                   + "codigo_referencia, fecha_extraccion, tipo_donacion, tipo_componente, cantidad_pinta) "
                   + "VALUES (UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dona.getIdDonacion());
            ps.setString(2, dona.getDocumentIdDonante());
            ps.setString(3, dona.getDocumentIdPersonal());
            ps.setString(4, dona.getRnc());
            ps.setString(5, dona.getCodigoReferencia());
            ps.setTimestamp(6, Timestamp.valueOf(dona.getFechaExtracion()));
            ps.setString(7, dona.getTipoDonacionObj().name());
            ps.setString(8, dona.getTipoComponenteObj().name());
            ps.setFloat(9, dona.getCantidadPinta());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Donacion> obtenerHistorial() throws SQLException {
        List<Donacion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_donacion,1) as uuid, d.* FROM donacion d ORDER BY fecha_extraccion DESC";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearDonacion(rs));
            }
        }
        return lista;
    }

    @Override
    public Donacion obtenerPorId(String idDonacion) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_donacion,1) as uuid, d.* FROM donacion d WHERE id_donacion = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearDonacion(rs);
            }
        }
        return null;
    }

    @Override
    public Donacion obtenerPorCodigoReferencia(String codigoReferencia) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_donacion,1) as uuid, d.* FROM donacion d WHERE codigo_referencia = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigoReferencia);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearDonacion(rs);
            }
        }
        return null;
    }

    @Override
    public List<Donacion> obtenerHistorialDeDonante(String documentoId) throws SQLException {
        List<Donacion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_donacion,1) as uuid, d.* FROM donacion d WHERE document_id_donante = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDonacion(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Donacion> obtenerPorFecha(java.time.LocalDate fecha) throws SQLException {
        List<Donacion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_donacion,1) as uuid, d.* FROM donacion d WHERE DATE(fecha_extraccion) = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDonacion(rs));
            }
        }
        return lista;
    }

    @Override
    public String obtenerDocumentIdDonantePorDonacion(String idDonacion) throws SQLException {
        String sql = "SELECT document_id_donante FROM donacion WHERE id_donacion = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idDonacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("document_id_donante");
            }
        }
        return null;
    }

    private Donacion mapearDonacion(ResultSet rs) throws SQLException {
        return new Donacion(
            rs.getString("uuid"),
            rs.getString("codigo_referencia"),
            rs.getString("document_id_donante"),
            null, 
            rs.getString("document_id_personal"),
            rs.getString("rnc_institucion"),
            rs.getTimestamp("fecha_extraccion").toLocalDateTime(),
            TipoDonancion.valueOf(rs.getString("tipo_donacion")),
            TipoComponente.valueOf(rs.getString("tipo_componente")),
            rs.getFloat("cantidad_pinta")
        );
    }
}
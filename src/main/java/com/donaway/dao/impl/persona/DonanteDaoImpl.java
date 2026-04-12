package com.donaway.dao.impl.persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.donaway.dao.persona.IDonanteDao;
import com.donaway.model.persona.Donante;
import com.donaway.utils.constants.*;
import com.donaway.utils.db.ConexionDbDonaway;

public class DonanteDaoImpl implements IDonanteDao {

    @Override
    public void registrarDonante(Donante d) throws SQLException {
        String sql = "INSERT INTO donante (document_id_donante, nombre, sexo, fecha_nacimiento, telefono, email, "
                   + "fecha_registro, tipo_sangre, factor_rh, id_ubicacion, ultima_donacion, estado_disponibilidad, "
                   + "autorizacion_contacto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, UUID_TO_BIN(?,1), ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getDocumentoId());
            ps.setString(2, d.getNombre());
            ps.setString(3, d.getSexo().name());
            ps.setDate(4, Date.valueOf(d.getFechaNacimiento()));
            ps.setString(5, d.getTelefono());
            ps.setString(6, d.getEmail());
            ps.setTimestamp(7, Timestamp.valueOf(d.getFechaRegistro()));
            ps.setString(8, d.getTipoSangre().name());
            ps.setString(9, d.getFactorRh().name());
            ps.setString(10, d.getIdUbicacion());
            ps.setTimestamp(11, d.getUltimaDonacion() != null ? Timestamp.valueOf(d.getUltimaDonacion()) : null);
            ps.setString(12, d.getEstadoDisponibilidad().name());
            ps.setBoolean(13, d.getAutorizacionContacto());

            ps.executeUpdate();
        }
    }

    @Override
    public void modificarDonante(Donante d) throws SQLException {
        String sql = "UPDATE donante SET nombre = ?, sexo = ?, fecha_nacimiento = ?, telefono = ?, email = ?, "
                   + "tipo_sangre = ?, factor_rh = ?, id_ubicacion = UUID_TO_BIN(?,1), estado_disponibilidad = ?, "
                   + "autorizacion_contacto = ? WHERE document_id_donante = ?";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, d.getNombre());
            ps.setString(2, d.getSexo().name());
            ps.setDate(3, Date.valueOf(d.getFechaNacimiento()));
            ps.setString(4, d.getTelefono());
            ps.setString(5, d.getEmail());
            ps.setString(6, d.getTipoSangre().name());
            ps.setString(7, d.getFactorRh().name());
            ps.setString(8, d.getIdUbicacion());
            ps.setString(9, d.getEstadoDisponibilidad().name());
            ps.setBoolean(10, d.getAutorizacionContacto());
            ps.setString(11, d.getDocumentoId());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminarDonante(String documentoId) throws SQLException {
        String sql = "DELETE FROM donante WHERE document_id_donante = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentoId);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizarEstado(String documentoId, EstadoDisponibilidad nuevoEstado) throws SQLException {
        String sql = "UPDATE donante SET estado_disponibilidad = ? WHERE document_id_donante = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, documentoId);
            ps.executeUpdate();
        }
    }

    @Override
    public Donante obtenerPorId(String documentId) throws SQLException {
        String sql = "SELECT *, BIN_TO_UUID(id_ubicacion,1) as id_ubi FROM donante WHERE document_id_donante = ?";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, documentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearDonante(rs);
            }
        }
        return null;
    }

    @Override
    public List<Donante> obtenerTodo() throws SQLException {
        String sql = "SELECT *, BIN_TO_UUID(id_ubicacion,1) as id_ubi FROM donante";
        return ejecutarConsultaLista(sql, null, false);
    }

    @Override
    public List<Donante> obtenerPorSangre(TipoSangre tipo, FactorRh factor) throws SQLException {
        String sql = "SELECT *, BIN_TO_UUID(id_ubicacion,1) as id_ubi FROM donante WHERE tipo_sangre = ? AND factor_rh = ?";
        List<Donante> lista = new ArrayList<>();
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipo.name());
            ps.setString(2, factor.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDonante(rs));
            }
        }
        return lista;
    }

    @Override
    public List<Donante> obtenerPorEstado(EstadoDisponibilidad estado) throws SQLException {
        String sql = "SELECT *, BIN_TO_UUID(id_ubicacion,1) as id_ubi FROM donante WHERE estado_disponibilidad = ?";
        return ejecutarConsultaLista(sql, estado.name(), true);
    }

    // --- MÉTODOS DE APOYO ---

    private List<Donante> ejecutarConsultaLista(String sql, String parametro, boolean tieneParametro) throws SQLException {
        List<Donante> lista = new ArrayList<>();
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (tieneParametro) ps.setString(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapearDonante(rs));
            }
        }
        return lista;
    }

    private Donante mapearDonante(ResultSet rs) throws SQLException {
        Donante d = new Donante(
            rs.getString("document_id_donante"),
            rs.getString("nombre"),
            Sexo.valueOf(rs.getString("sexo")),
            rs.getDate("fecha_nacimiento").toLocalDate(),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getTimestamp("fecha_registro").toLocalDateTime(),
            TipoSangre.valueOf(rs.getString("tipo_sangre")),
            FactorRh.valueOf(rs.getString("factor_rh")),
            rs.getString("id_ubi"),
            rs.getTimestamp("ultima_donacion") != null ? rs.getTimestamp("ultima_donacion").toLocalDateTime() : null,
            EstadoDisponibilidad.valueOf(rs.getString("estado_disponibilidad")),
            rs.getBoolean("autorizacion_contacto")
        );
        return d;
    }
}
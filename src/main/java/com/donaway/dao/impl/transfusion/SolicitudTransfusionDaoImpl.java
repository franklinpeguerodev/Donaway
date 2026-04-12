package com.donaway.dao.impl.transfusion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.donaway.dao.transfusion.ISolicitudTransfusionDao;
import com.donaway.model.transfusion.SolicitudTransfusion;
import com.donaway.utils.constants.EstadoSolicitud;
import com.donaway.utils.constants.PrioridadSolicitud;
import com.donaway.utils.constants.TipoComponente;
import com.donaway.utils.db.ConexionDbDonaway;

public class SolicitudTransfusionDaoImpl implements ISolicitudTransfusionDao {

    @Override
    public void registrar(SolicitudTransfusion solicitud) throws SQLException {
        String sql = "INSERT INTO solicitud_transfusion (id_solicitud, document_id_receptor, rnc_institucion, "
                   + "codigo_referencia, cantidad_solicitada, fecha_solicitud, estado_solicitud, prioridad, "
                   + "tipo_componente, fecha_entrega) "
                   + "VALUES (UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, solicitud.getIdSolicitud());
            ps.setString(2, solicitud.getDocumentIdReceptor());
            ps.setString(3, solicitud.getRnc());
            ps.setString(4, solicitud.getCodigoReferencia());
            ps.setDouble(5, solicitud.getCantidadSolicitada());
            ps.setTimestamp(6, Timestamp.valueOf(solicitud.getFechaSolicitud()));
            ps.setString(7, solicitud.getEstadoSolicitudObj().name());
            ps.setString(8, solicitud.getPrioridadObj().name());
            ps.setString(9, solicitud.getComponentes().name());
            ps.setTimestamp(10, solicitud.getFechaEntrega() != null ? Timestamp.valueOf(solicitud.getFechaEntrega()) : null);

            ps.executeUpdate();
        }
    }

    @Override
    public void actualizarEstado(String idSolicitud, EstadoSolicitud nuevoEstado) throws SQLException {
        String sql = "UPDATE solicitud_transfusion SET estado_solicitud = ? WHERE id_solicitud = UUID_TO_BIN(?,1)";
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, idSolicitud);
            ps.executeUpdate();
        }
    }

    @Override
    public List<SolicitudTransfusion> obtenerPendientes(PrioridadSolicitud prioridad) throws SQLException {
        List<SolicitudTransfusion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_solicitud,1) as id_sol, s.* FROM solicitud_transfusion s "
                   + "WHERE estado_solicitud = 'PENDIENTE' AND prioridad = ? ORDER BY fecha_solicitud ASC";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prioridad.name());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearSolicitud(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public List<SolicitudTransfusion> obtenerPorInstitucion(String rnc) throws SQLException {
        List<SolicitudTransfusion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_solicitud,1) as id_sol, s.* FROM solicitud_transfusion s "
                   + "WHERE rnc_institucion = ?";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rnc);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearSolicitud(rs));
                }
            }
        }
        return lista;
    }

    @Override
    public SolicitudTransfusion obtenerPorId(String idSolicitud) throws SQLException {
        String sql = "SELECT BIN_TO_UUID(id_solicitud,1) as id_sol, s.* FROM solicitud_transfusion s "
                   + "WHERE id_solicitud = UUID_TO_BIN(?,1)";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, idSolicitud);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearSolicitud(rs);
            }
        }
        return null;
    }

    @Override
    public List<SolicitudTransfusion> obtenerHistorial() throws SQLException {
        List<SolicitudTransfusion> lista = new ArrayList<>();
        String sql = "SELECT BIN_TO_UUID(id_solicitud,1) as id_sol, s.* FROM solicitud_transfusion s "
                   + "ORDER BY fecha_solicitud DESC";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearSolicitud(rs));
            }
        }
        return lista;
    }

    // Método de mapeo privado para asegurar que se usen los setters correctos del Model
    private SolicitudTransfusion mapearSolicitud(ResultSet rs) throws SQLException {
        SolicitudTransfusion s = new SolicitudTransfusion();
        s.setIdSolicitud(rs.getString("id_sol"));
        s.setDocumentIdReceptor(rs.getString("document_id_receptor"));
        s.setRnc(rs.getString("rnc_institucion"));
        s.setCodigoReferencia(rs.getString("codigo_referencia"));
        s.setCantidadSolicitada(rs.getDouble("cantidad_solicitada"));
        s.setFechaSolicitud(rs.getTimestamp("fecha_solicitud").toLocalDateTime());
        s.setEstadoSolicitudObj(EstadoSolicitud.valueOf(rs.getString("estado_solicitud")));
        s.setPrioridadObj(PrioridadSolicitud.valueOf(rs.getString("prioridad")));
        s.setComponentes(TipoComponente.valueOf(rs.getString("tipo_componente")));
        
        Timestamp entrega = rs.getTimestamp("fecha_entrega");
        if (entrega != null) {
            s.setFechaEntrega(entrega.toLocalDateTime());
        }
        
        return s;
    }
}
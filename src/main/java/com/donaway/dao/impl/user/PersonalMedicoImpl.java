package com.donaway.dao.impl.user;

import com.donaway.dao.user.IPersonalMedico;
import com.donaway.utils.constants.Cargo;
import com.donaway.utils.constants.Departamento;
import com.donaway.utils.constants.EstadoPersonal;
import com.donaway.utils.constants.Jornada;
import com.donaway.utils.constants.Rol;
import com.donaway.utils.constants.Sexo;
import com.donaway.utils.db.ConexionDbDonaway; 
import com.donaway.model.user.PersonalMedico;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
	
public class PersonalMedicoImpl implements IPersonalMedico{
	
	@Override
    public void registrarPersonalMedico(PersonalMedico pm) throws SQLException {
        String sql = "INSERT INTO personal_medico (document_id_personal, rnc_institucion, nombre, fecha_nacimiento, "
                   + "id_ubicacion, cargo, departamento, rol, codigo_carnet, jornada, telefono, email, "
                   + "credencial_encriptada, fecha_registro, estado, sexo) "
                   + "VALUES (?, ?, ?, ?, UUID_TO_BIN(?,1), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, pm.getDocumentoId());
            ps.setString(2, pm.getRnc());
            ps.setString(3, pm.getNombre());
            ps.setDate(4, java.sql.Date.valueOf(pm.getFechaNacimiento()));
            ps.setString(5, pm.getIdUbicacion());
            ps.setString(6, pm.getCargo().name());
            ps.setString(7, pm.getDepartamento().name());
            ps.setString(8, pm.getRol().name());
            ps.setString(9, pm.getCodigoCarnet());
            ps.setString(10, pm.getJornada().name());
            ps.setString(11, pm.getTelefono());
            ps.setString(12, pm.getEmail());
            ps.setString(13, pm.getCredencialEncriptada());
            ps.setTimestamp(14, Timestamp.valueOf(pm.getFechaRegistro()));
            ps.setString(15, pm.getEstado().name());
            ps.setString(16, pm.getSexo().name());

            ps.executeUpdate();
        }
    }

    @Override
    public void modificarPersonalMedico(PersonalMedico pm) throws SQLException {
        String sql = "UPDATE personal_medico SET nombre = ?, cargo = ?, departamento = ?, rol = ?, "
                   + "jornada = ?, telefono = ?, email = ?, estado = ? WHERE document_id_personal = ?";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, pm.getNombre());
            ps.setString(2, pm.getCargo().name());
            ps.setString(3, pm.getDepartamento().name());
            ps.setString(4, pm.getRol().name());
            ps.setString(5, pm.getJornada().name());
            ps.setString(6, pm.getTelefono());
            ps.setString(7, pm.getEmail());
            ps.setString(8, pm.getEstado().name());
            ps.setString(9, pm.getDocumentoId());

            ps.executeUpdate();
        }
    }

    @Override
    public void actualizarUltimoAccesoPersonalMedico(String documentId) throws SQLException {
    	
        String sql = "UPDATE personal_medico SET ultimo_acceso = CURRENT_TIMESTAMP WHERE document_id_personal = ?";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, documentId);
            ps.executeUpdate();
        }
    }

    @Override
    public PersonalMedico obtenerPorId(String documentId) throws SQLException {
    	
    	String sql = "SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, "
                + "v.direccion_completa "
                + "FROM personal_medico p "
                + "INNER JOIN v_ubicacion_completa v ON p.id_ubicacion = v.id_ubicacion "
                + "WHERE p.document_id_personal = ?";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, documentId);
            
            try (ResultSet rs = ps.executeQuery()) {
            	
                if (rs.next()) 
                	return mapearPersonal(rs);
            }
        }
        return null;
    }

    
    
    @Override
    public PersonalMedico obtenerPorCodigoCarnet(String codigo) throws SQLException {
        String sql = "SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, "
                   + "v.direccion_completa "
                   + "FROM personal_medico p "
                   + "INNER JOIN v_ubicacion_completa v ON p.id_ubicacion = v.id_ubicacion "
                   + "WHERE p.codigo_carnet = ?";
                   
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, codigo);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // El mapeo centralizado ya maneja la carga de la vista
                    return mapearPersonal(rs);
                }
            }
        }
        return null;
    }

    
    
    @Override
    public List<PersonalMedico> obtenerInstitucion(String rnc) throws SQLException {
        String sql = "SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, v.direccion_completa "
                   + "FROM personal_medico p "
                   + "INNER JOIN v_ubicacion_completa v ON p.id_ubicacion = v.id_ubicacion "
                   + "WHERE p.rnc_institucion = ?";
        return ejecutarConsultaLista(sql, rnc);
    }

    @Override
    public List<PersonalMedico> obtenerPorDepartamento(Departamento depto) throws SQLException {
        String sql = "SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, v.direccion_completa "
                   + "FROM personal_medico p "
                   + "INNER JOIN v_ubicacion_completa v ON p.id_ubicacion = v.id_ubicacion "
                   + "WHERE p.departamento = ?";
        return ejecutarConsultaLista(sql, depto.name());
    }

    @Override
    public List<PersonalMedico> obtenerPorJornada(Jornada jornada) throws SQLException {
        String sql = "SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, v.direccion_completa "
                   + "FROM personal_medico p "
                   + "INNER JOIN v_ubicacion_completa v ON p.id_ubicacion = v.id_ubicacion "
                   + "WHERE p.jornada = ?";
        return ejecutarConsultaLista(sql, jornada.name());
    }

    @Override
    public List<PersonalMedico> obtenerActivos() throws SQLException {
        String sql = "SELECT p.*, BIN_TO_UUID(p.id_ubicacion, 1) AS id_ubi_uuid, v.direccion_completa "
                   + "FROM personal_medico p "
                   + "INNER JOIN v_ubicacion_completa v ON p.id_ubicacion = v.id_ubicacion "
                   + "WHERE p.estado = 'ACTIVO'";
                   
        List<PersonalMedico> lista = new ArrayList<>();
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearPersonal(rs));
            }
        }
        return lista;
    }

    @Override
    public boolean validarCredenciales(String email, String password) throws SQLException {
    	
        String sql = "SELECT COUNT(*) FROM personal_medico WHERE email = ? AND credencial_encriptada = ? AND estado = 'ACTIVO'";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
            	
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    
    
    private List<PersonalMedico> ejecutarConsultaLista(String sql, String parametro) throws SQLException {
    	
        List<PersonalMedico> lista = new ArrayList<>();
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
        	
            ps.setString(1, parametro);
            
            try (ResultSet rs = ps.executeQuery()) {
            	
                while (rs.next()) lista.add(mapearPersonal(rs));
            }
        }
        return lista;
    }

    
    
    private PersonalMedico mapearPersonal(ResultSet rs) throws SQLException {
    	
        PersonalMedico pm = new PersonalMedico(
        		
            rs.getString("document_id_personal"),
            rs.getString("nombre"),
            Sexo.valueOf(rs.getString("sexo")),
            rs.getDate("fecha_nacimiento").toLocalDate(),
            rs.getString("id_ubi_uuid"),
            rs.getString("telefono"),
            rs.getString("email"),
            rs.getTimestamp("fecha_registro").toLocalDateTime(),
            rs.getString("rnc_institucion"),
            rs.getString("codigo_carnet"),
            Cargo.valueOf(rs.getString("cargo")),
            Departamento.valueOf(rs.getString("departamento")),
            Rol.valueOf(rs.getString("rol")),
            Jornada.valueOf(rs.getString("jornada")),
            EstadoPersonal.valueOf(rs.getString("estado")),
            rs.getString("credencial_encriptada"),
            rs.getTimestamp("ultimo_acceso") != null ? rs.getTimestamp("ultimo_acceso").toLocalDateTime() : null
        );
        
        pm.setIdUbicacion(rs.getString("id_ubi_uuid"));
        pm.setCodigoCarnet(rs.getString("codigo_carnet"));
        pm.setDirreccionCompleta(rs.getString("direccion_completa"));
        
        return pm;
    }

    @Override
    public PersonalMedico devolverPorCarnet(String carnet) throws SQLException {
        return obtenerPorCodigoCarnet(carnet);
    }
}
package com.donaway.dao.impl.institucion;

import com.donaway.dao.institucion.IInstitucionDao;
import com.donaway.model.institucion.Institucion;
import com.donaway.utils.db.ConexionDbDonaway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
public class InstitucionImpl implements IInstitucionDao{
	
	
	@Override
    public void registrarInstitucion(Institucion inst) throws SQLException {
        String sql = "INSERT INTO institucion_medica (rnc_institucion, id_ubicacion, nombre, contacto_emergencia, fecha_registro) "
                   + "VALUES (?, UUID_TO_BIN(?,1), ?, ?, ?)";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, inst.getRnc());
            ps.setString(2, inst.getIdUbicacion());
            ps.setString(3, inst.getNombre());
            ps.setString(4, inst.getContactoEmergencia());
            ps.setTimestamp(5, Timestamp.valueOf(inst.getFechaRegistro().atStartOfDay())); // Convertimos LocalDate a Timestamp para MySQL

            ps.executeUpdate();
            System.out.println("Institución registrada: " + inst.getNombre());
        }
    }

    @Override
    public Institucion obtenerPorRnc(String rnc) throws SQLException {
        String sql = "SELECT inst.rnc_institucion, inst.nombre, inst.contacto_emergencia, inst.fecha_registro, "
        		   + "BIN_TO_UUID(inst.id_ubicacion,1) as id_ubi, " 
        		   + "view.direccion_completa "
                   + "FROM institucion_medica inst INNER JOIN v_ubicacion_completa view " 
        		   + "ON inst.id_ubicacion = view.id_ubicacion WHERE inst.rnc_institucion = ?";
        
        Institucion inst = null;

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, rnc);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    inst = new Institucion(
                        rs.getString("rnc_institucion"),
                        rs.getString("nombre"),
                        rs.getString("id_ubi"), // Recuperamos el UUID como String
                        rs.getString("contacto_emergencia"),
                        rs.getTimestamp("fecha_registro").toLocalDateTime().toLocalDate()
                    );
                    inst.setDirreccionCompleta(rs.getString("direccion_completa"));
                }
            }
        }
        return inst;
    }

    @Override
    public List<Institucion> obtenerInstituciones() throws SQLException {
        List<Institucion> lista = new ArrayList<>();
        String sql = "SELECT inst.rnc_institucion, inst.nombre, inst.contacto_emergencia, inst.fecha_registro, "
     		   + "BIN_TO_UUID(inst.id_ubicacion,1) as id_ubi, " 
     		   + "view.direccion_completa "
                + "FROM institucion_medica inst INNER JOIN v_ubicacion_completa view " 
     		   + "ON inst.id_ubicacion = view.id_ubicacion";
        
        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
            	Institucion inst = new Institucion(
                    rs.getString("rnc_institucion"),
                    rs.getString("nombre"),
                    rs.getString("id_ubi"),
                    rs.getString("contacto_emergencia"),
                    rs.getTimestamp("fecha_registro").toLocalDateTime().toLocalDate() 
                );
            	inst.setDirreccionCompleta(rs.getString("direccion_completa"));
            	
            	lista.add(inst);
            }
        }
        return lista;
    }

    @Override
    public void modificarInstitucion(Institucion inst) throws SQLException {
        String sql = "UPDATE institucion_medica SET nombre = ?, contacto_emergencia = ? WHERE rnc_institucion = ?";

        try (Connection conn = ConexionDbDonaway.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, inst.getNombre());
            ps.setString(2, inst.getContactoEmergencia());
            ps.setString(3, inst.getRnc());

            ps.executeUpdate();
        }
    }
}


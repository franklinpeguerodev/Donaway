package com.donaway.dao.impl.donante;

import com.donaway.dao.donacion.IDonanteDao;
import com.donaway.model.donacion.Donante;
import com.donaway.utils.db.ConexionBdDonaway;
import java.sql.Connection;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;

public class DonanteImpl implements IDonanteDao {
	@Override
	public void registrarDonante(Donante donante) {
		String sql = "INSERT INTO donante (documento_id,nombre,fecha_nacimiento,sexo,telefono,"
				+ "email,tipo_sangre,factor_rh,peso,estado_disponibilidad,fecha_registo,autorizacion_contacto,ultima_donacion,id_ubicacion)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn= ConexionBdDonaway.getConnection()
			;PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, donante.getDocumentoId());
			ps.setString(2, donante.getNombre());
			ps.setString(3, donante.getSexo());
			ps.setObject(4, donante.getFechaNacimiento());
			ps.setString(5, donante.getTelefono());
			ps.setString(6, donante.getEmail());
			ps.setObject(7, donante.getFechaRegistro());
			ps.setFloat(8,  donante.getPeso());
			ps.setString(9, donante.getTipoSangre());
			ps.setString(10,donante.getFactorRh());
			ps.setObject(11,donante.getUltimaDonacion());
			ps.setString(12,donante.getEstadoDisponibilidad());
			ps.setString(13,donante.getDireccion());
			ps.setBoolean(14,donante.getAutorizacionContacto());
			
		}catch(SQLException e){
			System.out.print("Error de sql" + e);
			e.printStackTrace();
		}
		
	}
	@Override
	public void eliminarDonante (String documenId) {
		
	}
	@Override
	public void modificarDonante (Donante donante) {
	
	}
	@Override
	public Donante obtenerId(String documentId) {
		Donante d = new Donante();
		return d;
	}
	@Override	
	public List<Donante> obtenerTodo(){
		List<Donante> list = new ArrayList<>();
		String sql= "SELECT * FROM donantes";
		try(Connection conn = ConexionBdDonaway.getConnection();
		PreparedStatement ps =conn.prepareStatement(sql)){
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				Donante d = new Donante();
				d.setDocumentoId(rs.getString("document_id"));
				d.setNombre(rs.getString("nombre"));
				d.setSexo(rs.getString("sexo"));
				d.setFechaNacimiento(rs.getObject("fecha_nacimiento",LocalDate.class));
				d.setTelefono(rs.getString("telefono"));
				d.setEmail(rs.getString("email"));
				d.setFechaRegistro(rs.getObject("fecha_registro",LocalDateTime.class));
				d.setPeso(rs.getFloat("peso"));
				d.setTipoSangre(rs.getString("tipo_sangre"));
				d.setFactorRh(rs.getString("factor_rh"));
				d.setUltimaDonacion(rs.getObject("ultima_donacion",LocalDateTime.class));
				d.setEstadoDisponibilidad(rs.getString("estado_disponibilidad"));
				d.setDireccion(rs.getString("direccion"));
				d.setAutorizacionContacto(rs.getBoolean("autorizacion_contacto"));
				list.add(d);
				
			}	
			}catch(SQLException e){
			System.out.println("Error sql:" + e);
			e.printStackTrace();
		}
		return list;
	}
	public static void main(String[] args) {
        System.out.println("Buscando donantes...");
        for (Donante d : new DonanteImpl().obtenerTodo()) {
            System.out.println(d.getNombre() + " - " + d.getDocumentoId());
        }
    }
}


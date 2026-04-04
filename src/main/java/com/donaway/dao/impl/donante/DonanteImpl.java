package com.donaway.dao.impl.donante;

import com.donaway.dao.donacion.IDonanteDao;
import com.donaway.model.donacion.Donante;
import com.donaway.utils.db.ConexionDbDonaway;
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
		String sql = "INSERT INTO donante(document_id_donante,nombre,sexo,fecha_nacimiento,telefono,"
				+ "email,fecha_registro,tipo_sangre,factor_rh,ultima_donacion,estado_disponibilidad,autorizacion_contacto)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		try (Connection conn= ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, donante.getDocumentoId());
			ps.setString(2, donante.getNombre());
			ps.setString(3, donante.getSexo());
			ps.setObject(4, donante.getFechaNacimiento());
			ps.setString(5, donante.getTelefono());
			ps.setString(6, donante.getEmail());
			ps.setObject(7, donante.getFechaRegistro());
			ps.setString(8, donante.getTipoSangre());
			ps.setString(9,donante.getFactorRh());
			ps.setObject(10,donante.getUltimaDonacion());
			ps.setString(11,donante.getEstadoDisponibilidad());
			ps.setBoolean(12,donante.getAutorizacionContacto());;
			ps.executeUpdate();
			System.out.print("se inserto un nuevo Donante");
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
	public List<Donante> obtenerTodo(){
		return null;
	}
	
}


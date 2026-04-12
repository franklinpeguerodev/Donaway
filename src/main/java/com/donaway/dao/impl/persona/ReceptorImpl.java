package com.donaway.dao.impl.persona;

import com.donaway.dao.persona.IReceptorDao;
import com.donaway.model.persona.Donante;
import com.donaway.model.persona.Receptor;
import com.donaway.utils.db.ConexionDbDonaway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
public class ReceptorImpl implements IReceptorDao {
	
	@Override
	public void registrarReceptor(Receptor receptor) throws SQLException{
		String sql="INSERT INTO receptor (document_id_receptor,nombre,sexo,fecha_nacimiento,telefono,email,fechaRegistro"
				+ "peso,tipo_sangre,factor_rh) VALUES (?,?,?,?,?,?,?,?,?,?)";
		try(Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1,receptor.getDocumentoId());
			ps.setString(2,receptor.getNombre());
			ps.setString(3,receptor.getSexo().name());
			ps.setObject(4,receptor.getFechaNacimiento());
			ps.setString(5,receptor.getTelefono());
			ps.setString(6,receptor.getEmail());
			ps.setObject(7,receptor.getFechaRegistro());
			ps.setBigDecimal(8,receptor.getPeso());
			ps.setString(9,receptor.getTipoSangre().name());
			ps.setString(10,receptor.getFactorRh().name());
			ps.executeUpdate();
			System.out.println("Se inserto");
		}
	
	
	}

	@Override
	public void modificarReceptor(Receptor receptor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Receptor> obtenerTodo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Receptor> buscarPorNombre(String nombre) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Receptor obtenerPorId(String documentId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}


	
}

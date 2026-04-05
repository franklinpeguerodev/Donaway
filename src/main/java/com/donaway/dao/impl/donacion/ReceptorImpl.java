package com.donaway.dao.impl.donacion;

import com.donaway.dao.donacion.IReceptorDao;
import com.donaway.model.donacion.Receptor;
import com.donaway.utils.db.ConexionDbDonaway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class ReceptorImpl implements IReceptorDao {
	
	@Override
	public void registrarReceptorDonante(Receptor receptor){
		String sql="INSERT INTO receptor (document_id_receptor,nombre,sexo,fecha_nacimiento,telefono,email,fechaRegistro"
				+ "peso,tipo_sangre,factor_rh) VALUES (?,?,?,?,?,?,?,?,?,?)";
		try(Connection conn = ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1,receptor.getDocumentoId());
			ps.setString(2,receptor.getNombre());
			ps.setString(3,receptor.getSexo());
			ps.setObject(4,receptor.getFechaNacimiento());
			ps.setString(5,receptor.getTelefono());
			ps.setString(6,receptor.getEmail());
			ps.setObject(7,receptor.getFechaRegistro());
			ps.setBigDecimal(8,receptor.getPeso());
			ps.setString(9,receptor.getTipoSangre());
			ps.setString(10,receptor.getFactorRh());
			ps.executeUpdate();
			System.out.println("Se inserto");
		}catch(SQLException e){
			System.out.println("Error de sql: "+e );
			e.printStackTrace();
		}
	
	
	
	}
}

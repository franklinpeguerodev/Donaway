package com.donaway.dao.impl.institucion;

import com.donaway.dao.institucion.IInstitucionDao;
import com.donaway.model.institucion.Institucion;
import com.donaway.utils.db.ConexionDbDonaway;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class InstitucionImpl implements IInstitucionDao{
	@Override
	public void registrarInstitucion(Institucion institucion) {
		String sql ="INSERT TO institucion_medica(rnc_institucion,nombre,contacto_emergencia,fecha_registro) VALUES (?,?,?,?)";
		try(Connection conn=ConexionDbDonaway.getConnection();
		   PreparedStatement ps= conn.prepareStatement(sql)){
			ps.setString(1,institucion.getRnc());
			ps.setString(2, institucion.getNombre());
			ps.setString(3, institucion.getContactoEmergencia());
			ps.setObject(4, institucion.getFechaRegistro());
		}catch(SQLException e) {
			System.out.println("Error sql: " + e);
		}
	}
}

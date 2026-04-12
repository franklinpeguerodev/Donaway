package com.donaway.dao.impl.user;
import com.donaway.dao.user.IUserDao;
import com.donaway.dto.login.LoginRequestDto;
import com.donaway.dto.login.UserSessionDto;
import com.donaway.utils.db.ConexionDbDonaway;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UserImpl implements IUserDao {
	@Override
	public UserSessionDto validarLogin(LoginRequestDto request) throws SQLException {
		String sql = "SELECT nombre,cargo,departamento,codigo_carnet,turno,estado"
				+ "FROM personal_medico where document_id_personal=? AND credencial_encriptada=?";
		try(Connection conn=ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1,request.usuario());
			ps.setString(2, request.password());
			ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			return new UserSessionDto(
					rs.getString("nombre"),
					rs.getString("cargo"),
					rs.getString("departameneto"),
					rs.getString("carnet"),
					rs.getString("turno"),
					rs.getString("estado")
					);
		}
		
		return null;
		}
	
	}
}

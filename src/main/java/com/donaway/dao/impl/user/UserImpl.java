package com.donaway.dao.impl.user;
import com.donaway.dao.user.IUserDao;
import com.donaway.dto.login.LoginRequestDto;
import com.donaway.dto.login.UserSessionDto;
import com.donaway.utils.constants.Cargo;
import com.donaway.utils.constants.Departamento;
import com.donaway.utils.constants.EstadoPersonal;
import com.donaway.utils.constants.Jornada;
import com.donaway.utils.db.ConexionDbDonaway;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
public class UserImpl implements IUserDao {
	@Override
	public UserSessionDto validarLogin(LoginRequestDto request) throws SQLException {
	  
		String sql = "SELECT nombre,cargo,departamento,codigo_carnet,jornada,estado "
				+ "FROM personal_medico where document_id_personal=? AND credencial_encriptada=?";
		
		try(Connection conn=ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1,request.usuario());
			ps.setString(2, request.password());
			ResultSet rs = ps.executeQuery();
		
		if (rs.next()) {
			String cargo = rs.getString("cargo");
			String departamento = rs.getString("departamento");
			String jornada = rs.getString("jornada");
			String estado = rs.getString("estado");
			
			Cargo cargoEnum = Cargo.valueOf(cargo);
			Departamento departamentoEnum = Departamento.valueOf(departamento);
			Jornada jornadaEnum = Jornada.valueOf(jornada);
			EstadoPersonal estadoEnum = EstadoPersonal.valueOf(estado);
			return new UserSessionDto(
					rs.getString("nombre"),
					cargoEnum,
					departamentoEnum,
					rs.getString("codigo_carnet"),
					jornadaEnum,
					estadoEnum
					);
		}
		
		return null;
		}
	
	}
}

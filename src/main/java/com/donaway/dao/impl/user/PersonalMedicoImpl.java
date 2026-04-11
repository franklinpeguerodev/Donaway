package com.donaway.dao.impl.user;
import com.donaway.dao.user.IPersonalMedico;
import com.donaway.utils.db.ConexionDbDonaway; 
import com.donaway.model.user.PersonalMedico;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
	public class PersonalMedicoImpl implements IPersonalMedico{
		String sql = "INSERT INTO personal_medico (documento_id_personal,nombre,sexo,fecha_nacimiento,telefono,email"
				+ "rnc_institucion,rol,credenciales_encriptadas,ultimoAcceso)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
		@Override
		public void registrarPersonalMedico(PersonalMedico personalMedico) throws SQLException{
		try (Connection conn= ConexionDbDonaway.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setString(1, personalMedico.getDocumentoId());
			ps.setString(2, personalMedico.getNombre());
			ps.setString(3,	personalMedico.getSexo().name());
			ps.setObject(4,	personalMedico.getFechaNacimiento());
			ps.setString(5, personalMedico.getTelefono());
			ps.setString(6,	personalMedico.getEmail());
			ps.setString(7, personalMedico.getRnc());
			ps.setString(8,personalMedico.getRol().name());
			ps.setString(9, personalMedico.getCredencialEncriptada());
			ps.setObject(10, personalMedico.getUltimoAcceso());
			
			ps.executeUpdate();
			System.out.println("Se inserto los datos");
		}
				
	}}


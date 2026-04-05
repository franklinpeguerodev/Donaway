package com.donaway.dao.impl.user;
import com.donaway.dao.user.IPersonalMedico;
import com.donaway.model.user.PersonalMedico;
public class PersonalMedicoImpl implements IPersonalMedico{
	public void registrarPersonalMedico(PersonalMedico personalMedico) {
		String sql = "INSERT INTO personal_medico (documento_id_personal,nombre,sexo,fecha_nacimiento,telefono,email"
				+ "rnc_institucion,rol,credenciales_encriptadas,ultimoAcceso)  ";
	}
}

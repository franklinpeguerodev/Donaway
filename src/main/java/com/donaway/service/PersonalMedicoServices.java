package com.donaway.service;
import com.donaway.dao.user.IPersonalMedico;
import com.donaway.dao.impl.user.PersonalMedicoImpl;
import com.donaway.model.user.PersonalMedico;
import com.donaway.utils.mensaje.Mensaje;
import com.donaway.exception.ValidacionException;
import java.sql.SQLException;
public class PersonalMedicoServices {
	private IPersonalMedico personalMedicoDao = new PersonalMedicoImpl();
	
	public void procesarNuevoPersonal(PersonalMedico personalMedico) {
		
		try {
			personalMedicoDao.registrarPersonalMedico(personalMedico);
			System.out.println("Se inserto");
			}catch (SQLException e) {
				if (e.getErrorCode()==1062) {
					throw new ValidacionException(Mensaje.ERR_SQL_DUPLICADO);
				}
				throw new ValidacionException(Mensaje.ERR_CONEXION);
			}
		
		
	}




}

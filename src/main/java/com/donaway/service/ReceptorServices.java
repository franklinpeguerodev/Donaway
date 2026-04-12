package com.donaway.service;

import com.donaway.dao.impl.persona.ReceptorImpl;
import com.donaway.dao.persona.IReceptorDao;
import com.donaway.model.persona.Receptor;
import com.donaway.exception.ValidacionException;
import com.donaway.utils.mensaje.Mensaje;
import java.sql.SQLException;
public class ReceptorServices {
	private IReceptorDao receptorDao = new ReceptorImpl();
	public void ProcesarNuevoReceptor (Receptor receptor) {
		try {
		receptorDao.registrarReceptor(receptor);
		}catch(SQLException e) {
			if (e.getErrorCode()==1062) {
				throw new ValidacionException(Mensaje.ERR_SQL_DUPLICADO);
			}
			throw new ValidacionException(Mensaje.ERR_CONEXION); 
		}
	}
}

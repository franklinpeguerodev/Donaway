package com.donaway.service;

import com.donaway.model.donacion.Receptor;
import com.donaway.dao.impl.donacion.ReceptorImpl;
import com.donaway.dao.donacion.IReceptorDao;
import com.donaway.exception.ValidacionException;
import com.donaway.utils.mensaje.Mensaje;
import java.sql.SQLException;
public class ReceptorServices {
	private IReceptorDao receptorDao = new ReceptorImpl();
	public void ProcesarNuevoReceptor (Receptor receptor) {
		try {
		receptorDao.registrarReceptorDonante(receptor);
		}catch(SQLException e) {
			if (e.getErrorCode()==1062) {
				throw new ValidacionException(Mensaje.ERR_SQL_DUPLICADO);
			}
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
}

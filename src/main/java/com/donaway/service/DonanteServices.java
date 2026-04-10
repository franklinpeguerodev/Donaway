package com.donaway.service;
import com.donaway.dao.donacion.IDonanteDao;
import com.donaway.dao.impl.donacion.DonanteImpl;

import com.donaway.exception.ValidacionException;

import com.donaway.utils.mensaje.Mensaje;

import	java.sql.SQLException;

import com.donaway.model.persona.Donante;


public class DonanteServices {
	private IDonanteDao donanteDao = new DonanteImpl();
	public void procesarNuevoDonante(Donante donante) {
		try {		
		donanteDao.registrarDonante(donante);
		System.out.println("Se inserto");
		}catch (SQLException e){
			if (e.getErrorCode()==1062) {
				throw new ValidacionException(Mensaje.ERR_SQL_DUPLICADO);
			}
			throw new ValidacionException(Mensaje.ERR_CONEXION);
	}
	
}
}
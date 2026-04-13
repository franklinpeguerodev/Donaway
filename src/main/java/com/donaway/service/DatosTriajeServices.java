package com.donaway.service;

import com.donaway.dao.donacion.IDatosTriajeDao;
import com.donaway.dao.impl.donacion.DatosTriajeDaoImpl;
import com.donaway.exception.ValidacionException;
import com.donaway.model.donacion.DatosTriaje;
import com.donaway.utils.mensaje.Mensaje;

import java.sql.SQLException;
import java.util.UUID;

public class DatosTriajeServices {

	private IDatosTriajeDao triajeDao = new DatosTriajeDaoImpl();

	////////////////////////////////////////////////////////////////////////////
	// Metodo para registrar triaje 
	
	public void registrarTriaje(DatosTriaje t) {
		

		if (t.getIdDonacion() == null || t.getCodigoReferencia() == null) {
			throw new ValidacionException(Mensaje.ERR_DATOS_INCOMPLETOS);
		}

		//Validacion de rangos (Sincronizado con los CHECK de SQL)
		
		if (t.getPesoLbs() < 110) {
			throw new ValidacionException("Peso insuficiente: El minimo son 110 lbs.");
		}

		if (t.getPresionSistolica() < 90 || t.getPresionSistolica() > 180) {
			throw new ValidacionException("Presion Sistolica fuera de rango (90-180 mmHg).");
		}

		if (t.getPresionDiastolica() < 50 || t.getPresionDiastolica() > 100) {
			throw new ValidacionException("Presion Diastolica fuera de rango (50-100 mmHg).");
		}

		if (t.getPulso() < 50 || t.getPulso() > 100) {
			throw new ValidacionException("Frecuencia cardiaca (Pulso) fuera de rango (50-100 lpm).");
		}

		if (t.getTemperatura() < 36.0 || t.getTemperatura() > 37.2) {
			throw new ValidacionException("Temperatura fuera de rango permitido (36.0°C - 37.2°C).");
		}

		if (t.getHemoglobina() < 12.5 || t.getHemoglobina() > 20.0) {
			throw new ValidacionException("Niveles de hemoglobina no aptos para donacion (12.5 - 20.0 g/dL).");
		}

		try {
		
			
			if (t.getIdTriaje() == null || t.getIdTriaje().isEmpty()) {
				t.setIdTriaje(UUID.randomUUID().toString());
			}

			//Verificacion de duplicidad de triaje para la misma donacion
			if (triajeDao.obtenerDonacion(t.getIdDonacion()) != null) {
				throw new ValidacionException("Ya existe un registro de triaje para esta donacion.");
			}

			
			triajeDao.registrar(t);

		} catch (SQLException e) {
			throw new ValidacionException(Mensaje.ERR_CONEXION);
		}
	}
	////////////////////////////////////////////////////////////////////////////
}
package com.donaway.service;

import com.donaway.model.donacion.Receptor;
import com.donaway.dao.impl.donacion.ReceptorImpl;
import com.donaway.dao.donacion.IReceptorDao;
public class ReceptorServices {
	private IReceptorDao receptorDao = new ReceptorImpl();
	public Boolean ProcesarNuevoReceptor (Receptor receptor) {
		if(receptor.getNombre()==null) {
			System.out.println("Esta vacia Nombre");//no funciona,por que los input no devuelven null si no ""
			return false;
		}
		receptorDao.registrarReceptorDonante(receptor);
	return true;
	}
}

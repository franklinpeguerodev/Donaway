package com.donaway.service;
import com.donaway.dao.donacion.IDonanteDao;
import com.donaway.dao.impl.donacion.DonanteImpl;
import com.donaway.model.donacion.Donante;

public class DonanteServices {
	private IDonanteDao donanteDao = new DonanteImpl();
	
	public boolean procesarNuevoDonante(Donante donante) {
		if(donante.getNombre() ==null) {
		System.out.println("Se inserto vacio");
		return false;
		}
		donanteDao.registrarDonante(donante);
		System.out.println("Se inserto");
		return true;
		
	}
	
}

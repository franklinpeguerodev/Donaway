package com.donaway.service;
import com.donaway.dao.donacion.IDonanteDao;
import com.donaway.model.donacion.Donante;
import com.donaway.dao.impl.donante.DonanteImpl;

public class DonanteServices {
	private IDonanteDao donanteDao = new DonanteImpl();
	
	public boolean procesarNuevoDonante(Donante donante) {
		if(donante.getPeso() <110) {
			
			return false;
		}
		if(donante.getNombre() ==null) {
			
		return false;
		}
		
		donanteDao.registrarDonante(donante);
		System.out.println("Se inserto");
		return true;
		
	}
	
}

package com.donaway.service;
import com.donaway.dao.user.IPersonalMedico;
import com.donaway.dao.impl.user.PersonalMedicoImpl;
import com.donaway.model.user.PersonalMedico;
public class PersonalMedicoServices {
	private IPersonalMedico personalMedicoDao = new PersonalMedicoImpl();
	
	public boolean procesarNuevoPersonal(PersonalMedico personalMedico) {
		if (personalMedico.getNombre() == null) {
			System.out.println("Se inserto el nombre vacio");
			return false;
		}
		personalMedicoDao.registrarPersonalMedico(personalMedico);
			System.out.println("Se inserto");
		return true;
	}




}

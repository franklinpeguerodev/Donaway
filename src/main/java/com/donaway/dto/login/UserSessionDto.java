package com.donaway.dto.login;
import com.donaway.utils.constants.Cargo;
import com.donaway.utils.constants.Departamento;
import com.donaway.utils.constants.Jornada;
import com.donaway.utils.constants.EstadoPersonal;
public record UserSessionDto(String nombre,Cargo cargo,
		Departamento departamento,String carnet,Jornada jornada, EstadoPersonal estado ) {
  
	
}

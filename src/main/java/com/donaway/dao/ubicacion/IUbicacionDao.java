package com.donaway.dao.ubicacion;

import java.util.List;

import com.donaway.model.ubicacion.*;

public interface IUbicacionDao {

	// buscar las regiones no requiere nada para hacertlo
	List <Region> listaRegiones();
	 
	// filtrar las provincias despues de recivir cual region se selecciono
	List <Provincia> listaProvinciasPorRegion(int idRegion);
	
	// filtrar los municipios despues de recivir cual provincia se selecciono
	List<Municipio> listaMunicipioPorProvincia (int idProvincia);
	
	// filtrar los distritos despues de recivir cual municipio se selecciono 
	List<Distrito> listaDistritoPorMunicipio (int idMunicipio);
	
	// filtrar las secciones despues de recivir cual municipio se selecciono en caso que no haiga distrito
	List<Seccion> listaSeccionPorMunicipio (int idMunicipio);
	
	// filtrar las secciones despues de recivir cual distrito se selecciono en caso de que haiga
	List<Seccion> listaSeccionPorDistrito (int idDistrito);
	
	// filtrar los barrios despues de recivir cual municipio se selecciono
	List<Barrio> listaBarrioPorSeccion (int idSeccion);

	// filtrar los sub barrios despues de recivir cual barrio se selecciono
	List<SubBarrio> listaSubBarrioPorBarrio (int idBarrio);
	
	
	//metodo para poder alamecenar la ubicacion a trabes de los ids
	int registrarUbicacion (Ubicacion ubicacion);
}

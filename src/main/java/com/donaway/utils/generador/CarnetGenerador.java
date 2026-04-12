package com.donaway.utils.generador;
import java.security.SecureRandom;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
public class CarnetGenerador {
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final char[] ALFABETO_INCOMPLETO="23456789ABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
	private CarnetGenerador() {
	}
	//generador de las siglas apartir del nombre de la institucion
	public static String generadorSiglasInstitucion(String nombre) {
		if(nombre == null || nombre.isBlank()) return "DW";
		
		String limpio = nombre.toLowerCase()
				.replaceAll("\\b(de|la|el|y|los|las|del|en)\\b", "")
				.replaceAll("\\s+", " ")
				.trim();
		String[] palabras = limpio.split(" ");
		StringBuilder siglas = new StringBuilder();
		if (palabras.length ==1) {
			String palabra = palabras[0];
			int limite = Math.min(3, palabra.length());
			siglas.append(palabra.substring(0,limite));
		}else {
			for (String palabra : palabras) {
				if (!palabra.isEmpty() && siglas.length() <4) {
					siglas.append(palabra.charAt(0));
				}
			}
		}
		return siglas.length() >0 ? siglas.toString().toUpperCase(): "DW";
	}

	//generador de los 5 digitos aleatorios
	public static String generadorId () {
		return NanoIdUtils.randomNanoId(RANDOM,ALFABETO_INCOMPLETO,5);
	}
	
	public static String GeneradorCarnet(String nombreInst) {
		
		return String.format("DW-%s-%s",
				generadorSiglasInstitucion(nombreInst),
				generadorId()
				
				
				);
	}




}


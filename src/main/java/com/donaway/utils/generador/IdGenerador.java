package com.donaway.utils.generador;

import java.security.SecureRandom;
import com.fasterxml.uuid.Generators;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.LocalDate;
public class IdGenerador {
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final char[] ALFABETO_COMPLETO ="ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();

	private IdGenerador() {
			throw new IllegalStateException("Clase utilitaria - no instanaciar");
		}
	public static String generadorUUID() {
	return Generators.timeBasedGenerator().generate().toString();
	}
	public static String generadorCodigoReferencia(String prefijo) {
		int year = LocalDate.now().getYear();
		String idRandom = NanoIdUtils.randomNanoId(RANDOM,ALFABETO_COMPLETO,8);
		return prefijo + "-" + year + "-" + idRandom;
	}
}

package com.donaway.utils.security;

import org.mindrot.jbcrypt.BCrypt;

public class SeguridadEncrip {
	private static final int WORK_FACTOR=12;
	private SeguridadEncrip() {
		throw new IllegalStateException("Clase utilitaria - no instanciar");
	}
	public static String hasearpassword(String passworplana) {
		return BCrypt.hashpw(passworplana, BCrypt.gensalt(WORK_FACTOR));
	}
}
;
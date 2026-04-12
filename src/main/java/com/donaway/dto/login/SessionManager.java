package com.donaway.dto.login;

import com.donaway.dto.login.UserSessionDto;
public class SessionManager {
	private static SessionManager instance;
	private UserSessionDto usuarioActual;
	
	private SessionManager() {}
	
	public static SessionManager getInstance() {
		if (instance == null) {
			instance = new SessionManager();
		}
		return instance;	
	}
	
	public void setUsuarioActual(UserSessionDto usuario) {
		this.usuarioActual =usuario;
	}
	
	public UserSessionDto getUsuarioActual() {
		return usuarioActual;
	}
	
	public void cerrarSesion() {
		usuarioActual=null;
	}

}

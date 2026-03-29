package com.donaway.model.user;
import java.time.LocalDateTime;

public class PersonalMedico extends Persona{
	protected String rnc;
	protected enum rol{};
	protected String credencialEcriptada;
	protected LocalDateTime ultimoAcceso;
}

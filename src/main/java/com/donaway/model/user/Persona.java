package com.donaway.model.user;
import java.time.LocalDate;
public abstract class Persona {
	protected String documentoId;
	protected String nombre;
	protected String sexo;
	protected LocalDate fechaNacimiento;
	protected String telefono ;
	protected String email;
	protected LocalDate fechaRegistro;
}

package com.donaway.utils.time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Time {
	private static final DateTimeFormatter fechaFormateada = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy",Locale.forLanguageTag("es-ES")); ;
	
	private Time() {}
	
	public static String dateNow() {
		return LocalDate.now().format(fechaFormateada);
		
		
	}
	
	public LocalDateTime dateTimeNow() {
		return LocalDateTime.now();
	}


}

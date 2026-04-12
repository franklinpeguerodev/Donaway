package com.donaway.utils.constants;

public enum Jornada {
    
    MATUTINA("Matutino (8 AM - 5 PM)"),
    NOCTURNA("Nocturno(12 AM - 6 AM)");

    private final String descripcionFront;


    Jornada(String descripcionFront)
	{this.descripcionFront = descripcionFront;}
	
	@Override
	public String toString() {
		return descripcionFront;}
	
	
}

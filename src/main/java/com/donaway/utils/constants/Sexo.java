package com.donaway.utils.constants;

public enum Sexo {
M ("Masculino"),
F ("Femenino");
	
	private final String descripcionFront;
	Sexo(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
	
	
    public static Sexo desdeTextoFront(String textoPantalla) {
        for (Sexo opcion : Sexo.values()) {
            if (opcion.descripcionFront.equalsIgnoreCase(textoPantalla.trim())) {
                return opcion;
            }
        }
        return null; 
    }
}
	
	
	
	

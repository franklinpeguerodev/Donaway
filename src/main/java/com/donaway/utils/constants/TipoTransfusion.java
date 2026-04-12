package com.donaway.utils.constants;

public enum TipoTransfusion {
	DIRECTA ("Directa"), 
	AUTOLOGA ("Autologa"), 
	ALOGENETICA ("Alogenetica");
	
	private final String descripcionFront;
	TipoTransfusion(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
}

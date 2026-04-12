package com.donaway.utils.constants;

public enum TipoSangre {
A ("A"),
B ("B"),
AB ("AB"),
O ("O");
	private final String descripcionFront;
	TipoSangre(String descripcionFront)
	{this.descripcionFront = descripcionFront;}

	@Override
	public String toString() {
		return descripcionFront;}
	
}

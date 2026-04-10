package com.donaway.controller.controllerpersonalmedico;

public class MenuGraficasObjeto {

	private String titulo;
    private String tipo;

    public MenuGraficasObjeto(String titulo, String tipo) {
        this.titulo = titulo;
        this.tipo = tipo;
    }

    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
	
}

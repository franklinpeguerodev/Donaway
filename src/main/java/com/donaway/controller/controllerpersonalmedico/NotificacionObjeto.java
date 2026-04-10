package com.donaway.controller.controllerpersonalmedico;

public class NotificacionObjeto {
	private String titulo;
    private String tiempo;

    public NotificacionObjeto(String titulo, String tiempo) {
        this.titulo = titulo;
        this.tiempo = tiempo;
    }
    public String getTitulo() { return titulo; }
    public String getTiempo() { return tiempo; }
}

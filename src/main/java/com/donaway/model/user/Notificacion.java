package com.donaway.model.user;

public class Notificacion {
    private String titulo;
    private String tiempo;

    public Notificacion(String titulo, String tiempo) {
        this.titulo = titulo;
        this.tiempo = tiempo;
    }
    public String getTitulo() { return titulo; }
    public String getTiempo() { return tiempo; }
}

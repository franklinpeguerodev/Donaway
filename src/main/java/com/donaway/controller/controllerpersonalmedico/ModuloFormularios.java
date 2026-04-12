package com.donaway.controller.controllerpersonalmedico;

public class ModuloFormularios {
	private String nombreFormulario;
    private String rutaFXML;

    public ModuloFormularios(String nombre, String ruta) {
        this.nombreFormulario = nombre;
        this.rutaFXML = ruta;
    }

    public String getNombreFormulario() { return nombreFormulario; }
    public String getRutaFXML() { return rutaFXML; }
}

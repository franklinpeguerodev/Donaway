package com.donaway.exception;

import javafx.scene.control.Alert;

public class Alerta {
	
	
	private Alerta() {
	}
	
	
    public static void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

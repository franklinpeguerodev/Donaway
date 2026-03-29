package com.donaway.controller.user;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import com.donaway.model.user.Laboratorio;
public class LaboratorioController {
	@FXML
	private TextField inputNombre;
	@FXML 
	private TextField inputApellido;
	@FXML
	private TextField inputEdad;
	@FXML 
	private Button btnEnviar;
	@FXML
	public void initialize() {
		System.out.println("Pantalla ingresar donantes... ");
	}
	@FXML
	public void registrarDonante(ActionEvent Event) {
	String nombre= inputNombre.getText();
	String apellido= inputApellido.getText();
	String edad = inputEdad.getText();
	if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
		System.out.println("Tienes un campo vacio");
	}
	}
}

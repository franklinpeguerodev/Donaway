package com.donaway.controller.persona;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ToggleGroup;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.donaway.utils.constants.TipoSangre;
import com.donaway.utils.constants.FactorRh;
import com.donaway.utils.constants.Sexo;
import com.donaway.utils.constants.EstadoDisponibilidad;
import com.donaway.model.persona.Donante;
import com.donaway.service.DonanteServices;
public class DonanteController {
	@FXML
	private TextField inputNombre;
	@FXML
	private TextField inputDocumentoid;
	@FXML
	private ToggleGroup grupoGenero;
	@FXML
	private TextField inputTelefono;
	@FXML 
	private DatePicker dateNaciemiento;
	@FXML
	private TextField inputEmail;
	@FXML
	private ComboBox <TipoSangre> comboTipoSangre;
	@FXML
	private ComboBox <FactorRh> comboFactorRh;
	@FXML
	private DatePicker dateRegistro;
	@FXML
	private ComboBox <EstadoDisponibilidad>comboEstado;
	@FXML
	public void initialize () {
		comboTipoSangre.getItems().setAll(TipoSangre.values());
		comboFactorRh.getItems().setAll(FactorRh.values());
		comboEstado.getItems().setAll(EstadoDisponibilidad.values());
	}
	@FXML
	public void registrarDonante(ActionEvent event) {
		String nombre = inputNombre.getText().trim();
		String documentoId = inputDocumentoid.getText().trim();
		
		if (grupoGenero.getSelectedToggle() ==null ) {
			return;
		}
		RadioButton botonSeleccionado= (RadioButton) grupoGenero.getSelectedToggle();
		String textoSexo = botonSeleccionado.getText();
		Sexo sexo = Sexo.desdeTextoFront(textoSexo);
		
		String telefono = inputTelefono.getText();
		LocalDate fechaNacimiento = dateNaciemiento.getValue();
		String email = inputEmail.getText();
		String idubi = "lksldaksldkasl";
		TipoSangre tipoSangre = comboTipoSangre.getValue();
		FactorRh factorRh = comboFactorRh.getValue();
		LocalDate fechaElegida = dateRegistro.getValue();
		LocalDateTime fechaRegistro = fechaElegida.atTime(LocalTime.now());
		LocalDateTime ultimaDonancion = null;
		EstadoDisponibilidad estadoDisponibilidad = comboEstado.getValue();
		Boolean autorizacionContacto = true;
		Donante donante = new Donante(
				documentoId,
				nombre,
				sexo,
				fechaNacimiento,
				telefono,
				email,
				fechaRegistro,
				tipoSangre,
				factorRh,
				idubi,
				ultimaDonancion,
				estadoDisponibilidad,
				autorizacionContacto
				);
		DonanteServices services = new DonanteServices(); 
		services.procesarNuevoDonante(donante);
	}



}

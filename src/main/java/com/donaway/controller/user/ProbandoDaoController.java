package com.donaway.controller.user;
import com.donaway.service.DonanteServices;
import com.donaway.model.donacion.Donante;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.donaway.service.DonanteServices;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class ProbandoDaoController {
	@FXML
	private TextField input1;
	@FXML
	private TextField input2;
	@FXML
	private TextField input3;
	@FXML
	private TextField input4;
	@FXML
	private TextField input5;
	@FXML
	private TextField input6;
	@FXML
	private TextField input7;
	@FXML
	private TextField input8;
	@FXML
	private TextField input9;
	@FXML
	private TextField input10;
	@FXML
	private TextField input11;
	@FXML
	private TextField input12;
	//@FXML
	//private TextField input13;
	@FXML
	private Button btn;
	@FXML
	public void registrarDonante(ActionEvent event) {
	      DateTimeFormatter formato= DateTimeFormatter.ofPattern("dd/MM/yyyy");
		  DateTimeFormatter formatoCompleto =DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
	      String documentId = input1.getText();
		  String nombre = input2.getText();
		  String sexo = input3.getText();
		  String fechaNacimientoTxt = input4.getText();
		  LocalDate fechaNacimiento = LocalDate.parse(fechaNacimientoTxt,formato);
		  String telefono = input5.getText();
		  String email = input6.getText();
		  String fechaRegistroTxt = input7.getText().trim();
		  LocalDateTime fechaRegistro = LocalDateTime.parse(fechaRegistroTxt,formatoCompleto);
		  String tipoSangre = input8.getText();
		  String factorRh= input9.getText();
		  String ultimaDonacion_txt = input10.getText();
		  LocalDateTime ultimaDonacion = LocalDateTime.parse(ultimaDonacion_txt,formatoCompleto);
		  String estadoDisponibilidad = input11.getText();
		  String autorizacionContactoTxt = input12.getText();
		  boolean autorizacionContacto;
		  if(autorizacionContactoTxt.equalsIgnoreCase("si")){
			  autorizacionContacto=true;
		  }else {
			  autorizacionContacto=false;
		  }
		  Donante donante = new Donante(
				  documentId,
				  nombre,
				  sexo,
				  fechaNacimiento,
				  telefono,
				  email,
				  fechaRegistro,
				  tipoSangre,
				  factorRh,
				  ultimaDonacion,
				  estadoDisponibilidad,
				  autorizacionContacto
				  );
		    DonanteServices services = new DonanteServices();
		    boolean res =services.procesarNuevoDonante(donante);
		  if (res == true) {
			  System.out.println("Ready");
			  
		  }
		}}
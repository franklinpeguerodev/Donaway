package com.donaway.controller.user;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.donaway.dto.login.LoginRequestDto;
public class UserLoginController {
@FXML
private TextField inpuntUser;
@FXML
private TextField inputPassword;

@FXML
private Button btn;

public void verificarUsuario(ActionEvent event) {
	String user= inpuntUser.getText();
	String password= inputPassword.getText();
	LoginRequestDto login = new LoginRequestDto(
			user,
			password
			);
	




















}
}

package com.donaway.controller.user;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.donaway.dto.login.LoginRequestDto;
import com.donaway.dto.login.UserSessionDto;
import com.donaway.dto.login.SessionManager; // Tu manager global
import com.donaway.dao.user.IUserDao;
import com.donaway.dao.impl.user.UserImpl;
import com.donaway.exception.Alerta;
public class UserLoginController {

    @FXML private TextField inpuntUser;
    @FXML private TextField inputPassword;
    @FXML private Button btn;

    private final IUserDao userDao = new UserImpl();

    public void verificarUsuario(ActionEvent event) {
    	System.out.println("se hizo click");
        String user = inpuntUser.getText().trim();
        String password = inputPassword.getText().trim();

        if (user.isBlank() || password.isBlank()) {
            Alerta.mostrarAlerta("Campos Vacíos", "Por favor ingrese usuario y contraseña.");
            return;
        }

        LoginRequestDto login = new LoginRequestDto(user, password);

        try {
            UserSessionDto sesion = userDao.validarLogin(login);

            if (sesion != null) {
                if ("ACTIVO".equalsIgnoreCase(sesion.estado().name())) {
                    
                    // 1. GUARDAMOS EL USUARIO EN LA MEMORIA GLOBAL
                    SessionManager.getInstance().setUsuarioActual(sesion);
                    
                    // 2. ABRIMOS LA PANTALLA QUE LE TOCA
                    cargarVistaSegunRol(sesion.cargo().name());
                    
                    // 3. CERRAMOS LA PANTALLA DE LOGIN
                    Stage ventanaActual = (Stage) btn.getScene().getWindow();
                    ventanaActual.close();
                    
                } else {
                    Alerta.mostrarAlerta("Acceso Denegado", "El usuario está inactivo.");
                }
            } else {
                Alerta.mostrarAlerta("Error", "Credenciales incorrectas.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            
            Alerta.mostrarAlerta("Error de Conexión", "Fallo al conectar con la base de datos.");
        }
    }

    // --- EL MÉTODO QUE ABRE LA VISTA CORRECTA ---
    private void cargarVistaSegunRol(String cargo) {
        String rutaFxml = "";

        // Usamos el cargo exacto que viene de tu base de datos
        switch (cargo) { 
            case "ADMIN_INSTITUCIONAL" -> rutaFxml = "";
            case "BIOANALISTA_PRINCIPAL" -> rutaFxml = "/views/vistas_genericas/plantilla_personal_medico.fxml";
            case "GESTOR_HEMODERIVADOS" -> rutaFxml = "/";
            case "RECEPCIONISTA" -> rutaFxml = "/";
            default -> rutaFxml = "";
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("DonaWay - " + cargo);
            stage.setScene(new Scene(root));
            stage.centerOnScreen(); // Para que salga bien centrada en la pantalla
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alerta.mostrarAlerta("Error de Vista", "No se pudo cargar la pantalla: " + rutaFxml);
        }
    }


}
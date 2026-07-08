package com.alvear.multigraficaalvear.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private ImageView imgLogo;
    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIngresar;

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if ("multigrafica".equals(usuario) && "alvear".equals(password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/alvear/multigraficaalvear/views/MainDashboardView.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Multigrafica Alvear - Dashboard");
                stage.setScene(new Scene(root));
                stage.show();

                Stage currentStage = (Stage) txtUsuario.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Autenticacion");
            alert.setHeaderText(null);
            alert.setContentText("Usuario o contraseña incorrectos");
            alert.showAndWait();
        }
    }
}

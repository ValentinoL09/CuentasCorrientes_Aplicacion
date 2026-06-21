package com.alvear.multigraficaalvear.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainDashboardController {

    @FXML
    private BorderPane contentArea;

    @FXML
    private void mostrarClientes() {
        cargarVista("/com/alvear/multigraficaalvear/views/ClientesView.fxml");
    }

    @FXML
    private void mostrarVentas() {
        cargarVista("/com/alvear/multigraficaalvear/views/VentasView.fxml");
    }

    @FXML
    private void mostrarCuentasCorrientes() {
        cargarVista("/com/alvear/multigraficaalvear/views/CuentasCorrientesView.fxml");
    }

    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/alvear/multigraficaalvear/views/LoginView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Multigrafica Alvear - Login");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) contentArea.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarVista(String rutaFxml) {
        try {
            javafx.scene.Node vista = FXMLLoader.load(getClass().getResource(rutaFxml));
            contentArea.setCenter(vista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

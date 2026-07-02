package com.alvear.multigraficaalvear.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainDashboardController {
    @FXML private Button btnServicios;
    @FXML private Button btnClientes;
    @FXML private Button btnVentas;
    @FXML private Button btnCuentas;
    @FXML private Button btnCaja;

    @FXML
    private BorderPane contentArea;

    @FXML
    private void mostrarClientes() {
        cargarVista("/com/alvear/multigraficaalvear/views/ClientesView.fxml");
    }

    @FXML
    private void mostrarVentas() {
        cargarVista("/com/alvear/multigraficaalvear/views/VentasView.fxml");
        marcarBotonActivo(btnVentas);
    }

    @FXML
    private void mostrarCuentasCorrientes() {
        cargarVista("/com/alvear/multigraficaalvear/views/CuentasCorrientesView.fxml");
        marcarBotonActivo(btnCuentas);
    }

    @FXML
    private void mostrarServicios() {
        cargarVista("/com/alvear/multigraficaalvear/views/ServiciosView.fxml");
        marcarBotonActivo(btnServicios);
    }

    @FXML
    private void mostrarCajaDiaria() {
        cargarVista("/com/alvear/multigraficaalvear/views/CajaDiariaView.fxml");
        marcarBotonActivo(btnCaja);
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
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setMaximized(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void marcarBotonActivo(Button botonActivo) {
        // 1. Le sacamos la clase "active" a todos
        btnServicios.getStyleClass().remove("active");
        btnClientes.getStyleClass().remove("active");
        btnVentas.getStyleClass().remove("active");
        btnCuentas.getStyleClass().remove("active");
        btnCaja.getStyleClass().remove("active");
        
        // 2. Se la ponemos solo al que tocamos
        botonActivo.getStyleClass().add("active");
    }
}

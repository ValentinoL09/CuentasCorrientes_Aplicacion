package com.alvear.multigraficaalvear.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.nio.file.Path;
import java.nio.file.Paths;

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
        marcarBotonActivo(btnClientes);
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
    private void respaldarBaseDeDatos() {
        try {
            // 1. Configuramos la ventana de Windows para elegir dónde guardar
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar Copia de Seguridad");
            
            // 2. Nombre por defecto (Ej: Multigrafica_Backup_2026-07-06.db)
            String fechaHoy = LocalDate.now().toString();
            fileChooser.setInitialFileName("Multigrafica_Backup_" + fechaHoy + ".db");
            
            // 3. Filtro para que solo permita guardar extensiones .db
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Archivos de Base de Datos (*.db)", "*.db");
            fileChooser.getExtensionFilters().add(extFilter);

            // 4. Abrimos la ventana
            File fileDestino = fileChooser.showSaveDialog(null);

            // 5. Si el usuario eligió un lugar y no apretó "Cancelar"
            if (fileDestino != null) {
                // Buscamos el archivo real de tu proyecto
                Path origen = Paths.get("multigrafica.db");
                Path destino = fileDestino.toPath();

                // Copiamos el archivo
                Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Backup Exitoso");
                alert.setHeaderText(null);
                alert.setContentText("La copia de seguridad se guardó correctamente en:\n" + fileDestino.getAbsolutePath());
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error en el Backup");
            alert.setHeaderText(null);
            alert.setContentText("Hubo un problema al crear la copia de seguridad:\n" + e.getMessage());
            alert.showAndWait();
        }
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

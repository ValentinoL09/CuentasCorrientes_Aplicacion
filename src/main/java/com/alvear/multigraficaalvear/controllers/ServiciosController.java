package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.ServicioDAO;
import com.alvear.multigraficaalvear.models.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiciosController implements Initializable {

    @FXML
    public void initialize() {
        // Le decimos a cada columna qué atributo de tu clase "Servicio" tiene que leer
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // Acá llamás al método que ya tenías para traer los datos de tu base de datos
        cargarServiciosEnTabla(); 
    }

    private void cargarServiciosEnTabla() {
        // Tu lógica vieja que hace el SELECT a la base de datos y llena un ObservableList
        // ...
        // tablaServicios.setItems(listaDeServicios);
    }

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtCategoria;
    @FXML
    private TextField txtPrecioSugerido;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private TableView<Servicio> tblServicios;
    @FXML
    private TableColumn<Servicio, Integer> colId;
    @FXML
    private TableColumn<Servicio, String> colNombre;
    @FXML
    private TableColumn<Servicio, String> colCategoria;
    @FXML
    private TableColumn<Servicio, Double> colPrecio;

    private final ServicioDAO servicioDAO;
    private ObservableList<Servicio> listaServicios;

    public ServiciosController() {
        this.servicioDAO = new ServicioDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioSugerido"));

        cargarTabla();

        tblServicios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombre.setText(newSelection.getNombre());
                txtCategoria.setText(newSelection.getCategoria());
                txtPrecioSugerido.setText(String.valueOf(newSelection.getPrecioSugerido()));
            }
        });
    }

    private void cargarTabla() {
        listaServicios = FXCollections.observableArrayList(servicioDAO.listarTodos());
        tblServicios.setItems(listaServicios);
    }

    @FXML
    private void agregarServicio() {
        try {
            double precio = Double.parseDouble(txtPrecioSugerido.getText());
            Servicio servicio = new Servicio();
            servicio.setNombre(txtNombre.getText());
            servicio.setCategoria(txtCategoria.getText());
            servicio.setPrecioSugerido(precio);

            servicioDAO.insertar(servicio);
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Servicio agregado correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta("El precio sugerido debe ser un numero valido.");
        }
    }

    @FXML
    private void actualizarServicio() {
        Servicio servicioSeleccionado = tblServicios.getSelectionModel().getSelectedItem();
        if (servicioSeleccionado != null) {
            try {
                double precio = Double.parseDouble(txtPrecioSugerido.getText());
                servicioSeleccionado.setNombre(txtNombre.getText());
                servicioSeleccionado.setCategoria(txtCategoria.getText());
                servicioSeleccionado.setPrecioSugerido(precio);

                servicioDAO.actualizar(servicioSeleccionado);
                limpiarCampos();
                cargarTabla();
                mostrarAlerta("Servicio actualizado correctamente.");
            } catch (NumberFormatException e) {
                mostrarAlerta("El precio sugerido debe ser un numero valido.");
            }
        } else {
            mostrarAlerta("Por favor, seleccione un servicio de la tabla.");
        }
    }

    @FXML
    private void eliminarServicio() {
        Servicio servicioSeleccionado = tblServicios.getSelectionModel().getSelectedItem();
        if (servicioSeleccionado != null) {
            servicioDAO.eliminar(servicioSeleccionado.getId());
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Servicio eliminado correctamente.");
        } else {
            mostrarAlerta("Por favor, seleccione un servicio de la tabla.");
        }
    }

    @FXML
    private void limpiarCampos() {
        txtNombre.clear();
        txtCategoria.clear();
        txtPrecioSugerido.clear();
        tblServicios.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.TipoVentaDAO;
import com.alvear.multigraficaalvear.models.TipoVenta;
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

public class TiposVentaController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TableView<TipoVenta> tblTiposVenta;
    @FXML
    private TableColumn<TipoVenta, Integer> colId;
    @FXML
    private TableColumn<TipoVenta, String> colNombre;

    private final TipoVentaDAO tipoVentaDAO;
    private ObservableList<TipoVenta> listaTiposVenta;

    public TiposVentaController() {
        this.tipoVentaDAO = new TipoVentaDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        cargarTabla();

        tblTiposVenta.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombre.setText(newSelection.getNombre());
            }
        });
    }

    private void cargarTabla() {
        listaTiposVenta = FXCollections.observableArrayList(tipoVentaDAO.obtenerTodos());
        tblTiposVenta.setItems(listaTiposVenta);
    }

    @FXML
    private void guardarTipoVenta() {
        TipoVenta tipoVenta = new TipoVenta();
        tipoVenta.setNombre(txtNombre.getText());

        tipoVentaDAO.insertar(tipoVenta);
        limpiarCampos();
        cargarTabla();
        mostrarAlerta("Tipo de venta guardado correctamente.");
    }

    @FXML
    private void modificarTipoVenta() {
        TipoVenta tipoSeleccionado = tblTiposVenta.getSelectionModel().getSelectedItem();
        if (tipoSeleccionado != null) {
            tipoSeleccionado.setNombre(txtNombre.getText());

            tipoVentaDAO.actualizar(tipoSeleccionado);
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Tipo de venta modificado correctamente.");
        } else {
            mostrarAlerta("Por favor, seleccione un tipo de venta de la tabla.");
        }
    }

    @FXML
    private void eliminarTipoVenta() {
        TipoVenta tipoSeleccionado = tblTiposVenta.getSelectionModel().getSelectedItem();
        if (tipoSeleccionado != null) {
            tipoVentaDAO.eliminar(tipoSeleccionado.getId());
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Tipo de venta eliminado correctamente.");
        } else {
            mostrarAlerta("Por favor, seleccione un tipo de venta de la tabla.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        tblTiposVenta.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

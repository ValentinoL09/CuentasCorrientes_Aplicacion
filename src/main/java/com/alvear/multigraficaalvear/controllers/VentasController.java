package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.ClienteDAO;
import com.alvear.multigraficaalvear.daos.TipoVentaDAO;
import com.alvear.multigraficaalvear.daos.VentaDAO;
import com.alvear.multigraficaalvear.models.Cliente;
import com.alvear.multigraficaalvear.models.TipoVenta;
import com.alvear.multigraficaalvear.models.Venta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class VentasController implements Initializable {

    @FXML
    private ComboBox<Cliente> cmbCliente;
    @FXML
    private ComboBox<TipoVenta> cmbTipoVenta;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtMontoTotal;
    @FXML
    private TextField txtMontoRecibido;
    @FXML
    private Button btnRegistrar;
    @FXML
    private TableView<Venta> tblVentas;
    @FXML
    private TableColumn<Venta, Integer> colId;
    @FXML
    private TableColumn<Venta, Integer> colCliente;
    @FXML
    private TableColumn<Venta, String> colDescripcion;
    @FXML
    private TableColumn<Venta, Double> colMontoTotal;
    @FXML
    private TableColumn<Venta, Double> colMontoRecibido;
    @FXML
    private TableColumn<Venta, LocalDate> colFecha;

    private final VentaDAO ventaDAO;
    private final ClienteDAO clienteDAO;
    private final TipoVentaDAO tipoVentaDAO;

    public VentasController() {
        this.ventaDAO = new VentaDAO();
        this.clienteDAO = new ClienteDAO();
        this.tipoVentaDAO = new TipoVentaDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colMontoTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        colMontoRecibido.setCellValueFactory(new PropertyValueFactory<>("montoRecibido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        cargarClientes();
        cargarTiposVenta();
        cargarTabla();
    }

    private void cargarClientes() {
        ObservableList<Cliente> clientes = FXCollections.observableArrayList(clienteDAO.obtenerTodos());
        cmbCliente.setItems(clientes);
    }

    private void cargarTiposVenta() {
        ObservableList<TipoVenta> tipos = FXCollections.observableArrayList(tipoVentaDAO.obtenerTodos());
        cmbTipoVenta.setItems(tipos);
    }

    private void cargarTabla() {
        ObservableList<Venta> ventas = FXCollections.observableArrayList(ventaDAO.obtenerTodas());
        tblVentas.setItems(ventas);
    }

    @FXML
    private void registrarVenta() {
        if (cmbCliente.getValue() == null || cmbTipoVenta.getValue() == null ||
            txtDescripcion.getText().isEmpty() || txtMontoTotal.getText().isEmpty() ||
            txtMontoRecibido.getText().isEmpty()) {
            mostrarAlerta("Por favor, complete todos los campos.");
            return;
        }

        try {
            double montoTotal = Double.parseDouble(txtMontoTotal.getText());
            double montoRecibido = Double.parseDouble(txtMontoRecibido.getText());

            Venta venta = new Venta();
            venta.setClienteId(cmbCliente.getValue().getId());
            venta.setTipoVentaId(cmbTipoVenta.getValue().getId());
            venta.setDescripcion(txtDescripcion.getText());
            venta.setMontoTotal(montoTotal);
            venta.setMontoRecibido(montoRecibido);
            venta.setFecha(LocalDate.now());

            ventaDAO.insertar(venta);
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Venta registrada correctamente.");
        } catch (NumberFormatException e) {
            mostrarAlerta("Los montos deben ser numericos.");
        }
    }

    private void limpiarCampos() {
        cmbCliente.getSelectionModel().clearSelection();
        cmbTipoVenta.getSelectionModel().clearSelection();
        txtDescripcion.clear();
        txtMontoTotal.clear();
        txtMontoRecibido.clear();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

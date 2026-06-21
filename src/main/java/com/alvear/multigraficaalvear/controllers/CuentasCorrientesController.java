package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.PagoDAO;
import com.alvear.multigraficaalvear.daos.VentaDAO;
import com.alvear.multigraficaalvear.models.Pago;
import com.alvear.multigraficaalvear.models.Venta;
import javafx.beans.property.SimpleDoubleProperty;
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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CuentasCorrientesController implements Initializable {

    @FXML
    private TableView<Venta> tblVentas;
    @FXML
    private TableColumn<Venta, Integer> colIdVenta;
    @FXML
    private TableColumn<Venta, Integer> colCliente;
    @FXML
    private TableColumn<Venta, Double> colTotal;
    @FXML
    private TableColumn<Venta, Double> colRecibido;
    @FXML
    private TableColumn<Venta, Double> colSaldoPendiente;
    @FXML
    private TextField txtMontoPagar;
    @FXML
    private Button btnRegistrarPago;

    private final VentaDAO ventaDAO;
    private final PagoDAO pagoDAO;
    private ObservableList<Venta> listaVentasImpagas;

    public CuentasCorrientesController() {
        this.ventaDAO = new VentaDAO();
        this.pagoDAO = new PagoDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colIdVenta.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("clienteId"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("montoTotal"));
        colRecibido.setCellValueFactory(new PropertyValueFactory<>("montoRecibido"));
        colSaldoPendiente.setCellValueFactory(cellData -> {
            double saldo = cellData.getValue().getMontoTotal() - cellData.getValue().getMontoRecibido();
            return new SimpleDoubleProperty(saldo).asObject();
        });

        cargarTabla();
    }

    private void cargarTabla() {
        listaVentasImpagas = FXCollections.observableArrayList(
                ventaDAO.obtenerTodas().stream()
                        .filter(v -> v.getMontoTotal() > v.getMontoRecibido())
                        .collect(Collectors.toList())
        );
        tblVentas.setItems(listaVentasImpagas);
    }

    @FXML
    private void registrarPago() {
        Venta ventaSeleccionada = tblVentas.getSelectionModel().getSelectedItem();
        if (ventaSeleccionada == null) {
            mostrarAlerta("Por favor, seleccione una venta de la tabla.");
            return;
        }

        double montoPagar;
        try {
            montoPagar = Double.parseDouble(txtMontoPagar.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("El monto a pagar debe ser un numero valido.");
            return;
        }

        if (montoPagar <= 0) {
            mostrarAlerta("El monto a pagar debe ser mayor a 0.");
            return;
        }

        double saldoPendiente = ventaSeleccionada.getMontoTotal() - ventaSeleccionada.getMontoRecibido();
        if (montoPagar > saldoPendiente) {
            mostrarAlerta("El monto a pagar no puede superar el saldo pendiente.");
            return;
        }

        Pago pago = new Pago();
        pago.setVentaId(ventaSeleccionada.getId());
        pago.setMontoPagado(montoPagar);
        pago.setFecha(LocalDate.now());
        pagoDAO.insertar(pago);

        ventaSeleccionada.setMontoRecibido(ventaSeleccionada.getMontoRecibido() + montoPagar);
        ventaDAO.actualizar(ventaSeleccionada);

        txtMontoPagar.clear();
        cargarTabla();
        mostrarAlerta("Pago registrado correctamente.");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

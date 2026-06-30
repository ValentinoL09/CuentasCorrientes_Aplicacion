package com.alvear.multigraficaalvear.controllers;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.alvear.multigraficaalvear.daos.*;
import com.alvear.multigraficaalvear.models.*;

public class VentasController implements Initializable {

    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<TipoVenta> cmbTipoVenta;
    @FXML private ComboBox<Servicio> cmbServicios;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private Button btnAgregar;
    @FXML private TableView<DetalleVenta> tblCarrito;
    @FXML private TableColumn<DetalleVenta, String> colServicio;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotal;
    @FXML private Label lblTotal;
    @FXML private TextField txtMontoRecibido;
    @FXML private Button btnFinalizar;

    private final ClienteDAO clienteDAO;
    private final ServicioDAO servicioDAO;
    private final TipoVentaDAO tipoVentaDAO;
    private final VentaDAO ventaDAO;
    private final DetalleVentaDAO detalleVentaDAO;
    private final PagoDAO pagoDAO;

    private ObservableList<DetalleVenta> carrito;

    public VentasController() {
        this.clienteDAO = new ClienteDAO();
        this.servicioDAO = new ServicioDAO();
        this.tipoVentaDAO = new TipoVentaDAO();
        this.ventaDAO = new VentaDAO();
        this.detalleVentaDAO = new DetalleVentaDAO();
        this.pagoDAO = new PagoDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carrito = FXCollections.observableArrayList();
        tblCarrito.setItems(carrito);

        colServicio.setCellValueFactory(new PropertyValueFactory<>("nombreServicio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        cargarClientes();
        cargarTipoVenta();
        cargarServicios();
    }

    private void cargarClientes() {
        cmbCliente.setItems(FXCollections.observableArrayList(clienteDAO.obtenerTodos()));
    }

    private void cargarTipoVenta() {
        cmbTipoVenta.setItems(FXCollections.observableArrayList(tipoVentaDAO.obtenerTodos()));
    }

    private void cargarServicios() {
        cmbServicios.setItems(FXCollections.observableArrayList(servicioDAO.listarTodos()));
        cmbServicios.setOnAction(e -> {
            Servicio s = cmbServicios.getValue();
            if (s != null) txtPrecio.setText(String.valueOf(s.getPrecioSugerido()));
        });
    }

    @FXML
    private void agregarAlCarrito() {
        Servicio s = cmbServicios.getValue();
        if (s == null) { mostrarAlerta("Seleccione un servicio."); return; }
        int cantidad; double precio;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText());
            precio = Double.parseDouble(txtPrecio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Cantidad y precio deben ser numericos."); return;
        }

        DetalleVenta d = new DetalleVenta();
        d.setServicioId(s.getId());
        d.setNombreServicio(s.getNombre());
        d.setCantidad(cantidad);
        d.setPrecioUnitario(precio);
        carrito.add(d);
        actualizarTotal();
        limpiarCamposProducto();
    }

    private void actualizarTotal() {
        double total = carrito.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    private void limpiarCamposProducto() {
        cmbServicios.getSelectionModel().clearSelection();
        txtCantidad.clear();
        txtPrecio.clear();
    }

    @FXML
    private void finalizarVenta() {
        if (cmbCliente.getValue() == null || cmbTipoVenta.getValue() == null) {
            mostrarAlerta("Seleccione cliente y tipo de venta."); return;
        }
        if (carrito.isEmpty()) { mostrarAlerta("El carrito esta vacio."); return; }

        double total = carrito.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
        double montoRecibido;
        try {
            montoRecibido = txtMontoRecibido.getText().isEmpty() ? 0.0 : Double.parseDouble(txtMontoRecibido.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Monto recibido invalido."); return;
        }

        Venta v = new Venta();
        v.setClienteId(cmbCliente.getValue().getId());
        v.setTipoVentaId(cmbTipoVenta.getValue().getId());
        v.setDescripcion("Venta de " + String.valueOf(carrito.size()) + " items");
        v.setMontoTotal(total);
        v.setMontoRecibido(montoRecibido);
        v.setFecha(LocalDate.now());

        int idVenta = ventaDAO.insertar(v);
        if (idVenta == -1) { mostrarAlerta("Error al registrar la venta."); return; }

        for (DetalleVenta d : carrito) {
            d.setVentaId(idVenta);
            detalleVentaDAO.insertar(d);
        }

        if (montoRecibido > 0) {
            Pago p = new Pago(idVenta, montoRecibido, LocalDate.now());
            pagoDAO.insertar(p);
        }

        carrito.clear();
        actualizarTotal();
        limpiarCamposProducto();
        cmbCliente.getSelectionModel().clearSelection();
        cmbTipoVenta.getSelectionModel().clearSelection();
        txtMontoRecibido.clear();
        mostrarAlerta("Venta finalizada correctamente.");
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

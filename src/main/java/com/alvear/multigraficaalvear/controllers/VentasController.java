package com.alvear.multigraficaalvear.controllers;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.alvear.multigraficaalvear.daos.*;
import com.alvear.multigraficaalvear.models.*;

public class VentasController implements Initializable {

    // --- CONTENEDORES DE PANTALLA ---
    @FXML private VBox panelNuevaVenta;
    @FXML private VBox panelHistorial;
    @FXML private Button btnNavNuevaVenta;
    @FXML private Button btnNavHistorial;

    // --- ELEMENTOS DE NUEVA VENTA ---
    @FXML private ComboBox<Cliente> cmbCliente;
    @FXML private ComboBox<Servicio> cmbServicios;
    @FXML private TextField txtCantidad;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtDetalle; 
    @FXML private TextField txtMontoRecibido;
    @FXML private Label lblTotal;
    
    @FXML private TableView<DetalleVenta> tblCarrito;
    @FXML private TableColumn<DetalleVenta, String> colServicio;
    @FXML private TableColumn<DetalleVenta, Integer> colCantidad;
    @FXML private TableColumn<DetalleVenta, Double> colPrecioUnitario;
    @FXML private TableColumn<DetalleVenta, Double> colSubtotal;

    // --- ELEMENTOS DEL HISTORIAL ---
    @FXML private TableView<Venta> tblHistorial;
    @FXML private TableColumn<Venta, Integer> colHistId;
    @FXML private TableColumn<Venta, String> colHistFecha;
    @FXML private TableColumn<Venta, String> colHistCliente;
    @FXML private TableColumn<Venta, Double> colHistTotal;
    @FXML private TableColumn<Venta, String> colHistDetalle;

    // --- ELEMENTOS DEL DETALLE DEL HISTORIAL (TABLA SECUNDARIA) ---
    @FXML private TableView<DetalleVenta> tblDetalleHistorial;
    @FXML private TableColumn<DetalleVenta, Integer> colDetHistCant;
    @FXML private TableColumn<DetalleVenta, String> colDetHistServicio;
    @FXML private TableColumn<DetalleVenta, Double> colDetHistPrecio;
    @FXML private TableColumn<DetalleVenta, Double> colDetHistSub;

    private final ClienteDAO clienteDAO;
    private final ServicioDAO servicioDAO;
    private final VentaDAO ventaDAO;
    private final DetalleVentaDAO detalleVentaDAO;
    private final PagoDAO pagoDAO;

    private ObservableList<DetalleVenta> carrito;
    private ObservableList<Venta> listaHistorial;

    public VentasController() {
        this.clienteDAO = new ClienteDAO();
        this.servicioDAO = new ServicioDAO();
        this.ventaDAO = new VentaDAO();
        this.detalleVentaDAO = new DetalleVentaDAO();
        this.pagoDAO = new PagoDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carrito = FXCollections.observableArrayList();
        tblCarrito.setItems(carrito);

        // Columnas Carrito
        colServicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombreServicio()));
        colCantidad.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCantidad()));
        colPrecioUnitario.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrecioUnitario()));
        colSubtotal.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getSubtotal()));

        // Columnas Historial (Maestro)
        colHistId.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));
        colHistFecha.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFecha().toString()));
        colHistCliente.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombreCliente()));
        colHistTotal.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getMontoTotal()));
        colHistDetalle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDetalle()));

        // Columnas Detalle del Historial (Esclavo)
        colDetHistCant.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getCantidad()));
        colDetHistServicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNombreServicio()));
        colDetHistPrecio.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getPrecioUnitario()));
        colDetHistSub.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getSubtotal()));

        cargarClientes();
        cargarServicios();
        cargarHistorial();

        // ACÁ ESCUCHAMOS LOS CLICS EN EL HISTORIAL PARA LLENAR LA TABLA SECUNDARIA
        tblHistorial.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                cargarDetallesDeVentaSeleccionada(newSel.getId());
            } else {
                tblDetalleHistorial.getItems().clear(); // Si no hay nada seleccionado, limpiamos
            }
        });

        // Aseguramos que arranque mostrando la pantalla de Nueva Venta
        mostrarNuevaVenta();
    }

    // --- MÉTODOS DE NAVEGACIÓN ---
    @FXML
    private void mostrarNuevaVenta() {
        panelNuevaVenta.setVisible(true);
        panelNuevaVenta.setManaged(true);
        panelHistorial.setVisible(false);
        panelHistorial.setManaged(false);

        // Pintamos de morado el botón activo y de gris el inactivo
        btnNavNuevaVenta.getStyleClass().remove("btn-form-secundario");
        if (!btnNavNuevaVenta.getStyleClass().contains("btn-form-primario")) {
            btnNavNuevaVenta.getStyleClass().add("btn-form-primario");
        }

        btnNavHistorial.getStyleClass().remove("btn-form-primario");
        if (!btnNavHistorial.getStyleClass().contains("btn-form-secundario")) {
            btnNavHistorial.getStyleClass().add("btn-form-secundario");
        }
    }

    @FXML
    private void mostrarHistorial() {
        panelNuevaVenta.setVisible(false);
        panelNuevaVenta.setManaged(false);
        panelHistorial.setVisible(true);
        panelHistorial.setManaged(true);
        cargarHistorial(); // Refrescamos por si hubo ventas nuevas

        // Invertimos los colores: Historial pasa a morado, Nueva Venta a gris
        btnNavHistorial.getStyleClass().remove("btn-form-secundario");
        if (!btnNavHistorial.getStyleClass().contains("btn-form-primario")) {
            btnNavHistorial.getStyleClass().add("btn-form-primario");
        }

        btnNavNuevaVenta.getStyleClass().remove("btn-form-primario");
        if (!btnNavNuevaVenta.getStyleClass().contains("btn-form-secundario")) {
            btnNavNuevaVenta.getStyleClass().add("btn-form-secundario");
        }
    }

    // --- MÉTODOS DE DATOS ---
    private void cargarClientes() {
        cmbCliente.setItems(FXCollections.observableArrayList(clienteDAO.obtenerTodos()));
    }

    private void cargarServicios() {
        cmbServicios.setItems(FXCollections.observableArrayList(servicioDAO.listarTodos()));
        cmbServicios.setOnAction(e -> {
            Servicio s = cmbServicios.getValue();
            if (s != null) txtPrecio.setText(String.valueOf(s.getPrecioSugerido()));
        });
    }

    private void cargarHistorial() {
        listaHistorial = FXCollections.observableArrayList(ventaDAO.obtenerTodas());
        tblHistorial.setItems(listaHistorial);
    }

    private void cargarDetallesDeVentaSeleccionada(int ventaId) {
        ObservableList<DetalleVenta> detalles = FXCollections.observableArrayList(detalleVentaDAO.listarPorVenta(ventaId));
        tblDetalleHistorial.setItems(detalles);
    }

    @FXML
    private void agregarAlCarrito() {
        if (txtCantidad.getText().trim().isEmpty() || txtPrecio.getText().trim().isEmpty()) {
            mostrarAlerta("Llene cantidad y precio.", Alert.AlertType.WARNING);
            return;
        }

        Servicio s = cmbServicios.getValue();
        if (s == null) { mostrarAlerta("Seleccione un servicio.", Alert.AlertType.WARNING); return; }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            DetalleVenta d = new DetalleVenta();
            d.setServicioId(s.getId());
            d.setNombreServicio(s.getNombre());
            d.setCantidad(cantidad);
            d.setPrecioUnitario(precio);
            carrito.add(d);
            
            actualizarTotal();
            txtCantidad.clear();
            txtPrecio.clear();
            cmbServicios.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            mostrarAlerta("Formato de número incorrecto en cantidad o precio.", Alert.AlertType.WARNING);
        }
    }

    private void actualizarTotal() {
        double total = carrito.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
    }

    @FXML
    private void limpiarFormulario() {
        txtCantidad.clear();
        txtPrecio.clear();
        txtMontoRecibido.clear();
        txtDetalle.clear();
        cmbCliente.getSelectionModel().clearSelection();
        cmbServicios.getSelectionModel().clearSelection();
        carrito.clear();
        lblTotal.setText("Total: $0.00");
    }

    @FXML
    private void finalizarVenta() {
        if (cmbCliente.getValue() == null) {
            mostrarAlerta("Seleccione un cliente.", Alert.AlertType.WARNING);
            return;
        }
        if (carrito.isEmpty()) {
            mostrarAlerta("El carrito está vacío.", Alert.AlertType.WARNING);
            return;
        }

        double total = carrito.stream().mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario()).sum();
        double montoRecibido = 0.0;
        
        if (!txtMontoRecibido.getText().trim().isEmpty()) {
            try {
                montoRecibido = Double.parseDouble(txtMontoRecibido.getText().trim());
            } catch (NumberFormatException e) {
                mostrarAlerta("El monto recibido debe ser un número.", Alert.AlertType.WARNING);
                return;
            }
        }

        Venta v = new Venta();
        v.setClienteId(cmbCliente.getValue().getId());
        v.setDescripcion("Venta de " + carrito.size() + " items");
        v.setMontoTotal(total);
        v.setMontoRecibido(montoRecibido);
        v.setFecha(LocalDate.now());
        v.setDetalle(txtDetalle.getText());

        int idVenta = ventaDAO.insertar(v);
        if (idVenta == -1) {
            mostrarAlerta("Error al registrar la venta en la BD.", Alert.AlertType.ERROR);
            return;
        }

        for (DetalleVenta d : carrito) {
            d.setVentaId(idVenta);
            detalleVentaDAO.insertar(d);
        }

        if (montoRecibido > 0) {
            Pago p = new Pago(idVenta, montoRecibido, LocalDate.now());
            pagoDAO.insertar(p);
        }

        limpiarFormulario();
        mostrarAlerta("Venta finalizada con éxito.", Alert.AlertType.INFORMATION);
    }

    private void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

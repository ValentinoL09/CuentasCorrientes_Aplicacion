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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import com.alvear.multigraficaalvear.utils.FormatoUtil;
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
    @FXML private DatePicker dpFechaVenta;
    
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

        // --- APLICAMOS EL FORMATO DE DINERO A LAS COLUMNAS ---
        configurarColumnaDinero(colPrecioUnitario);
        configurarColumnaDinero(colSubtotal);
        configurarColumnaDinero(colHistTotal);
        configurarColumnaDinero(colDetHistPrecio);
        configurarColumnaDinero(colDetHistSub);

        cargarClientes();
        cargarServicios();
        cargarHistorial();

        dpFechaVenta.setValue(LocalDate.now());

        FormatoUtil.aplicarFormatoNumerico(txtCantidad);
        FormatoUtil.aplicarFormatoNumerico(txtPrecio);
        FormatoUtil.aplicarFormatoNumerico(txtMontoRecibido);
        
        // Aplicamos la mayúscula inicial al campo de detalle de la venta
        FormatoUtil.aplicarFormatoMayusculas(txtDetalle);

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
        
        // Cambiamos cómo se ve cada elemento desplegado en la lista
        cmbServicios.setCellFactory(param -> new ListCell<Servicio>() {
            @Override
            protected void updateItem(Servicio servicio, boolean empty) {
                super.updateItem(servicio, empty);
                if (empty || servicio == null) {
                    setText(null);
                } else {
                    setText(servicio.getNombre() + " (" + servicio.getCategoria() + ")");
                }
            }
        });

        // Cambiamos cómo se ve el elemento seleccionado una vez elegido
        cmbServicios.setConverter(new javafx.util.StringConverter<Servicio>() {
            @Override
            public String toString(Servicio servicio) {
                return servicio == null ? "" : servicio.getNombre() + " (" + servicio.getCategoria() + ")";
            }
            @Override
            public Servicio fromString(String string) {
                return null;
            }
        });

        cmbServicios.setOnAction(e -> {
            Servicio s = cmbServicios.getValue();
            if (s != null) txtPrecio.setText(String.valueOf((long) s.getPrecioSugerido()));
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
            int cantidad = (int) FormatoUtil.obtenerValor(txtCantidad);
            double precio = FormatoUtil.obtenerValor(txtPrecio);

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
        
        // Formateamos también el texto del Total con puntos
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator('.');
        DecimalFormat formato = new DecimalFormat("#,###", simbolos);
        lblTotal.setText("Total: $" + formato.format(total));
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
        lblTotal.setText("Total: $0");
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
                montoRecibido = FormatoUtil.obtenerValor(txtMontoRecibido);
            } catch (NumberFormatException e) {
                mostrarAlerta("El monto recibido debe ser un número.", Alert.AlertType.WARNING);
                return;
            }
        }

        LocalDate fechaSeleccionada = dpFechaVenta.getValue() != null ? dpFechaVenta.getValue() : LocalDate.now();

        Venta v = new Venta();
        v.setClienteId(cmbCliente.getValue().getId());
        v.setDescripcion("Venta de " + carrito.size() + " items");
        v.setMontoTotal(total);
        v.setMontoRecibido(montoRecibido);
        v.setFecha(fechaSeleccionada);
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
            Pago p = new Pago(idVenta, montoRecibido, fechaSeleccionada);
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

    // --- MÉTODOS DE AYUDA (Fábrica de celdas visuales) ---
    private <T> void configurarColumnaDinero(TableColumn<T, Double> columna) {
        columna.setCellFactory(col -> new TableCell<T, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                    simbolos.setGroupingSeparator('.');
                    DecimalFormat formato = new DecimalFormat("#,###", simbolos);
                    setText("$ " + formato.format(item));
                }
            }
        });
    }
}

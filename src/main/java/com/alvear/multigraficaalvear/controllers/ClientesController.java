package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.ClienteDAO;
import com.alvear.multigraficaalvear.models.Cliente;
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

public class ClientesController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCuit;
    @FXML
    private TextField txtCorreo;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TableView<Cliente> tblClientes;
    @FXML
    private TableColumn<Cliente, Integer> colId;
    @FXML
    private TableColumn<Cliente, String> colNombre;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TableColumn<Cliente, String> colCuit;
    @FXML
    private TableColumn<Cliente, String> colCorreo;

    private final ClienteDAO clienteDAO;
    private ObservableList<Cliente> listaClientes;

    public ClientesController() {
        this.clienteDAO = new ClienteDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        cargarTabla();

        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombre.setText(newSelection.getNombreCliente());
                txtTelefono.setText(newSelection.getTelefono());
                txtCuit.setText(newSelection.getCuit());
                txtCorreo.setText(newSelection.getCorreo());
            }
        });
    }

    private void cargarTabla() {
        listaClientes = FXCollections.observableArrayList(clienteDAO.obtenerTodos());
        tblClientes.setItems(listaClientes);
    }

    @FXML
    private void guardarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombreCliente(txtNombre.getText());
        cliente.setTelefono(txtTelefono.getText());
        cliente.setCuit(txtCuit.getText());
        cliente.setCorreo(txtCorreo.getText());

        clienteDAO.insertar(cliente);
        limpiarCampos();
        cargarTabla();
        mostrarAlerta("Cliente guardado correctamente.");
    }

    @FXML
    private void modificarCliente() {
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            clienteSeleccionado.setNombreCliente(txtNombre.getText());
            clienteSeleccionado.setTelefono(txtTelefono.getText());
            clienteSeleccionado.setCuit(txtCuit.getText());
            clienteSeleccionado.setCorreo(txtCorreo.getText());

            clienteDAO.actualizar(clienteSeleccionado);
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Cliente modificado correctamente.");
        } else {
            mostrarAlerta("Por favor, seleccione un cliente de la tabla.");
        }
    }

    @FXML
    private void eliminarCliente() {
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            clienteDAO.eliminar(clienteSeleccionado.getId());
            limpiarCampos();
            cargarTabla();
            mostrarAlerta("Cliente eliminado correctamente.");
        } else {
            mostrarAlerta("Por favor, seleccione un cliente de la tabla.");
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtTelefono.clear();
        txtCuit.clear();
        txtCorreo.clear();
        tblClientes.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

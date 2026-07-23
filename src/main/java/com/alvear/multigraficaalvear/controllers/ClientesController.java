package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.ClienteDAO;
import com.alvear.multigraficaalvear.models.Cliente;
import com.alvear.multigraficaalvear.utils.FormatoUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientesController implements Initializable {

    // --- CAMPOS DE TEXTO ---
    @FXML private TextField txtNombreEmpresa;
    @FXML private TextField txtEncargado;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtTelefonoAlternativo;
    @FXML private TextField txtCuit;
    @FXML private TextField txtCorreo;

    // --- BOTONES ---
    @FXML private Button btnGuardar;
    @FXML private Button btnModificar;
    @FXML private Button btnEliminar;

    // --- TABLA Y COLUMNAS ---
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNombreEmpresa;
    @FXML private TableColumn<Cliente, String> colEncargado;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colTelefonoAlternativo;
    @FXML private TableColumn<Cliente, String> colCuit;
    @FXML private TableColumn<Cliente, String> colCorreo;

    private final ClienteDAO clienteDAO;
    private ObservableList<Cliente> listaClientes;

    public ClientesController() {
        this.clienteDAO = new ClienteDAO();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Enlazamos las columnas con los nuevos atributos del modelo
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getId()));
        colNombreEmpresa.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombreEmpresa()));
        colEncargado.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEncargado()));
        colTelefono.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefono()));
        colTelefonoAlternativo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefonoAlternativo()));
        colCuit.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCuit()));
        colCorreo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCorreo()));

        cargarTabla();

        FormatoUtil.aplicarFormatoMayusculas(txtNombreEmpresa);
        FormatoUtil.aplicarFormatoMayusculas(txtEncargado);

        // Escuchador para cuando hacen clic en la tabla
        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNombreEmpresa.setText(newSelection.getNombreEmpresa());
                txtEncargado.setText(newSelection.getEncargado() != null ? newSelection.getEncargado() : "");
                txtTelefono.setText(newSelection.getTelefono());
                txtTelefonoAlternativo.setText(newSelection.getTelefonoAlternativo() != null ? newSelection.getTelefonoAlternativo() : "");
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
        // VALIDACIÓN: Obligamos a que pongan el nombre de la empresa
        if (txtNombreEmpresa.getText().trim().isEmpty()) {
            mostrarAlerta("El Nombre de la Empresa es obligatorio.");
            return; // Corta la ejecución acá, no guarda nada
        }

        Cliente cliente = new Cliente();
        cliente.setNombreEmpresa(txtNombreEmpresa.getText().trim());
        cliente.setEncargado(txtEncargado.getText().trim());
        cliente.setTelefono(txtTelefono.getText().trim());
        cliente.setTelefonoAlternativo(txtTelefonoAlternativo.getText().trim());
        cliente.setCuit(txtCuit.getText().trim());
        cliente.setCorreo(txtCorreo.getText().trim());

        clienteDAO.insertar(cliente);
        limpiarCampos();
        cargarTabla();
        mostrarAlerta("Cliente guardado correctamente.");
    }

    @FXML
    private void modificarCliente() {
        Cliente clienteSeleccionado = tblClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            
            // VALIDACIÓN también al modificar
            if (txtNombreEmpresa.getText().trim().isEmpty()) {
                mostrarAlerta("El Nombre de la Empresa no puede estar vacío.");
                return;
            }

            clienteSeleccionado.setNombreEmpresa(txtNombreEmpresa.getText().trim());
            clienteSeleccionado.setEncargado(txtEncargado.getText().trim());
            clienteSeleccionado.setTelefono(txtTelefono.getText().trim());
            clienteSeleccionado.setTelefonoAlternativo(txtTelefonoAlternativo.getText().trim());
            clienteSeleccionado.setCuit(txtCuit.getText().trim());
            clienteSeleccionado.setCorreo(txtCorreo.getText().trim());

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
        txtNombreEmpresa.clear();
        txtEncargado.clear();
        txtTelefono.clear();
        txtTelefonoAlternativo.clear();
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
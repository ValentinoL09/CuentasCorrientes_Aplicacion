package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.CajaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CajaDiariaController {

    @FXML
    private DatePicker dpFecha;

    @FXML
    private Label lblVentasContado;

    @FXML
    private Label lblCobrosCuentas;

    @FXML
    private Label lblTotalCaja;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        dpFecha.setValue(LocalDate.now());
        calcularCaja(LocalDate.now());

        dpFecha.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                calcularCaja(newValue);
            }
        });
    }

    private void calcularCaja(LocalDate fecha) {
        String formattedDate = fecha.format(formatter);

        double ventasContado = CajaDAO.getInstance().obtenerVentasContadoDelDia(formattedDate);
        double cobrosCuentas = CajaDAO.getInstance().obtenerPagosCuentasCorrientesDelDia(formattedDate);
        double totalCaja = ventasContado + cobrosCuentas;

        lblVentasContado.setText(String.format("$ %.2f", ventasContado));
        lblCobrosCuentas.setText(String.format("$ %.2f", cobrosCuentas));
        lblTotalCaja.setText(String.format("$ %.2f", totalCaja));
    }
}

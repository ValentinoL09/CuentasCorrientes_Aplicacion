package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.CajaDAO;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CajaDiariaController {

    @FXML
    private DatePicker dpFechaInicio;

    @FXML
    private DatePicker dpFechaFin;

    @FXML
    private Label lblVentasContado;

    @FXML
    private Label lblCobrosCuentas;

    @FXML
    private Label lblTotalCaja;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @FXML
    public void initialize() {
        dpFechaInicio.setValue(LocalDate.now());
        dpFechaFin.setValue(LocalDate.now());
        calcularCaja(LocalDate.now(), LocalDate.now());
    }

    @FXML
    private void calcularCajaDesdeBoton() {
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaFin = dpFechaFin.getValue();
        if (fechaInicio != null && fechaFin != null) {
            calcularCaja(fechaInicio, fechaFin);
        }
    }

    private void calcularCaja(LocalDate fechaInicio, LocalDate fechaFin) {
        String inicio = fechaInicio.format(formatter);
        String fin = fechaFin.format(formatter);

        double ventasContado = CajaDAO.getInstance().obtenerVentasPorRango(inicio, fin);
        double cobrosCuentas = CajaDAO.getInstance().obtenerPagosCuentasCorrientesPorRango(inicio, fin);
        double totalCaja = ventasContado + cobrosCuentas;

        lblVentasContado.setText(String.format("$ %.2f", ventasContado));
        lblCobrosCuentas.setText(String.format("$ %.2f", cobrosCuentas));
        lblTotalCaja.setText(String.format("$ %.2f", totalCaja));
    }

    @FXML
    private void exportarPDF() {
        try {
            String fechaInicio = dpFechaInicio.getValue().toString();
            String fechaFin = dpFechaFin.getValue().toString();
            String nombreArchivo = "Reporte_Caja_" + fechaInicio + "_al_" + fechaFin + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();

            document.add(new Paragraph("Reporte de Cierre de Caja"));
            document.add(new Paragraph("Rango del reporte: " + fechaInicio + " al " + fechaFin));
            document.add(new Paragraph("Ventas al Contado: " + lblVentasContado.getText()));
            document.add(new Paragraph("Cobros de Cuentas Corrientes: " + lblCobrosCuentas.getText()));
            document.add(new Paragraph("Total en Caja: " + lblTotalCaja.getText()));

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Generado");
            alert.setHeaderText(null);
            alert.setContentText("PDF generado con éxito");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error al generar el PDF: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

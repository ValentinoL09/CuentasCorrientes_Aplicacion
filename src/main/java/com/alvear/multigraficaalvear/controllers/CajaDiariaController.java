package com.alvear.multigraficaalvear.controllers;

import com.alvear.multigraficaalvear.daos.CajaDAO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CajaDiariaController {

    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    
    @FXML private Label lblVentasTotales; 
    @FXML private Label lblTotalACobrar;   
    @FXML private Label lblTotalCobrado;   

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

        // 1. VENTAS TOTALES (Lo que le sale al cliente el trabajo)
        double ventasTotales = CajaDAO.getInstance().obtenerTotalFacturadoPorRango(inicio, fin);
        
        // 2. TOTAL A COBRAR (Suma de las deudas de los clientes)
        double totalACobrar = CajaDAO.getInstance().obtenerTotalACobrarPorRango(inicio, fin);

        // 3. TOTAL COBRADO (Pagos iniciales + Pagos de cuentas corrientes)
        double pagosIniciales = CajaDAO.getInstance().obtenerPagosInicialesPorRango(inicio, fin);
        double cobrosCuentas = CajaDAO.getInstance().obtenerPagosCuentasCorrientesPorRango(inicio, fin);
        double totalCobrado = pagosIniciales + cobrosCuentas; 

        // 4. Mostramos en la pantalla
        lblVentasTotales.setText(String.format("%.2f", ventasTotales));
        lblTotalACobrar.setText(String.format("%.2f", totalACobrar));
        lblTotalCobrado.setText(String.format("%.2f", totalCobrado));
    }

    @FXML
    private void exportarPDF() {
        try {
            String fechaInicio = dpFechaInicio.getValue() != null ? dpFechaInicio.getValue().toString() : LocalDate.now().toString();
            String fechaFin = dpFechaFin.getValue() != null ? dpFechaFin.getValue().toString() : LocalDate.now().toString();
            String nombreArchivo = "Reporte_Caja_" + fechaInicio + "_al_" + fechaFin + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();

            Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, BaseColor.BLACK);
            Font fuenteSubtitulo = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.DARK_GRAY);
            Font fuenteTablaHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
            Font fuenteTablaBody = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.BLACK);
            Font fuenteDestacada = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(39, 174, 96)); // Verde

            Paragraph titulo = new Paragraph("Reporte de Cierre de Caja", fuenteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(10);
            document.add(titulo);

            Paragraph fechas = new Paragraph("Período: " + fechaInicio + " hasta " + fechaFin, fuenteSubtitulo);
            fechas.setAlignment(Element.ALIGN_CENTER);
            fechas.setSpacingAfter(30);
            document.add(fechas);

            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(80);
            tabla.setWidths(new float[]{2f, 1f});

            BaseColor colorMorado = new BaseColor(138, 43, 226);
            
            PdfPCell celdaHeader1 = new PdfPCell(new Phrase("Concepto", fuenteTablaHeader));
            celdaHeader1.setBackgroundColor(colorMorado);
            celdaHeader1.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHeader1.setPadding(10);
            tabla.addCell(celdaHeader1);

            PdfPCell celdaHeader2 = new PdfPCell(new Phrase("Monto", fuenteTablaHeader));
            celdaHeader2.setBackgroundColor(colorMorado);
            celdaHeader2.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaHeader2.setPadding(10);
            tabla.addCell(celdaHeader2);

            PdfPCell celdaF1 = new PdfPCell(new Phrase("Ventas Totales", fuenteTablaBody));
            celdaF1.setPadding(10);
            tabla.addCell(celdaF1);
            PdfPCell celdaF1V = new PdfPCell(new Phrase("$ " + lblVentasTotales.getText(), fuenteTablaBody));
            celdaF1V.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaF1V.setPadding(10);
            tabla.addCell(celdaF1V);

            PdfPCell celdaF2 = new PdfPCell(new Phrase("Total a Cobrar", fuenteTablaBody));
            celdaF2.setPadding(10);
            tabla.addCell(celdaF2);
            PdfPCell celdaF2V = new PdfPCell(new Phrase("$ " + lblTotalACobrar.getText(), fuenteTablaBody));
            celdaF2V.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaF2V.setPadding(10);
            tabla.addCell(celdaF2V);

            PdfPCell celdaF3 = new PdfPCell(new Phrase("TOTAL COBRADO", fuenteDestacada));
            celdaF3.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaF3.setPadding(15);
            tabla.addCell(celdaF3);
            PdfPCell celdaF3V = new PdfPCell(new Phrase("$ " + lblTotalCobrado.getText(), fuenteDestacada));
            celdaF3V.setHorizontalAlignment(Element.ALIGN_RIGHT);
            celdaF3V.setPadding(15);
            tabla.addCell(celdaF3V);

            document.add(tabla);

            Paragraph emision = new Paragraph("\n\nReporte generado el: " + LocalDate.now().toString(), fuenteSubtitulo);
            emision.setAlignment(Element.ALIGN_RIGHT);
            document.add(emision);

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("PDF Generado exitosamente");
            alert.setHeaderText(null);
            alert.setContentText("El reporte se ha guardado correctamente como:\n" + nombreArchivo);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.alvear.multigraficaalvear.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javafx.scene.control.TextField;

public class FormatoUtil {

    // Método estático para que cualquier pantalla le ponga los puntos al TextField
    public static void aplicarFormatoNumerico(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) return;
            String numeroPuro = newValue.replace(".", "");

            if (!numeroPuro.matches("\\d*")) {
                textField.setText(oldValue);
                return;
            }

            try {
                long numero = Long.parseLong(numeroPuro);
                DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
                simbolos.setGroupingSeparator('.');
                DecimalFormat formato = new DecimalFormat("#,###", simbolos);

                String textoFormateado = formato.format(numero);

                if (!newValue.equals(textoFormateado)) {
                    textField.setText(textoFormateado);
                    textField.positionCaret(textoFormateado.length());
                }
            } catch (NumberFormatException e) {
                textField.setText(oldValue);
            }
        });
    }

    // Método estático "mágico" para limpiar el punto y convertir a Double en una sola línea
    public static double obtenerValor(TextField textField) {
        String limpio = textField.getText().replace(".", "");
        return Double.parseDouble(limpio);
    }
}
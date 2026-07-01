package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CajaDAO {

    // --- INICIO DEL SINGLETON ---
    private static CajaDAO instance;
    private final Connection connection;

    // El constructor ahora es privado
    private CajaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // El método que el controlador estaba buscando a gritos
    public static CajaDAO getInstance() {
        if (instance == null) {
            instance = new CajaDAO();
        }
        return instance;
    }
    // --- FIN DEL SINGLETON ---

    public double obtenerVentasContadoDelDia(String fecha) {
        String sql = "SELECT SUM(monto_recibido) FROM ventas WHERE fecha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fecha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double resultado = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : resultado;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double obtenerPagosCuentasCorrientesDelDia(String fecha) {
        String sql = "SELECT SUM(monto_pagado) FROM pagos_cuenta_corriente WHERE fecha = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fecha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double resultado = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : resultado;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}

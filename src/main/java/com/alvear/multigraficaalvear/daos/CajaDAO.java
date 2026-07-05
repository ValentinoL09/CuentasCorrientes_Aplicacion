package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CajaDAO {

    private static CajaDAO instance;
    private final Connection connection;

    private CajaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public static CajaDAO getInstance() {
        if (instance == null) {
            instance = new CajaDAO();
        }
        return instance;
    }

    // --- MÉTODOS PARA EL DÍA ---
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

    // --- MÉTODOS PARA RANGOS DE FECHAS (Cierre de Caja) ---
    
    // 1. VENTAS TOTALES: Suma el valor total de los trabajos
    public double obtenerTotalFacturadoPorRango(String fechaInicio, String fechaFin) {
        String sql = "SELECT SUM(monto_total) FROM ventas WHERE fecha BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
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

    // 2. TOTAL A COBRAR: Calcula la deuda exacta (Total del trabajo - Todo lo que ya se pagó)
    public double obtenerTotalACobrarPorRango(String fechaInicio, String fechaFin) {
        String sql = "SELECT SUM(monto_total - monto_recibido) FROM ventas WHERE fecha BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
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

    // 3. PAGOS INICIALES: Calcula cuánta plata dejaron los clientes EN EL MOMENTO de la venta (Restando lo que pagaron después)
    public double obtenerPagosInicialesPorRango(String fechaInicio, String fechaFin) {
        String sql = "SELECT SUM(monto_recibido - COALESCE((SELECT SUM(monto_pagado) FROM pagos_cuenta_corriente WHERE venta_id = ventas.id), 0)) FROM ventas WHERE fecha BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
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

    // 4. COBROS DE CUENTAS CORRIENTES: Pagos de deudas en el rango de fechas
    public double obtenerPagosCuentasCorrientesPorRango(String fechaInicio, String fechaFin) {
        String sql = "SELECT SUM(monto_pagado) FROM pagos_cuenta_corriente WHERE fecha BETWEEN ? AND ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio);
            stmt.setString(2, fechaFin);
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
package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseConnection;
import com.alvear.multigraficaalvear.models.Pago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {

    private final Connection connection;

    public PagoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insertar(Pago pago) {
        String sql = "INSERT INTO pagos_cuenta_corriente (venta_id, monto_pagado, fecha) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pago.getVentaId());
            stmt.setDouble(2, pago.getMontoPagado());
            stmt.setString(3, pago.getFecha().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pago> obtenerPorVenta(int ventaId) {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM pagos_cuenta_corriente WHERE venta_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ventaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pago pago = new Pago();
                    pago.setId(rs.getInt("id"));
                    pago.setVentaId(rs.getInt("venta_id"));
                    pago.setMontoPagado(rs.getDouble("monto_pagado"));
                    pago.setFecha(LocalDate.parse(rs.getString("fecha")));
                    pagos.add(pago);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pagos;
    }
}

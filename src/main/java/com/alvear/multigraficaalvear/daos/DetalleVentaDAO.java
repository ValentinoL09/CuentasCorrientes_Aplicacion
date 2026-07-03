package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseConnection;
import com.alvear.multigraficaalvear.models.DetalleVenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetalleVentaDAO {

    private final Connection connection;

    public DetalleVentaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insertar(DetalleVenta detalle) {
        String sql = "INSERT INTO detalle_ventas (venta_id, servicio_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, detalle.getVentaId());
            stmt.setInt(2, detalle.getServicioId());
            stmt.setInt(3, detalle.getCantidad());
            stmt.setDouble(4, detalle.getPrecioUnitario());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<DetalleVenta> listarPorVenta(int ventaId) {
        List<DetalleVenta> detalles = new ArrayList<>();
        // ACÁ ESTÁ LA MAGIA: Hacemos un INNER JOIN para "robarle" el nombre a la tabla servicios
        String sql = "SELECT d.id, d.venta_id, d.servicio_id, d.cantidad, d.precio_unitario, s.nombre AS nombre_servicio " +
                     "FROM detalle_ventas d " +
                     "INNER JOIN servicios s ON d.servicio_id = s.id " +
                     "WHERE d.venta_id = ?";
                     
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, ventaId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setId(rs.getInt("id"));
                    detalle.setVentaId(rs.getInt("venta_id"));
                    detalle.setServicioId(rs.getInt("servicio_id"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    detalle.setNombreServicio(rs.getString("nombre_servicio")); // Lo guardamos
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detalles;
    }
}
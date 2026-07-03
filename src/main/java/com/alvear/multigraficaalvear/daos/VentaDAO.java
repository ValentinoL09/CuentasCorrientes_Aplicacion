package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseConnection;
import com.alvear.multigraficaalvear.models.Venta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    private final Connection connection;

    public VentaDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public int insertar(Venta venta) {
        // Quitamos tipo_venta_id y sumamos detalle
        String sql = "INSERT INTO ventas (cliente_id, descripcion, monto_total, monto_recibido, fecha, detalle) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, venta.getClienteId());
            stmt.setString(2, venta.getDescripcion());
            stmt.setDouble(3, venta.getMontoTotal());
            stmt.setDouble(4, venta.getMontoRecibido());
            stmt.setString(5, venta.getFecha().toString());
            stmt.setString(6, venta.getDetalle());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public List<Venta> obtenerTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas ORDER BY id DESC"; // Ordenamos para ver la más reciente primero
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setClienteId(rs.getInt("cliente_id"));
                venta.setDescripcion(rs.getString("descripcion"));
                venta.setMontoTotal(rs.getDouble("monto_total"));
                venta.setMontoRecibido(rs.getDouble("monto_recibido"));
                venta.setFecha(LocalDate.parse(rs.getString("fecha")));
                venta.setDetalle(rs.getString("detalle"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }

    public void actualizar(Venta venta) {
        String sql = "UPDATE ventas SET cliente_id = ?, descripcion = ?, monto_total = ?, monto_recibido = ?, fecha = ?, detalle = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venta.getClienteId());
            stmt.setString(2, venta.getDescripcion());
            stmt.setDouble(3, venta.getMontoTotal());
            stmt.setDouble(4, venta.getMontoRecibido());
            stmt.setString(5, venta.getFecha().toString());
            stmt.setString(6, venta.getDetalle());
            stmt.setInt(7, venta.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // (Podés dejar el método obtenerPorCliente igual, solo sacale el setTipoVentaId y agregale el setDetalle)
}
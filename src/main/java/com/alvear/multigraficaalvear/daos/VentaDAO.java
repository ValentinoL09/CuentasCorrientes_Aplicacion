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

    public void insertar(Venta venta) {
        String sql = "INSERT INTO ventas (cliente_id, tipo_venta_id, descripcion, monto_total, monto_recibido, fecha) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venta.getClienteId());
            stmt.setInt(2, venta.getTipoVentaId());
            stmt.setString(3, venta.getDescripcion());
            stmt.setDouble(4, venta.getMontoTotal());
            stmt.setDouble(5, venta.getMontoRecibido());
            stmt.setString(6, venta.getFecha().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Venta> obtenerTodas() {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));
                venta.setClienteId(rs.getInt("cliente_id"));
                venta.setTipoVentaId(rs.getInt("tipo_venta_id"));
                venta.setDescripcion(rs.getString("descripcion"));
                venta.setMontoTotal(rs.getDouble("monto_total"));
                venta.setMontoRecibido(rs.getDouble("monto_recibido"));
                venta.setFecha(LocalDate.parse(rs.getString("fecha")));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }

    public List<Venta> obtenerPorCliente(int clienteId) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM ventas WHERE cliente_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Venta venta = new Venta();
                    venta.setId(rs.getInt("id"));
                    venta.setClienteId(rs.getInt("cliente_id"));
                    venta.setTipoVentaId(rs.getInt("tipo_venta_id"));
                    venta.setDescripcion(rs.getString("descripcion"));
                    venta.setMontoTotal(rs.getDouble("monto_total"));
                    venta.setMontoRecibido(rs.getDouble("monto_recibido"));
                    venta.setFecha(LocalDate.parse(rs.getString("fecha")));
                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ventas;
    }
}

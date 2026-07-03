package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseConnection;
import com.alvear.multigraficaalvear.models.Servicio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    private final Connection connection;

    public ServicioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void insertar(Servicio servicio) {
        String sql = "INSERT INTO servicios (nombre, categoria, precio_sugerido, detalle) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, servicio.getNombre());
            stmt.setString(2, servicio.getCategoria());
            stmt.setDouble(3, servicio.getPrecioSugerido());
            stmt.setString(4, servicio.getDetalle());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizar(Servicio servicio) {
        String sql = "UPDATE servicios SET nombre = ?, categoria = ?, precio_sugerido = ?, detalle = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, servicio.getNombre());
            stmt.setString(2, servicio.getCategoria());
            stmt.setDouble(3, servicio.getPrecioSugerido());
            stmt.setString(4, servicio.getDetalle());
            stmt.setInt(5, servicio.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM servicios WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Servicio> listarTodos() {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT * FROM servicios";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setCategoria(rs.getString("categoria"));
                servicio.setPrecioSugerido(rs.getDouble("precio_sugerido"));
                servicio.setDetalle(rs.getString("detalle")); // Leemos el detalle
                servicios.add(servicio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicios;
    }
}

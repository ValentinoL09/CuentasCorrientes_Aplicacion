package com.alvear.multigraficaalvear.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initializeDatabase() {
        Connection connection = DatabaseConnection.getInstance().getConnection();

        try (Statement stmt = connection.createStatement()) {
            String createClientesTable = "CREATE TABLE IF NOT EXISTS clientes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre_cliente TEXT NOT NULL," +
                    "telefono TEXT," +
                    "cuit TEXT UNIQUE," +
                    "correo TEXT" +
                    ")";
            stmt.execute(createClientesTable);

            try {
                stmt.execute("ALTER TABLE clientes ADD COLUMN detalle TEXT");
            } catch (SQLException e) {
                // Si la columna ya existe, lo ignora silenciosamente.
            }

            String createTiposVentaTable = "CREATE TABLE IF NOT EXISTS tipos_venta (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT UNIQUE NOT NULL" +
                    ")";
            stmt.execute(createTiposVentaTable);

            String createVentasTable = "CREATE TABLE IF NOT EXISTS ventas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cliente_id INTEGER," +
                    "tipo_venta_id INTEGER," +
                    "descripcion TEXT," +
                    "monto_total REAL," +
                    "monto_recibido REAL," +
                    "fecha TEXT," +
                    "FOREIGN KEY (cliente_id) REFERENCES clientes(id)," +
                    "FOREIGN KEY (tipo_venta_id) REFERENCES tipos_venta(id)" +
                    ")";
            stmt.execute(createVentasTable);

            String createPagosTable = "CREATE TABLE IF NOT EXISTS pagos_cuenta_corriente (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "venta_id INTEGER," +
                    "monto_pagado REAL," +
                    "fecha TEXT," +
                    "FOREIGN KEY (venta_id) REFERENCES ventas(id)" +
                    ")";
            stmt.execute(createPagosTable);

            String createAuditoriaTable = "CREATE TABLE IF NOT EXISTS auditoria (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tabla_afectada TEXT," +
                    "registro_id INTEGER," +
                    "tipo_operacion TEXT," +
                    "datos_anteriores TEXT," +
                    "fecha_hora TEXT" +
                    ")";
            stmt.execute(createAuditoriaTable);

            // AGREGAMOS detalle TEXT a la creación original
            String createServiciosTable = "CREATE TABLE IF NOT EXISTS servicios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "categoria TEXT," +
                    "precio_sugerido REAL," +
                    "detalle TEXT" + 
                    ")";
            stmt.execute(createServiciosTable);

            // TÉCNICA DE MIGRACIÓN: Si la tabla ya existe de antes, le inyectamos la columna.
            // Si la columna ya existe, el catch ignora el error silenciosamente.
            try {
                stmt.execute("ALTER TABLE servicios ADD COLUMN detalle TEXT");
            } catch (SQLException e) {
                // La columna ya existe, no hacemos nada.
            }

            String createDetalleVentasTable = "CREATE TABLE IF NOT EXISTS detalle_ventas (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "venta_id INTEGER NOT NULL," +
                    "servicio_id INTEGER NOT NULL," +
                    "cantidad INTEGER NOT NULL," +
                    "precio_unitario REAL NOT NULL," +
                    "FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (servicio_id) REFERENCES servicios(id)" +
                    ")";
            stmt.execute(createDetalleVentasTable);

            try {
                stmt.execute("ALTER TABLE ventas ADD COLUMN detalle TEXT");
            } catch (SQLException e) {
                // Si la columna ya existe, lo ignora.
            }

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
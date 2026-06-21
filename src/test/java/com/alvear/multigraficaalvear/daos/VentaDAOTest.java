package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseInitializer;
import com.alvear.multigraficaalvear.models.Cliente;
import com.alvear.multigraficaalvear.models.TipoVenta;
import com.alvear.multigraficaalvear.models.Venta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class VentaDAOTest {

    private static ClienteDAO clienteDAO;
    private static TipoVentaDAO tipoVentaDAO;
    private static VentaDAO ventaDAO;

    @BeforeAll
    public static void setup() {
        DatabaseInitializer.initializeDatabase();
        clienteDAO = new ClienteDAO();
        tipoVentaDAO = new TipoVentaDAO();
        ventaDAO = new VentaDAO();
    }

    @Test
    public void testInsertarYObtenerVenta() {
        Cliente cliente = new Cliente();
        cliente.setNombreCliente("Cliente Venta Test");
        cliente.setTelefono("1234567890");
        cliente.setCuit("30-11111111-1");
        cliente.setCorreo("venta@test.com");
        clienteDAO.insertar(cliente);
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        int clienteId = clientes.get(clientes.size() - 1).getId();

        TipoVenta tipoVenta = new TipoVenta();
        tipoVenta.setNombre("Tipo Venta Test");
        tipoVentaDAO.insertar(tipoVenta);
        List<TipoVenta> tipos = tipoVentaDAO.obtenerTodos();
        int tipoVentaId = tipos.get(tipos.size() - 1).getId();

        LocalDate fechaHoy = LocalDate.now();
        Venta venta = new Venta();
        venta.setClienteId(clienteId);
        venta.setTipoVentaId(tipoVentaId);
        venta.setDescripcion("Descripcion de prueba");
        venta.setMontoTotal(1500.50);
        venta.setMontoRecibido(500.00);
        venta.setFecha(fechaHoy);

        ventaDAO.insertar(venta);

        List<Venta> ventasDelCliente = ventaDAO.obtenerPorCliente(clienteId);

        Assertions.assertFalse(ventasDelCliente.isEmpty(), "La lista de ventas del cliente no debe estar vacia");
        Venta ventaRecuperada = ventasDelCliente.get(ventasDelCliente.size() - 1);
        Assertions.assertEquals(fechaHoy, ventaRecuperada.getFecha(), "La fecha recuperada debe ser igual a la fecha insertada");
    }
}

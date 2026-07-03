package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseInitializer;
import com.alvear.multigraficaalvear.models.Cliente;
import com.alvear.multigraficaalvear.models.Venta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class VentaDAOTest {

    private static ClienteDAO clienteDAO;
    private static VentaDAO ventaDAO;

    @BeforeAll
    public static void setup() {
        DatabaseInitializer.initializeDatabase();
        clienteDAO = new ClienteDAO();
        ventaDAO = new VentaDAO();
    }

    @Test
    public void testInsertarYObtenerVenta() {
        // 1. Creamos e insertamos el cliente para la prueba
        Cliente cliente = new Cliente();
        cliente.setNombreCliente("Cliente Venta Test");
        cliente.setTelefono("1234567890");
        cliente.setCuit("30-11111111-1");
        cliente.setCorreo("venta@test.com");
        clienteDAO.insertar(cliente);
        
        List<Cliente> clientes = clienteDAO.obtenerTodos();
        int clienteId = clientes.get(clientes.size() - 1).getId();

        LocalDate fechaHoy = LocalDate.now();
        
        // 2. Armamos la nueva estructura de la Venta (Sin TipoVenta y con Detalle)
        Venta venta = new Venta();
        venta.setClienteId(clienteId);
        venta.setDescripcion("Descripcion de prueba");
        venta.setMontoTotal(1500.50);
        venta.setMontoRecibido(500.00);
        venta.setFecha(fechaHoy);
        venta.setDetalle("Nota de prueba para el test"); // <--- Probamos el nuevo campo

        // 3. Insertamos en la Base de Datos
        ventaDAO.insertar(venta);

        // 4. Recuperamos para verificar que se guardó correctamente
        List<Venta> ventasDelCliente = ventaDAO.obtenerPorCliente(clienteId);

        // 5. Aserciones (Verificaciones de JUnit)
        Assertions.assertFalse(ventasDelCliente.isEmpty(), "La lista de ventas del cliente no debe estar vacia");
        Venta ventaRecuperada = ventasDelCliente.get(ventasDelCliente.size() - 1);
        
        Assertions.assertEquals(fechaHoy, ventaRecuperada.getFecha(), "La fecha recuperada debe ser igual a la fecha insertada");
        Assertions.assertEquals("Nota de prueba para el test", ventaRecuperada.getDetalle(), "El detalle recuperado debe coincidir");
    }
}
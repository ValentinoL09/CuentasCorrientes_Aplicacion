package com.alvear.multigraficaalvear.daos;

import com.alvear.multigraficaalvear.config.DatabaseInitializer;
import com.alvear.multigraficaalvear.models.Cliente;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ClienteDAOTest {

    private static ClienteDAO clienteDAO;

    @BeforeAll
    public static void setup() {
        DatabaseInitializer.initializeDatabase();
        clienteDAO = new ClienteDAO();
    }

    @Test
    public void testInsertarYObtenerCliente() {
        Cliente cliente = new Cliente();
        cliente.setNombreCliente("Cliente de Prueba");
        cliente.setTelefono("1234567890");
        cliente.setCuit("30-12345678-9");
        cliente.setCorreo("test@gmail.com");

        clienteDAO.insertar(cliente);

        List<Cliente> clientes = clienteDAO.obtenerTodos();

        Assertions.assertFalse(clientes.isEmpty(), "La tabla de clientes no debe estar vacia despues de insertar");

        boolean encontrado = false;
        for (Cliente c : clientes) {
            if ("Cliente de Prueba".equals(c.getNombreCliente()) &&
                "test@gmail.com".equals(c.getCorreo())) {
                encontrado = true;
                break;
            }
        }
        Assertions.assertTrue(encontrado, "El cliente insertado debe estar en la lista obtenida");

        for (Cliente c : clientes) {
            if ("Cliente de Prueba".equals(c.getNombreCliente())) {
                clienteDAO.eliminar(c.getId());
                break;
            }
        }
    }
}

package dao;

import entity.Cliente;
import java.util.List;

public interface ClienteDAO {
    boolean insertar(Cliente cliente);

    Cliente obtenerPorId(int id);
    List<Cliente> listarTodos();

    boolean modificar(Cliente cliente);

    boolean eliminar(int id);
}
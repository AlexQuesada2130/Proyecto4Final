package dao;

import entity.Vehiculo;
import java.util.List;

public interface VehiculoDAO {
    boolean insertar(Vehiculo vehiculo);

    Vehiculo obtenerPorId(int id);
    List<Vehiculo> listarTodos();

    List<Vehiculo> listarPorCategoria(int idCategoria);

    boolean modificar(Vehiculo vehiculo);

    boolean eliminar(int id);
}
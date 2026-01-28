package dao;

import entity.CategoriaVehiculo;
import java.util.List;

public interface CategoriaVehiculoDAO {
    boolean insertar(CategoriaVehiculo categoria);

    CategoriaVehiculo obtenerPorId(int id);

    List<CategoriaVehiculo> listarTodos();

    boolean modificar(CategoriaVehiculo categoria);

    boolean eliminar(int id);
}
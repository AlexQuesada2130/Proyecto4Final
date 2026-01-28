package dao;

import entity.Alquiler;
import java.util.List;

public interface AlquilerDAO {
    boolean insertar(Alquiler alquiler);

    List<Alquiler> listarTodos();

    boolean eliminar(int id);

    boolean vehiculoEstaAlquilado(int idVehiculo);
}
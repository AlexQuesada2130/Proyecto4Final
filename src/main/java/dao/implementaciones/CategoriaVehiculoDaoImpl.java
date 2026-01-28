package dao.implementaciones;

import conBD.ConexionBD;
import dao.CategoriaVehiculoDAO;
import entity.CategoriaVehiculo;
import javax.persistence.EntityManager;

import java.util.List;

public class CategoriaVehiculoDaoImpl implements CategoriaVehiculoDAO {

    @Override
    public List<CategoriaVehiculo> listarTodos() {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM CategoriaVehiculo c", CategoriaVehiculo.class).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public CategoriaVehiculo obtenerPorId(int idCategoria) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            return em.find(CategoriaVehiculo.class, idCategoria);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean insertar(CategoriaVehiculo categoria) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al insertar categoría: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean modificar(CategoriaVehiculo categoria) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(categoria);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al modificar categoría: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminar(int idCategoria) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            // Opcional: Verificar si existen vehículos en esta categoría para mantener integridad
            Long count = em.createQuery("SELECT COUNT(v) FROM Vehiculo v WHERE v.categoria.id = :id", Long.class)
                    .setParameter("id", idCategoria)
                    .getSingleResult();

            if (count > 0) {
                System.out.println("ERROR: Hay vehículos asociados a esta categoría.");
                return false;
            }

            em.getTransaction().begin();
            CategoriaVehiculo c = em.find(CategoriaVehiculo.class, idCategoria);
            if (c != null) {
                em.remove(c);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al eliminar categoría: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
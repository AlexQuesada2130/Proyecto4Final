package dao.implementaciones;

import conBD.ConexionBD;
import dao.VehiculoDAO;
import entity.Vehiculo;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

public class VehiculoDaoImpl implements VehiculoDAO {

    @Override
    public boolean insertar(Vehiculo vehiculo) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            // Asegurarse de que la categoría asociada esté gestionada si es nueva,
            // pero normalmente se selecciona una existente.
            em.persist(vehiculo);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al insertar vehículo: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Vehiculo obtenerPorId(int id) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            return em.find(Vehiculo.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Vehiculo> listarTodos() {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            // Hacemos JOIN FETCH con categoria para que al mostrar el toString no falle (Lazy Loading)
            String jpql = "SELECT v FROM Vehiculo v LEFT JOIN FETCH v.categoria";
            TypedQuery<Vehiculo> query = em.createQuery(jpql, Vehiculo.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Vehiculo> listarPorCategoria(int idCategoria) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            // Filtramos navegando por el objeto: v.categoria.id
            String jpql = "SELECT v FROM Vehiculo v LEFT JOIN FETCH v.categoria WHERE v.categoria.id = :catId";
            TypedQuery<Vehiculo> query = em.createQuery(jpql, Vehiculo.class)
                    .setParameter("catId", idCategoria);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean modificar(Vehiculo vehiculo) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(vehiculo);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al modificar vehículo: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminar(int id) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            // Integridad: Verificar alquileres
            Long count = em.createQuery("SELECT COUNT(a) FROM Alquiler a WHERE a.idVehiculo.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (count > 0) {
                System.out.println("ERROR: Vehículo tiene alquileres activos.");
                return false;
            }

            em.getTransaction().begin();
            Vehiculo v = em.find(Vehiculo.class, id);
            if (v != null) {
                em.remove(v);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al eliminar vehículo: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
package dao.implementaciones;

import conBD.ConexionBD;
import dao.AlquilerDAO;
import entity.Alquiler;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

public class AlquilerDaoImpl implements AlquilerDAO {
    public boolean insertar(Alquiler alquiler) {
        // Validación manual antes de insertar
        if (vehiculoEstaAlquilado(alquiler.getIdVehiculo().getId())) {
            System.out.println("Error: El vehículo ya está alquilado.");
            return false;
        }

        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(alquiler);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error al insertar alquiler: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
    public List<Alquiler> listarTodos() {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            // Consulta JPQL para traer datos relacionados (JOIN FETCH)
            String jpql = "SELECT a FROM Alquiler a JOIN FETCH a.idVehiculo v JOIN FETCH a.idCliente c ORDER BY v.marca";
            TypedQuery<Alquiler> query = em.createQuery(jpql, Alquiler.class);
            return query.getResultList();
        } catch (Exception e) {
            System.out.println("Error al listar alquileres: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    public boolean eliminar(int id) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            Alquiler a = em.find(Alquiler.class, id);
            if (a != null) {
                em.remove(a);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("Error al eliminar alquiler: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    // Méto do auxiliar para lógica de negocio
    public boolean vehiculoEstaAlquilado(int idVehiculo) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            String jpql = "SELECT COUNT(a) FROM Alquiler a WHERE a.idVehiculo.id = :idV";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("idV", idVehiculo)
                    .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }
}


package dao.implementaciones;

import conBD.ConexionBD;
import dao.ClienteDAO;
import entity.Cliente;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;

public class ClienteDaoImpl implements ClienteDAO {

    @Override
    public boolean insertar(Cliente cliente) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al insertar cliente: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public Cliente obtenerPorId(int id) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Cliente> listarTodos() {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public boolean modificar(Cliente cliente) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(cliente); // Actualiza el objeto en la BD
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al modificar cliente: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean eliminar(int id) {
        EntityManager em = ConexionBD.getEntityManager();
        try {
            // Verificación de integridad: ¿Tiene alquileres?
            Long count = em.createQuery("SELECT COUNT(a) FROM Alquiler a WHERE a.idCliente.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();

            if (count > 0) {
                System.out.println("ERROR: No se puede eliminar el cliente. Tiene alquileres asociados.");
                return false;
            }

            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
                em.getTransaction().commit();
                return true;
            }
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.out.println("Error al eliminar cliente: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }
}
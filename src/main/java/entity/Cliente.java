package entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellidos;

    @OneToMany(mappedBy = "idCliente", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Alquiler> alquileres;

    public Cliente() {}

    public Cliente(String nombre, String apellidos) {
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public List<Alquiler> getAlquileres() { return alquileres; }
    public void setAlquileres(List<Alquiler> alquileres) { this.alquileres = alquileres; }

    @Override
    public String toString() {
        return "ID: " + id + " | " + nombre + " " + apellidos;
    }
}
package entity;

import javax.persistence.*;


@Entity
@Table(name = "vehiculo")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String marca;
    private String modelo;
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoriaVehiculo categoria;

    public Vehiculo() {}

    public Vehiculo(String marca, String modelo, String matricula, CategoriaVehiculo categoria) {
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getMarca() {
        return marca;
    }
    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public CategoriaVehiculo getCategoria() {
        return categoria;
    }
    public void setCategoria(CategoriaVehiculo categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return id + " - " + marca + " " + modelo + " (" + matricula + ")";
    }
}
package entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "alquiler")
public class Alquiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    @ManyToOne(fetch = FetchType.LAZY)

    private Vehiculo idVehiculo;
    @ManyToOne(fetch = FetchType.LAZY)

    private Cliente idCliente;
    private Timestamp fechaContrato;

    public Alquiler() {}

    public Alquiler(Vehiculo v, Cliente c, Timestamp fecha) {
        this.idVehiculo = v;
        this.idCliente = c;
        this.fechaContrato = fecha;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Vehiculo getIdVehiculo() {
        return idVehiculo;
    }
    public void setIdVehiculo(Vehiculo idVehiculo) {
        this.idVehiculo = idVehiculo;
    }
    public Cliente getIdCliente() {
        return idCliente;
    }
    public void setIdCliente(Cliente idCliente) {
        this.idCliente = idCliente;
    }
    public Timestamp getFechaContrato() {
        return fechaContrato;
    }
    public void setFechaContrato(Timestamp fechaContrato) {
        this.fechaContrato = fechaContrato;
    }

    @Override
    public String toString() {
        return "Alquiler{" +
                "id=" + id +
                ", idVehiculo=" + idVehiculo +
                ", idCliente=" + idCliente +
                ", fechaContrato=" + fechaContrato +
                '}';
    }
}
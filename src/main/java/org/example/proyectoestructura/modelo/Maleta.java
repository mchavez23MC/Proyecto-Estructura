package org.example.proyectoestructura.modelo;

public class Maleta {
    private String id;
    private Double peso;
    private String idPasajero;

    public Maleta(String id, Double peso,  String idPasajero) {
        this.id = id;
        this.peso = peso;
        this.idPasajero = idPasajero;
    }

    public String getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(String idPasajero) {
        this.idPasajero = idPasajero;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Maleta{" +
                "id='" + id + '\'' +
                ", peso=" + peso +
                ", idPasajero='" + idPasajero + '\'' +
                '}';
    }
}

package org.example.proyectoestructura.modelo;

public class Pasajero {
    private String id;
    private String nombre;
    private String vuelo;
    private int cantidadMaletas;

    public Pasajero(String id, String nombre, String vuelo, int cantidadMaletas) {
        this.id = id;
        this.nombre = nombre;
        this.vuelo = vuelo;
        this.cantidadMaletas = cantidadMaletas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getVuelo() {
        return vuelo;
    }

    public void setVuelo(String vuelo) {
        this.vuelo = vuelo;
    }

    public int getCantidadMaletas() {
        return cantidadMaletas;
    }

    public void setCantidadMaletas(int cantidadMaletas) {
        this.cantidadMaletas = cantidadMaletas;
    }

    @Override
    public String toString() {
        return id;
    }
}

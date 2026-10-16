package org.example.proyectoestructura;

import org.example.proyectoestructura.estructura.ListaArreglo;

import org.example.proyectoestructura.modelo.Pasajero;
import org.example.proyectoestructura.modelo.Maleta;

import java.util.Comparator;
import java.util.Random;
import org.example.proyectoestructura.estructura.ListaEnlazada;
import org.example.proyectoestructura.simulacion.SimulacionAeropuerto;

public class Main {
    public static void main(String[] args) {
        Random aleatorio = args.length == 0 ? new Random() : new Random(Long.parseLong(args[0]));
        ListaArreglo<Pasajero> pasajeros = new ListaArreglo<>(10);
        pasajeros.addLast(new Pasajero("P001", "Ana", "Torres", "EC123456"));
        pasajeros.addLast(new Pasajero("P002", "Carlos", "Mendoza", "EC654321"));
        pasajeros.addLast(new Pasajero("P003", "Lucia", "Vega", "EC987654"));

        ListaArreglo<Maleta> maletas = new ListaArreglo<>(10);
        maletas.addLast(new Maleta("M001", 18.5, "P001"));
        maletas.addLast(new Maleta("M002", 7.2, "P001"));
        maletas.addLast(new Maleta("M003", 22.0, "P002"));
        maletas.addLast(new Maleta("M004", 12.8, "P003"));
        maletas.addLast(new Maleta("M011", 9.0, "P002"));

        Comparator<Maleta> porCodigo = Comparator.comparingInt(maleta -> extraerNumero(maleta.getId()));
        System.out.println("Maletas con código mayor a 10:");
        System.out.println(maletas.buscarMayores(new Maleta("M010", 0.0, ""), porCodigo));

        Comparator<Pasajero> porCantidadDeMaletas = Comparator.comparingInt(
                pasajero -> contarMaletas(maletas, pasajero.getId()));
        // P003 tiene una maleta y sirve como referencia para filtrar pasajeros con más de una.
        System.out.println("Pasajeros con más de una maleta:");
        System.out.println(pasajeros.buscarMayores(new Pasajero("P003", "", "", ""), porCantidadDeMaletas));

        // La cantidad de maletas se sigue calculando desde la relación idPasajero.
        ListaEnlazada<Pasajero> pendientes = new ListaEnlazada<>();
        for (int i = 0; i < pasajeros.size(); i++) {
            pendientes.agregarUltimo(pasajeros.get(i));
        }
        for (int i = 4; i <= 12; i++) {
            pendientes.agregarUltimo(new Pasajero("P%03d".formatted(i),
                    "Pasajero " + i, "Ejemplo", "EC%06d".formatted(i)));
        }
        new SimulacionAeropuerto(pendientes, aleatorio, System.out).ejecutar();
    }

    private static int extraerNumero(String codigo) {
        String numero = codigo.replaceAll("\\D", "");
        if (numero.isEmpty()) {
            throw new IllegalArgumentException("El código de maleta debe contener un número: " + codigo);
        }
        return Integer.parseInt(numero);
    }

    private static int contarMaletas(ListaArreglo<Maleta> maletas, String idPasajero) {
        int cantidad = 0;
        for (int i = 0; i < maletas.size(); i++) {
            if (maletas.get(i).getIdPasajero().equals(idPasajero)) {
                cantidad++;
            }
        }
        return cantidad;
    }
}

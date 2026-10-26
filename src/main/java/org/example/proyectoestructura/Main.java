package org.example.proyectoestructura;

import java.util.Random;
import java.util.Scanner;
import org.example.proyectoestructura.estructura.ListaEnlazada;
import org.example.proyectoestructura.modelo.Maleta;
import org.example.proyectoestructura.modelo.Pasajero;
import org.example.proyectoestructura.simulacion.SimulacionAeropuerto;

public class Main {
    public static void main(String[] args) {
        Random aleatorio;
        if (args.length == 0) {
            aleatorio = new Random();
        } else {
            aleatorio = new Random(Long.parseLong(args[0]));
        }
        ListaEnlazada<Pasajero> LPasajeros = crearPasajeros();
        ListaEnlazada<Maleta> LMaletas = SimulacionAeropuerto.crearMaletas(LPasajeros, aleatorio);
        System.out.println("LMaletas: " + LMaletas);
        SimulacionAeropuerto simulacion = new SimulacionAeropuerto(LPasajeros, LMaletas, aleatorio, System.out);

        Scanner teclado = new Scanner(System.in);
        int opcion = -1;
        while (opcion != 0) {
            System.out.println();
            System.out.println("===== Airport Flow Manager =====");
            System.out.println("1. Ejecutar un ciclo de check-in");
            System.out.println("2. Ejecutar todo el check-in");
            System.out.println("3. Cerrar un mostrador");
            System.out.println("4. Procesar una maleta");
            System.out.println("5. Procesar todas las maletas");
            System.out.println("6. Ver estado de LP y LM");
            System.out.println("7. Ejecutar la simulación completa");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            if (!teclado.hasNextLine()) {
                break;
            }
            opcion = leerNumero(teclado.nextLine());
            if (opcion == 1) {
                if (simulacion.haTerminado()) {
                    System.out.println("El check-in ya terminó.");
                } else {
                    simulacion.ejecutarRonda();
                }
            } else if (opcion == 2) {
                simulacion.ejecutarCheckIn();
                simulacion.imprimirEstado();
            } else if (opcion == 3) {
                System.out.println(simulacion.getMostradores());
                System.out.print("Número de mostrador a cerrar: ");
                if (teclado.hasNextLine()) {
                    simulacion.cerrarMostrador("Mostrador " + leerNumero(teclado.nextLine()));
                    simulacion.imprimirEstado();
                }
            } else if (opcion == 4) {
                if (!simulacion.haTerminado()) {
                    System.out.println("Primero deben atenderse todos los pasajeros.");
                } else {
                    simulacion.procesarMaleta();
                    simulacion.imprimirEstado();
                }
            } else if (opcion == 5) {
                if (!simulacion.haTerminado()) {
                    System.out.println("Primero deben atenderse todos los pasajeros.");
                } else {
                    simulacion.procesarTodasLasMaletas();
                }
            } else if (opcion == 6) {
                simulacion.imprimirEstado();
            } else if (opcion == 7) {
                simulacion.ejecutar();
            } else if (opcion != 0) {
                System.out.println("Opción no válida.");
            }
        }
        System.out.println("Fin del programa.");
    }

    public static ListaEnlazada<Pasajero> crearPasajeros() {
        ListaEnlazada<Pasajero> LPasajeros = new ListaEnlazada<>();
        LPasajeros.agregarUltimo(new Pasajero("P001", "Juan", "GYE-UIO", 2));
        LPasajeros.agregarUltimo(new Pasajero("P002", "Pepe", "GYE-LIM", 1));
        LPasajeros.agregarUltimo(new Pasajero("P003", "Patricio", "GYE-MIA", 0));
        LPasajeros.agregarUltimo(new Pasajero("P004", "María", "GYE-UIO", 4));
        LPasajeros.agregarUltimo(new Pasajero("P005", "Rocío", "GYE-LIM", 2));
        LPasajeros.agregarUltimo(new Pasajero("P006", "Carlos", "GYE-MIA", 1));
        LPasajeros.agregarUltimo(new Pasajero("P007", "Mariana", "GYE-UIO", 3));
        LPasajeros.agregarUltimo(new Pasajero("P008", "Pedro", "GYE-LIM", 1));
        LPasajeros.agregarUltimo(new Pasajero("P009", "Lucía", "GYE-MIA", 2));
        LPasajeros.agregarUltimo(new Pasajero("P010", "Andrés", "GYE-UIO", 1));
        LPasajeros.agregarUltimo(new Pasajero("P011", "Sofía", "GYE-LIM", 0));
        LPasajeros.agregarUltimo(new Pasajero("P012", "Diego", "GYE-MIA", 2));
        return LPasajeros;
    }

    private static int leerNumero(String texto) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

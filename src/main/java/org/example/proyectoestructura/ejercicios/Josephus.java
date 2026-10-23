package org.example.proyectoestructura.ejercicios;

import java.io.PrintStream;
import org.example.proyectoestructura.estructura.Cola;

public class Josephus {
    public static <E> E josephus(Cola<E> cola, int k, PrintStream salida) {
        if (cola.estaVacio()) {
            return null;
        }
        if (k < 1) {
            throw new IllegalArgumentException("k debe ser mayor a 0: " + k);
        }
        while (cola.getSize() > 1) {
            for (int i = 1; i < k; i++) {
                cola.encolar(cola.desencolar());
            }
            E eliminado = cola.desencolar();
            salida.println("Sale " + eliminado + ", quedan " + cola);
        }
        return cola.desencolar();
    }
}

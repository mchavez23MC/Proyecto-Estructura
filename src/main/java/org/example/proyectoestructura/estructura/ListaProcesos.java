package org.example.proyectoestructura.estructura;

import java.util.StringJoiner;

public class ListaProcesos<E, F> {
    private NodoProceso<E, F> primero;
    private NodoProceso<E, F> ultimo;

    public NodoProceso<E, F> getPrimero() { return primero; }
    public boolean estaVacio() { return primero == null; }

    public void agregarUltimo(E valor) {
        NodoProceso<E, F> nuevo = new NodoProceso<>(valor);
        if (ultimo == null) {
            primero = nuevo;
        } else {
            ultimo.setSig(nuevo);
        }
        ultimo = nuevo;
    }

    public int getSize() {
        int cantidad = 0;
        for (NodoProceso<E, F> nodo = primero; nodo != null; nodo = nodo.getSig()) {
            cantidad++;
        }
        return cantidad;
    }

    public boolean estanVaciasLA() {
        for (NodoProceso<E, F> nodo = primero; nodo != null; nodo = nodo.getSig()) {
            if (!nodo.getLista().estaVacio()) {
                return false;
            }
        }
        return true;
    }

    // Acceso por posición desde cero, no por el valor identificador del proceso.
    public NodoProceso<E, F> getNodoXId(int pos) {
        if (pos < 0) {
            throw new IndexOutOfBoundsException("Posición negativa: " + pos);
        }
        NodoProceso<E, F> nodo = primero;
        for (int i = 0; i < pos && nodo != null; i++) {
            nodo = nodo.getSig();
        }
        if (nodo == null) {
            throw new IndexOutOfBoundsException("Posición fuera de rango: " + pos);
        }
        return nodo;
    }

    @Override
    public String toString() {
        StringJoiner procesos = new StringJoiner(System.lineSeparator());
        for (NodoProceso<E, F> nodo = primero; nodo != null; nodo = nodo.getSig()) {
            procesos.add(nodo.getValor() + ": " + nodo.getLista());
        }
        return procesos.toString();
    }
}

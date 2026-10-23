package org.example.proyectoestructura.estructura;

import java.util.StringJoiner;

public class ListaProcesos<E, F> {
    private NodoProceso<E, F> primero;
    private NodoProceso<E, F> ultimo;

    public NodoProceso<E, F> getPrimero() { return primero; }
    public NodoProceso<E, F> getUltimo() { return ultimo; }
    public boolean estaVacio() { return primero == null; }

    public void agregarUltimo(E valor) {
        NodoProceso<E, F> nuevo = new NodoProceso<>(valor);
        if (ultimo == null) {
            primero = nuevo;
        } else {
            ultimo.setSig(nuevo);
            nuevo.setSig(primero);
        }
        ultimo = nuevo;
    }

    public int getSize() {
        if (estaVacio()) {
            return 0;
        }
        int cantidad = 0;
        NodoProceso<E, F> nodo = primero;
        do {
            cantidad++;
            nodo = nodo.getSig();
        } while (nodo != primero);
        return cantidad;
    }

    public boolean estanVaciasLA() {
        if (estaVacio()) {
            return true;
        }
        NodoProceso<E, F> nodo = primero;
        do {
            if (!nodo.getLista().estaVacio()) {
                return false;
            }
            nodo = nodo.getSig();
        } while (nodo != primero);
        return true;
    }

    public NodoProceso<E, F> buscar(E valor) {
        if (estaVacio()) {
            return null;
        }
        NodoProceso<E, F> nodo = primero;
        do {
            if (nodo.getValor().equals(valor)) {
                return nodo;
            }
            nodo = nodo.getSig();
        } while (nodo != primero);
        return null;
    }

    public NodoProceso<E, F> getNodoXId(int pos) {
        if (pos < 0 || pos >= getSize()) {
            throw new IndexOutOfBoundsException("Posición fuera de rango: " + pos);
        }
        NodoProceso<E, F> nodo = primero;
        for (int i = 0; i < pos; i++) {
            nodo = nodo.getSig();
        }
        return nodo;
    }

    @Override
    public String toString() {
        StringJoiner procesos = new StringJoiner(System.lineSeparator());
        if (estaVacio()) {
            return procesos.toString();
        }
        NodoProceso<E, F> nodo = primero;
        do {
            procesos.add(nodo.getValor() + ": " + nodo.getLista());
            nodo = nodo.getSig();
        } while (nodo != primero);
        return procesos.toString();
    }
}

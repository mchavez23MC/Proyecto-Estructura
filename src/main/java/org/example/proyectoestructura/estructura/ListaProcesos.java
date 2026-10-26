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

    public boolean agregarProceso(E valor) {
        if (buscar(valor) != null) {
            return false;
        }
        agregarUltimo(valor);
        return true;
    }

    public boolean removerProceso(E valor) {
        NodoProceso<E, F> nodo = buscar(valor);
        if (nodo == null || !nodo.getLista().estaVacio()) {
            return false;
        }
        if (primero == ultimo) {
            primero = null;
            ultimo = null;
            return true;
        }
        NodoProceso<E, F> anterior = ultimo;
        while (anterior.getSig() != nodo) {
            anterior = anterior.getSig();
        }
        anterior.setSig(nodo.getSig());
        if (nodo == primero) {
            primero = nodo.getSig();
        }
        if (nodo == ultimo) {
            ultimo = anterior;
        }
        nodo.setSig(nodo);
        return true;
    }

    public NodoProceso<E, F> encontrarProceso(E valor) {
        return buscar(valor);
    }

    public boolean agregar(E valor, F elemento) {
        NodoProceso<E, F> nodo = buscar(valor);
        if (nodo == null) {
            return false;
        }
        nodo.getLista().agregarUltimo(elemento);
        return true;
    }

    public F eliminar(E valor) {
        NodoProceso<E, F> nodo = buscar(valor);
        if (nodo == null) {
            return null;
        }
        NodoLista<F> retirado = nodo.getLista().eliminarPrimero();
        if (retirado == null) {
            return null;
        }
        return retirado.getValor();
    }

    public F revisarPrimero(E valor) {
        NodoProceso<E, F> nodo = buscar(valor);
        if (nodo == null || nodo.getLista().estaVacio()) {
            return null;
        }
        return nodo.getLista().getPrimero().getValor();
    }

    public int tamanoProceso(E valor) {
        NodoProceso<E, F> nodo = buscar(valor);
        if (nodo == null) {
            return -1;
        }
        return nodo.getLista().getSize();
    }

    public E obtenerProcesoMasCorto() {
        return obtenerProcesoMasCorto(null);
    }

    public E obtenerProcesoMasCorto(E excluido) {
        if (estaVacio()) {
            return null;
        }
        NodoProceso<E, F> menor = null;
        NodoProceso<E, F> nodo = primero;
        do {
            if (!nodo.getValor().equals(excluido)) {
                if (menor == null || nodo.getLista().getSize() < menor.getLista().getSize()) {
                    menor = nodo;
                }
            }
            nodo = nodo.getSig();
        } while (nodo != primero);
        if (menor == null) {
            return null;
        }
        return menor.getValor();
    }

    public int totalDeProcesos() {
        return getSize();
    }

    public int totalElementos() {
        if (estaVacio()) {
            return 0;
        }
        int total = 0;
        NodoProceso<E, F> nodo = primero;
        do {
            total = total + nodo.getLista().getSize();
            nodo = nodo.getSig();
        } while (nodo != primero);
        return total;
    }

    public E getValorXId(int pos) {
        return getNodoXId(pos).getValor();
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

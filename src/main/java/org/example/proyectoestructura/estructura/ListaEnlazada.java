package org.example.proyectoestructura.estructura;

import java.util.StringJoiner;

public class ListaEnlazada<E> {
    private NodoLista<E> primero;
    private NodoLista<E> ultimo;

    public NodoLista<E> getPrimero() { return primero; }
    public NodoLista<E> getUltimo() { return ultimo; }
    public boolean estaVacio() { return primero == null; }

    public int getSize() {
        int cantidad = 0;
        for (NodoLista<E> nodo = primero; nodo != null; nodo = nodo.getSig()) {
            cantidad++;
        }
        return cantidad;
    }

    public void agregarPrimero(E valor) {
        NodoLista<E> nuevo = new NodoLista<>(valor);
        if (primero == null) {
            ultimo = nuevo;
        } else {
            nuevo.setSig(primero);
            primero.setAnt(nuevo);
        }
        primero = nuevo;
    }

    public void agregarUltimo(E valor) {
        NodoLista<E> nuevo = new NodoLista<>(valor);
        if (ultimo == null) {
            primero = nuevo;
        } else {
            ultimo.setSig(nuevo);
            nuevo.setAnt(ultimo);
        }
        ultimo = nuevo;
    }

    public NodoLista<E> eliminarPrimero() {
        NodoLista<E> retirado = primero;
        if (retirado != null) {
            primero = retirado.getSig();
            retirado.setSig(null);
            if (primero == null) {
                ultimo = null;
            } else {
                primero.setAnt(null);
            }
        }
        return retirado;
    }

    public NodoLista<E> eliminarUltimo() {
        NodoLista<E> retirado = ultimo;
        if (retirado != null) {
            ultimo = retirado.getAnt();
            retirado.setAnt(null);
            if (ultimo == null) {
                primero = null;
            } else {
                ultimo.setSig(null);
            }
        }
        return retirado;
    }

    public NodoLista<E> eliminarNodo(NodoLista<E> nodo) {
        if (nodo == null || estaVacio()) {
            return null;
        }
        if (nodo == primero) {
            return eliminarPrimero();
        }
        if (nodo == ultimo) {
            return eliminarUltimo();
        }
        nodo.getAnt().setSig(nodo.getSig());
        nodo.getSig().setAnt(nodo.getAnt());
        nodo.setSig(null);
        nodo.setAnt(null);
        return nodo;
    }

    public String toStringInverso() {
        StringJoiner valores = new StringJoiner(", ", "[", "]");
        for (NodoLista<E> nodo = ultimo; nodo != null; nodo = nodo.getAnt()) {
            valores.add(String.valueOf(nodo.getValor()));
        }
        return valores.toString();
    }

    @Override
    public String toString() {
        StringJoiner valores = new StringJoiner(", ", "[", "]");
        for (NodoLista<E> nodo = primero; nodo != null; nodo = nodo.getSig()) {
            valores.add(String.valueOf(nodo.getValor()));
        }
        return valores.toString();
    }
}

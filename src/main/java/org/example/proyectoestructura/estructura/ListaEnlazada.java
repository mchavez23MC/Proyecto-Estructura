package org.example.proyectoestructura.estructura;

import java.util.StringJoiner;

public class ListaEnlazada<E> {
    private NodoLista<E> primero;
    private NodoLista<E> ultimo;

    public NodoLista<E> getPrimero() { return primero; }
    public boolean estaVacio() { return primero == null; }

    public int getSize() {
        int cantidad = 0;
        for (NodoLista<E> nodo = primero; nodo != null; nodo = nodo.getSig()) {
            cantidad++;
        }
        return cantidad;
    }

    public void agregarUltimo(E valor) {
        NodoLista<E> nuevo = new NodoLista<>(valor);
        if (ultimo == null) {
            primero = nuevo;
        } else {
            ultimo.setSig(nuevo);
        }
        ultimo = nuevo;
    }

    // Devuelve null si no hay elementos; el nodo retirado queda desconectado.
    public NodoLista<E> eliminarPrimero() {
        NodoLista<E> retirado = primero;
        if (retirado != null) {
            primero = retirado.getSig();
            retirado.setSig(null);
            if (primero == null) {
                ultimo = null;
            }
        }
        return retirado;
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

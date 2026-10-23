package org.example.proyectoestructura.estructura;

import java.util.StringJoiner;

public class Cola<E> {
    private NodoLista<E> frente;
    private NodoLista<E> fin;
    private int size;

    public boolean estaVacio() { return frente == null; }
    public int getSize() { return size; }

    public void encolar(E valor) {
        NodoLista<E> nuevo = new NodoLista<>(valor);
        if (fin == null) {
            frente = nuevo;
        } else {
            fin.setSig(nuevo);
        }
        fin = nuevo;
        size++;
    }

    public E desencolar() {
        if (estaVacio()) {
            return null;
        }
        NodoLista<E> retirado = frente;
        frente = retirado.getSig();
        retirado.setSig(null);
        if (frente == null) {
            fin = null;
        }
        size--;
        return retirado.getValor();
    }

    public E verFrente() {
        return estaVacio() ? null : frente.getValor();
    }

    @Override
    public String toString() {
        StringJoiner valores = new StringJoiner(", ", "[", "]");
        for (NodoLista<E> nodo = frente; nodo != null; nodo = nodo.getSig()) {
            valores.add(String.valueOf(nodo.getValor()));
        }
        return valores.toString();
    }
}

package org.example.proyectoestructura.estructura;

import java.util.StringJoiner;

public class Pila<E> {
    private NodoLista<E> cima;
    private int size;

    public boolean estaVacio() { return cima == null; }
    public int getSize() { return size; }

    public void apilar(E valor) {
        NodoLista<E> nuevo = new NodoLista<>(valor);
        nuevo.setSig(cima);
        cima = nuevo;
        size++;
    }

    public E desapilar() {
        if (estaVacio()) {
            return null;
        }
        NodoLista<E> retirado = cima;
        cima = retirado.getSig();
        retirado.setSig(null);
        size--;
        return retirado.getValor();
    }

    public E verCima() {
        return estaVacio() ? null : cima.getValor();
    }

    @Override
    public String toString() {
        StringJoiner valores = new StringJoiner(", ", "[", "]");
        for (NodoLista<E> nodo = cima; nodo != null; nodo = nodo.getSig()) {
            valores.add(String.valueOf(nodo.getValor()));
        }
        return valores.toString();
    }
}

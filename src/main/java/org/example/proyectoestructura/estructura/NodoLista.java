package org.example.proyectoestructura.estructura;

public class NodoLista<E> {
    private final E valor;
    private NodoLista<E> sig;

    public NodoLista(E valor) { this.valor = valor; }
    public E getValor() { return valor; }
    public NodoLista<E> getSig() { return sig; }
    void setSig(NodoLista<E> sig) { this.sig = sig; }
}

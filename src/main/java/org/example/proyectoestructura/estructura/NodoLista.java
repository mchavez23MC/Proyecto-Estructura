package org.example.proyectoestructura.estructura;

public class NodoLista<E> {
    private final E valor;
    private NodoLista<E> sig;
    private NodoLista<E> ant;

    public NodoLista(E valor) { this.valor = valor; }
    public E getValor() { return valor; }
    public NodoLista<E> getSig() { return sig; }
    public NodoLista<E> getAnt() { return ant; }
    void setSig(NodoLista<E> sig) { this.sig = sig; }
    void setAnt(NodoLista<E> ant) { this.ant = ant; }
}

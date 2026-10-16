package org.example.proyectoestructura.estructura;

public class NodoProceso<E, F> {
    private final E valor;
    private final ListaEnlazada<F> lista = new ListaEnlazada<>();
    private NodoProceso<E, F> sig;

    public NodoProceso(E valor) { this.valor = valor; }
    public E getValor() { return valor; }
    public ListaEnlazada<F> getLista() { return lista; }
    public NodoProceso<E, F> getSig() { return sig; }
    void setSig(NodoProceso<E, F> sig) { this.sig = sig; }
}

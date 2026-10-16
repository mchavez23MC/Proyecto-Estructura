package org.example.proyectoestructura.estructura;

import java.util.Comparator;

public class ListaArreglo <E>{
    private final Object[] elements;
    private int size;
    private int max;

    public ListaArreglo(int max) {
        this.elements = new Object[max];
        this.size = 0;
        this.max = max;
    }

    public boolean isEmpty() {return size == 0;}

    public int size() { return size; }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }
        return (E) elements[index];
    }

    public void addLast(E valor){
        if(size == max){return;}
        elements[size] = valor;
        size++;
    }

    public void removeLast(){
        if(size == 0){return;}
        elements[--size] = null;
    }

    public void addFirst(E valor){
        if(size == max){return;}
        for(int i = size; i > 0; i--){
            elements[i] = elements[i - 1];
        }
        elements[0] = valor;
        size++;
    }

    public void removeFirst(){
        if(size == 0){return;}
        for(int i = 0; i < size - 1; i++){
            elements[i] = elements[i + 1];
        }
        elements[--size] = null;
    }

    @SuppressWarnings("unchecked")
    public ListaArreglo<E> buscarMayores(E referencia, Comparator<? super E> comparador){
        ListaArreglo<E> resultado = new ListaArreglo<>(size);
        for (int i = 0; i < size; i++){
            E elemento = (E) elements[i];
            if (comparador.compare(elemento, referencia) > 0){
                resultado.addLast(elemento);
            }
        }
        return resultado;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < size; i++){
            s.append(elements[i]).append(" ");
        }
        return s.toString();
    }
}

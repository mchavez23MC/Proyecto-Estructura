package org.example.proyectoestructura.ejercicios;

public class MyClass {

    static class Nodo<E> {
        E dato;
        Nodo<E> siguiente;

        Nodo(E dato) {
            this.dato = dato;
        }
    }

    static class ListaEnlazada<E> {
        Nodo<E> primero;

        public boolean estaVacia() {
            return primero == null;
        }

        public void anadirInicio(E dato) {
            Nodo<E> nuevo = new Nodo<>(dato);
            nuevo.siguiente = primero;
            primero = nuevo;
        }

        public E eliminarPrim() {
            E dato = primero.dato;
            primero = primero.siguiente;
            return dato;
        }
    }

    public static void main(String[] args) {
        String exp = "7 - ((X* ((X+Y)/(J-3)) + Y) / (4-2.5))";
        System.out.println(exp + " -> " + estaBalanceado(exp));
    }

    public static boolean estaBalanceado(String palabra) {
        ListaEnlazada<String> P = new ListaEnlazada<>();
        for (int i = 0; i < palabra.length(); i++) {
            char c = palabra.charAt(i);
            if (c == '(')
                P.anadirInicio("(");
            else if (c == ')') {
                if (P.estaVacia()) return false;
                P.eliminarPrim();
            }
        }
        return P.estaVacia();
    }
}

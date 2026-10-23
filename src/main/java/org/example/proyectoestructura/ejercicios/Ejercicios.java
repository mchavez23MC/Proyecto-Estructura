package org.example.proyectoestructura.ejercicios;

import java.util.Scanner;

public class Ejercicios {

    static class Nodo<E> {
        E dato;
        Nodo<E> siguiente;

        Nodo(E dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    static class Pila<E> {
        Nodo<E> cima;
        int size;

        public Pila() {
            cima = null;
            size = 0;
        }

        public boolean estaVacia() {
            if (cima == null) {
                return true;
            }
            return false;
        }

        public int getSize() {
            return size;
        }

        public void apilar(E dato) {
            Nodo<E> nuevo = new Nodo<>(dato);
            nuevo.siguiente = cima;
            cima = nuevo;
            size++;
        }

        public E desapilar() {
            if (estaVacia()) {
                System.out.println("La pila esta vacia");
                return null;
            }
            E dato = cima.dato;
            cima = cima.siguiente;
            size--;
            return dato;
        }

        public E verCima() {
            if (estaVacia()) {
                return null;
            }
            return cima.dato;
        }

        public void mostrar() {
            Nodo<E> aux = cima;
            while (aux != null) {
                System.out.print(aux.dato + " ");
                aux = aux.siguiente;
            }
            System.out.println();
        }
    }

    static class Cola<E> {
        Nodo<E> frente;
        Nodo<E> fin;
        int size;

        public Cola() {
            frente = null;
            fin = null;
            size = 0;
        }

        public boolean estaVacia() {
            if (frente == null) {
                return true;
            }
            return false;
        }

        public int getSize() {
            return size;
        }

        public void encolar(E dato) {
            Nodo<E> nuevo = new Nodo<>(dato);
            if (estaVacia()) {
                frente = nuevo;
                fin = nuevo;
            } else {
                fin.siguiente = nuevo;
                fin = nuevo;
            }
            size++;
        }

        public E desencolar() {
            if (estaVacia()) {
                System.out.println("La cola esta vacia");
                return null;
            }
            E dato = frente.dato;
            frente = frente.siguiente;
            if (frente == null) {
                fin = null;
            }
            size--;
            return dato;
        }

        public E verFrente() {
            if (estaVacia()) {
                return null;
            }
            return frente.dato;
        }

        public void mostrar() {
            Nodo<E> aux = frente;
            while (aux != null) {
                System.out.print(aux.dato + " ");
                aux = aux.siguiente;
            }
            System.out.println();
        }
    }

    static class ListaEnlazada<E> {
        Nodo<E> primero;

        public ListaEnlazada() {
            primero = null;
        }

        public boolean estaVacia() {
            if (primero == null) {
                return true;
            }
            return false;
        }

        public void anadirInicio(E dato) {
            Nodo<E> nuevo = new Nodo<>(dato);
            nuevo.siguiente = primero;
            primero = nuevo;
        }

        public E eliminarPrim() {
            if (estaVacia()) {
                return null;
            }
            E dato = primero.dato;
            primero = primero.siguiente;
            return dato;
        }
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

    public static double evaluarPostfijo(String expresion) {
        Pila<Double> P = new Pila<>();
        String[] elementos = expresion.split(" ");
        for (int i = 0; i < elementos.length; i++) {
            String x = elementos[i];
            if (x.equals("+") || x.equals("-") || x.equals("*") || x.equals("/")) {
                double b = P.desapilar();
                double a = P.desapilar();
                if (x.equals("+")) {
                    P.apilar(a + b);
                } else if (x.equals("-")) {
                    P.apilar(a - b);
                } else if (x.equals("*")) {
                    P.apilar(a * b);
                } else {
                    P.apilar(a / b);
                }
            } else {
                P.apilar(Double.parseDouble(x));
            }
        }
        return P.desapilar();
    }

    public static int josephus(int n, int paso) {
        Cola<Integer> soldados = new Cola<>();
        for (int i = 1; i <= n; i++) {
            soldados.encolar(i);
        }
        while (soldados.getSize() > 1) {
            for (int i = 1; i < paso; i++) {
                soldados.encolar(soldados.desencolar());
            }
            int muerto = soldados.desencolar();
            System.out.print(muerto + " ");
        }
        System.out.println();
        return soldados.desencolar();
    }

    public static boolean esCapicua(int numero) {
        String digitos = String.valueOf(numero);
        Cola<Character> cola = new Cola<>();
        Pila<Character> pila = new Pila<>();
        for (int i = 0; i < digitos.length(); i++) {
            cola.encolar(digitos.charAt(i));
            pila.apilar(digitos.charAt(i));
        }
        while (!cola.estaVacia()) {
            char deCola = cola.desencolar();
            char dePila = pila.desapilar();
            if (deCola != dePila) {
                return false;
            }
        }
        return true;
    }

    static class BinaryCounter {

        public void imprimirBinarios(int valor) {
            Cola<String> cola = new Cola<>();
            cola.encolar("1");
            for (int i = 1; i <= valor; i++) {
                String actual = cola.desencolar();
                System.out.println(actual);
                cola.encolar(actual + "0");
                cola.encolar(actual + "1");
            }
        }

        public void ejecutar() {
            Scanner sc = new Scanner(System.in);
            System.out.print("Ingrese un valor: ");
            int valor = sc.nextInt();
            imprimirBinarios(valor);
            sc.close();
        }
    }

    public static void main(String[] args) {
        System.out.println("Prueba pila:");
        Pila<Integer> pila = new Pila<>();
        pila.apilar(1);
        pila.apilar(2);
        pila.apilar(3);
        pila.mostrar();
        System.out.println("Cima: " + pila.verCima());
        System.out.println("Desapilo: " + pila.desapilar());
        pila.mostrar();

        System.out.println();
        System.out.println("Prueba cola:");
        Cola<String> cola = new Cola<>();
        cola.encolar("Ana");
        cola.encolar("Luis");
        cola.encolar("Sofia");
        cola.mostrar();
        System.out.println("Frente: " + cola.verFrente());
        System.out.println("Desencolo: " + cola.desencolar());
        cola.mostrar();

        System.out.println();
        System.out.println("Parentesis:");
        String exp = "7 - ((X* ((X+Y)/(J-3)) + Y) / (4-2.5))";
        System.out.println(exp + " -> " + estaBalanceado(exp));
        System.out.println("((a + b) -> " + estaBalanceado("((a + b)"));
        System.out.println("(a + b)) -> " + estaBalanceado("(a + b))"));

        System.out.println();
        System.out.println("Postfijo:");
        System.out.println("2 1 + 3 * 2 3 / - -> " + evaluarPostfijo("2 1 + 3 * 2 3 / -"));
        System.out.println("5 3 - -> " + evaluarPostfijo("5 3 -"));

        System.out.println();
        System.out.println("Josephus 40 soldados, paso 7:");
        System.out.print("Orden en que mueren: ");
        int posicion = josephus(40, 7);
        System.out.println("Josephus se sienta en: " + posicion);

        System.out.println();
        System.out.println("Josephus 7 soldados, paso 3:");
        System.out.print("Orden en que mueren: ");
        System.out.println("Josephus se sienta en: " + josephus(7, 3));

        System.out.println();
        System.out.println("Capicuas:");
        System.out.println("121 -> " + esCapicua(121));
        System.out.println("55 -> " + esCapicua(55));
        System.out.println("252 -> " + esCapicua(252));
        System.out.println("123 -> " + esCapicua(123));

        System.out.println();
        System.out.println("Prueba cola vacia:");
        Cola<Integer> vacia = new Cola<>();
        System.out.println("Desencolo: " + vacia.desencolar());

        System.out.println();
        System.out.println("Numeros binarios:");
        BinaryCounter contador = new BinaryCounter();
        contador.ejecutar();
    }
}

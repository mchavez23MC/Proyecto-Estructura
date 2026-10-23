package org.example.proyectoestructura.ejercicios;

import org.example.proyectoestructura.estructura.Pila;

public class ParserParentesis {
    private static final String APERTURAS = "([{";
    private static final String CIERRES = ")]}";

    public static boolean estaBalanceada(String expresion) {
        Pila<Character> pila = new Pila<>();
        for (int i = 0; i < expresion.length(); i++) {
            char c = expresion.charAt(i);
            if (APERTURAS.indexOf(c) != -1) {
                pila.apilar(c);
            } else if (CIERRES.indexOf(c) != -1) {
                if (pila.estaVacio()) {
                    return false;
                }
                char abierto = pila.desapilar();
                if (APERTURAS.indexOf(abierto) != CIERRES.indexOf(c)) {
                    return false;
                }
            }
        }
        return pila.estaVacio();
    }
}

package org.example.proyectoestructura.ejercicios;

import org.example.proyectoestructura.estructura.Cola;

public class MainEjercicios {
    public static void main(String[] args) {
        String[] expresiones = {
                "(a + b) * (c - d)",
                "{[(2 + 3) * 4] - 1}",
                "((a + b)",
                "(a + b))",
                "[(a + b])",
                ""
        };
        System.out.println("Parser de paréntesis:");
        for (String expresion : expresiones) {
            System.out.println("\"" + expresion + "\" -> "
                    + (ParserParentesis.estaBalanceada(expresion) ? "balanceada" : "no balanceada"));
        }

        System.out.println();
        System.out.println("Josephus con k = 3:");
        Cola<String> personas = new Cola<>();
        for (String nombre : new String[] {"Ana", "Luis", "Sofia", "Carlos", "Maria", "Pedro", "Lucia"}) {
            personas.encolar(nombre);
        }
        System.out.println("Personas: " + personas);
        String ganador = Josephus.josephus(personas, 3, System.out);
        System.out.println("Sobrevive: " + ganador);
    }
}

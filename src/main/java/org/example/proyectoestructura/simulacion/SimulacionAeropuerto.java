package org.example.proyectoestructura.simulacion;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Random;
import org.example.proyectoestructura.estructura.ListaEnlazada;
import org.example.proyectoestructura.estructura.ListaProcesos;
import org.example.proyectoestructura.estructura.NodoLista;
import org.example.proyectoestructura.estructura.NodoProceso;
import org.example.proyectoestructura.modelo.Pasajero;

public class SimulacionAeropuerto {
    private final ListaEnlazada<Pasajero> pendientes;
    private final ListaProcesos<String, Pasajero> mostradores = new ListaProcesos<>();
    private final Random aleatorio;
    private final PrintStream salida;

    public SimulacionAeropuerto(ListaEnlazada<Pasajero> pendientes,
                               Random aleatorio, PrintStream salida) {
        this.pendientes = Objects.requireNonNull(pendientes);
        this.aleatorio = Objects.requireNonNull(aleatorio);
        this.salida = Objects.requireNonNull(salida);
        for (int i = 1; i <= 3; i++) {
            mostradores.agregarUltimo("Mostrador " + i);
        }
    }

    public ListaProcesos<String, Pasajero> getMostradores() { return mostradores; }

    public boolean haTerminado() {
        return pendientes.estaVacio() && mostradores.estanVaciasLA();
    }

    public void ejecutar() {
        salida.println("Estado inicial:");
        imprimirEstado();
        int ronda = 0;
        while (!haTerminado()) {
            salida.println("Ronda " + ++ronda);
            ejecutarRonda();
        }
        salida.println("Simulación terminada: no quedan pasajeros pendientes.");
    }

    public void ejecutarRonda() {
        if (haTerminado()) {
            return;
        }
        distribuirPasajeros();
        atenderPasajero();
        imprimirEstado();
    }

    private void distribuirPasajeros() {
        int cantidad = pendientes.getSize();
        if (cantidad == 0) {
            return;
        }
        int np = cantidad <= 2 ? cantidad : aleatorio.nextInt(cantidad / 2) + 1;
        salida.println("Llegan " + np + " pasajeros a los mostradores.");
        NodoProceso<String, Pasajero> mostrador = mostradores.getPrimero();
        for (int i = 0; i < np; i++) {
            Pasajero pasajero = pendientes.eliminarPrimero().getValor();
            mostrador.getLista().agregarUltimo(pasajero);
            salida.println("Asignado " + pasajero.getId() + " a " + mostrador.getValor());
            mostrador = mostrador.getSig();
        }
    }

    private int contarOcupados() {
        int ocupados = 0;
        NodoProceso<String, Pasajero> nodo = mostradores.getPrimero();
        for (int i = 0; i < mostradores.getSize(); i++) {
            if (!nodo.getLista().estaVacio()) {
                ocupados++;
            }
            nodo = nodo.getSig();
        }
        return ocupados;
    }

    private void atenderPasajero() {
        // Elegir uniformemente entre filas ocupadas evita desperdiciar turnos.
        int ocupados = contarOcupados();
        if (ocupados == 0) {
            return;
        }
        int elegido = aleatorio.nextInt(ocupados);
        NodoProceso<String, Pasajero> nodo = mostradores.getPrimero();
        while (nodo.getLista().estaVacio() || elegido-- > 0) {
            nodo = nodo.getSig();
        }
        NodoLista<Pasajero> atendido = nodo.getLista().eliminarPrimero();
        if (atendido != null) {
            salida.println("Atendido " + atendido.getValor().getId()
                    + " en " + nodo.getValor());
        }
    }

    private void imprimirEstado() {
        salida.println("Pasajeros en origen: " + pendientes.getSize());
        salida.println(mostradores);
    }
}

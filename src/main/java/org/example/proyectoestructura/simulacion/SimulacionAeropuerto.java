package org.example.proyectoestructura.simulacion;

import java.io.PrintStream;
import java.util.Objects;
import java.util.Random;
import org.example.proyectoestructura.estructura.ListaEnlazada;
import org.example.proyectoestructura.estructura.ListaProcesos;
import org.example.proyectoestructura.estructura.NodoLista;
import org.example.proyectoestructura.estructura.NodoProceso;
import org.example.proyectoestructura.modelo.Maleta;
import org.example.proyectoestructura.modelo.Pasajero;

public class SimulacionAeropuerto {
    private final ListaEnlazada<Pasajero> pendientes;
    private final ListaEnlazada<Maleta> maletas;
    private final ListaProcesos<String, Pasajero> mostradores = new ListaProcesos<>();
    private final ListaProcesos<String, Maleta> vuelos = new ListaProcesos<>();
    private final Random aleatorio;
    private final PrintStream salida;

    public SimulacionAeropuerto(ListaEnlazada<Pasajero> pendientes, ListaEnlazada<Maleta> maletas,
                               Random aleatorio, PrintStream salida) {
        this.pendientes = Objects.requireNonNull(pendientes);
        this.maletas = Objects.requireNonNull(maletas);
        this.aleatorio = Objects.requireNonNull(aleatorio);
        this.salida = Objects.requireNonNull(salida);
        for (int i = 1; i <= 3; i++) {
            mostradores.agregarUltimo("Mostrador " + i);
        }
    }

    public ListaProcesos<String, Pasajero> getMostradores() { return mostradores; }
    public ListaProcesos<String, Maleta> getVuelos() { return vuelos; }
    public ListaEnlazada<Maleta> getMaletas() { return maletas; }

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
        for (int i = 0; i < np; i++) {
            Pasajero pasajero = pendientes.eliminarPrimero().getValor();
            NodoProceso<String, Pasajero> mostrador = buscarFilaMasCorta();
            mostrador.getLista().agregarUltimo(pasajero);
            salida.println("Asignado " + pasajero.getId() + " a " + mostrador.getValor());
        }
    }

    private NodoProceso<String, Pasajero> buscarFilaMasCorta() {
        NodoProceso<String, Pasajero> menor = mostradores.getPrimero();
        NodoProceso<String, Pasajero> nodo = menor.getSig();
        for (int i = 1; i < mostradores.getSize(); i++) {
            if (nodo.getLista().getSize() < menor.getLista().getSize()) {
                menor = nodo;
            }
            nodo = nodo.getSig();
        }
        return menor;
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
            moverMaletas(atendido.getValor());
        }
    }

    private void moverMaletas(Pasajero pasajero) {
        int cantidad = 0;
        NodoLista<Maleta> nodo = maletas.getPrimero();
        while (nodo != null) {
            NodoLista<Maleta> siguiente = nodo.getSig();
            Maleta maleta = nodo.getValor();
            if (maleta.getIdPasajero().equals(pasajero.getId())) {
                maletas.eliminarNodo(nodo);
                NodoProceso<String, Maleta> vuelo = vuelos.buscar(maleta.getVuelo());
                if (vuelo == null) {
                    vuelos.agregarUltimo(maleta.getVuelo());
                    vuelo = vuelos.getUltimo();
                    salida.println("Se crea el vuelo " + maleta.getVuelo() + " en LM");
                }
                vuelo.getLista().agregarUltimo(maleta);
                salida.println("Maleta " + maleta.getId() + " de " + pasajero.getId()
                        + " pasa al vuelo " + maleta.getVuelo());
                cantidad++;
            }
            nodo = siguiente;
        }
        if (cantidad == 0) {
            salida.println(pasajero.getId() + " no tiene maletas");
        }
    }

    private void imprimirEstado() {
        salida.println("Pasajeros en origen: " + pendientes.getSize());
        salida.println(mostradores);
        salida.println("Maletas en LMaletas: " + maletas.getSize());
        if (vuelos.estaVacio()) {
            salida.println("Vuelos en LM: ninguno");
        } else {
            salida.println("Vuelos en LM:");
            salida.println(vuelos);
        }
    }
}

package org.example.proyectoestructura.simulacion;

import java.io.PrintStream;
import java.util.Random;
import org.example.proyectoestructura.estructura.ListaEnlazada;
import org.example.proyectoestructura.estructura.ListaProcesos;
import org.example.proyectoestructura.estructura.NodoLista;
import org.example.proyectoestructura.modelo.Maleta;
import org.example.proyectoestructura.modelo.Pasajero;

public class SimulacionAeropuerto {
    private final ListaEnlazada<Pasajero> pendientes;
    private final ListaEnlazada<Maleta> maletas;
    private final ListaProcesos<String, Pasajero> mostradores = new ListaProcesos<>();
    private final ListaProcesos<String, Maleta> vuelos = new ListaProcesos<>();
    private final Random aleatorio;
    private final PrintStream salida;
    private int atendidos;
    private int maletasProcesadas;

    public SimulacionAeropuerto(ListaEnlazada<Pasajero> pendientes, ListaEnlazada<Maleta> maletas,
                               Random aleatorio, PrintStream salida) {
        this.pendientes = pendientes;
        this.maletas = maletas;
        this.aleatorio = aleatorio;
        this.salida = salida;
        for (int i = 1; i <= 3; i++) {
            mostradores.agregarProceso("Mostrador " + i);
        }
    }

    public static ListaEnlazada<Maleta> crearMaletas(ListaEnlazada<Pasajero> pasajeros, Random aleatorio) {
        ListaEnlazada<Maleta> lista = new ListaEnlazada<>();
        int numero = 1;
        for (NodoLista<Pasajero> nodo = pasajeros.getPrimero(); nodo != null; nodo = nodo.getSig()) {
            Pasajero pasajero = nodo.getValor();
            for (int i = 0; i < pasajero.getCantidadMaletas(); i++) {
                double peso = 5 + aleatorio.nextInt(20);
                lista.agregarUltimo(new Maleta("M%03d".formatted(numero), peso,
                        pasajero.getId(), pasajero.getVuelo()));
                numero++;
            }
        }
        return lista;
    }

    public ListaProcesos<String, Pasajero> getMostradores() { return mostradores; }
    public ListaProcesos<String, Maleta> getVuelos() { return vuelos; }
    public ListaEnlazada<Maleta> getMaletas() { return maletas; }
    public ListaEnlazada<Pasajero> getPendientes() { return pendientes; }
    public int getAtendidos() { return atendidos; }
    public int getMaletasProcesadas() { return maletasProcesadas; }

    public boolean haTerminado() {
        return pendientes.estaVacio() && mostradores.estanVaciasLA();
    }

    public boolean equipajeTerminado() {
        return vuelos.estaVacio();
    }

    public void ejecutar() {
        salida.println("Estado inicial:");
        imprimirEstado();
        ejecutarCheckIn();
        procesarTodasLasMaletas();
        salida.println("Simulación terminada.");
    }

    public void ejecutarCheckIn() {
        int ronda = 0;
        while (!haTerminado()) {
            ronda++;
            salida.println("===== Ciclo " + ronda + " =====");
            ejecutarRonda();
        }
        salida.println("Check-in terminado: no quedan pasajeros pendientes.");
    }

    public void ejecutarRonda() {
        if (haTerminado()) {
            return;
        }
        distribuirPasajeros();
        atenderPasajero();
        decidirCierre();
        imprimirEstado();
    }

    public void distribuirPasajeros() {
        int cantidad = pendientes.getSize();
        if (cantidad == 0) {
            return;
        }
        int np;
        if (cantidad <= 2) {
            np = cantidad;
        } else {
            np = aleatorio.nextInt(cantidad / 2) + 1;
        }
        salida.println("Llegan " + np + " pasajeros a los mostradores.");
        for (int i = 0; i < np; i++) {
            Pasajero pasajero = pendientes.eliminarPrimero().getValor();
            String mostrador = mostradores.obtenerProcesoMasCorto();
            mostradores.agregar(mostrador, pasajero);
            salida.println("Asignado " + pasajero.getId() + " a " + mostrador);
        }
    }

    private int contarOcupados() {
        int ocupados = 0;
        for (int i = 0; i < mostradores.totalDeProcesos(); i++) {
            if (mostradores.tamanoProceso(mostradores.getValorXId(i)) > 0) {
                ocupados++;
            }
        }
        return ocupados;
    }

    public void atenderPasajero() {
        int ocupados = contarOcupados();
        if (ocupados == 0) {
            return;
        }
        int elegido = aleatorio.nextInt(ocupados);
        String mostrador = null;
        for (int i = 0; i < mostradores.totalDeProcesos(); i++) {
            String actual = mostradores.getValorXId(i);
            if (mostradores.tamanoProceso(actual) > 0) {
                if (elegido == 0) {
                    mostrador = actual;
                    break;
                }
                elegido--;
            }
        }
        Pasajero pasajero = mostradores.eliminar(mostrador);
        if (pasajero != null) {
            atendidos++;
            salida.println("Atendido " + pasajero.getId() + " en " + mostrador);
            moverMaletas(pasajero);
        }
    }

    public void decidirCierre() {
        if (aleatorio.nextInt(5) != 0) {
            return;
        }
        int pos = aleatorio.nextInt(mostradores.totalDeProcesos());
        cerrarMostrador(mostradores.getValorXId(pos));
    }

    public boolean cerrarMostrador(String mostrador) {
        if (mostradores.encontrarProceso(mostrador) == null) {
            salida.println("No existe " + mostrador);
            return false;
        }
        if (mostradores.totalDeProcesos() == 1) {
            salida.println("No se puede cerrar " + mostrador + ": es el único mostrador abierto");
            return false;
        }
        salida.println("Se cierra " + mostrador);
        while (mostradores.tamanoProceso(mostrador) > 0) {
            Pasajero pasajero = mostradores.eliminar(mostrador);
            String destino = mostradores.obtenerProcesoMasCorto(mostrador);
            mostradores.agregar(destino, pasajero);
            salida.println("Reasignado " + pasajero.getId() + " de " + mostrador + " a " + destino);
        }
        mostradores.removerProceso(mostrador);
        return true;
    }

    private void moverMaletas(Pasajero pasajero) {
        int cantidad = 0;
        NodoLista<Maleta> nodo = maletas.getPrimero();
        while (nodo != null) {
            NodoLista<Maleta> siguiente = nodo.getSig();
            Maleta maleta = nodo.getValor();
            if (maleta.getIdPasajero().equals(pasajero.getId())) {
                maletas.eliminarNodo(nodo);
                if (vuelos.agregarProceso(maleta.getVuelo())) {
                    salida.println("Se crea el vuelo " + maleta.getVuelo() + " en LM");
                }
                vuelos.agregar(maleta.getVuelo(), maleta);
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

    public void procesarMaleta() {
        if (vuelos.estaVacio()) {
            salida.println("No hay maletas por procesar.");
            return;
        }
        int pos = aleatorio.nextInt(vuelos.totalDeProcesos());
        procesarMaleta(vuelos.getValorXId(pos));
    }

    public Maleta procesarMaleta(String vuelo) {
        Maleta maleta = vuelos.eliminar(vuelo);
        if (maleta == null) {
            salida.println("El vuelo " + vuelo + " no tiene maletas por procesar.");
        } else {
            maletasProcesadas++;
            salida.println("Procesada maleta " + maleta.getId() + " del vuelo " + vuelo);
        }
        if (vuelos.tamanoProceso(vuelo) == 0) {
            vuelos.removerProceso(vuelo);
            salida.println("Se elimina el vuelo " + vuelo + " de LM");
        }
        return maleta;
    }

    public void procesarTodasLasMaletas() {
        salida.println("===== Procesamiento de equipaje =====");
        while (!vuelos.estaVacio()) {
            procesarMaleta();
        }
        imprimirEstado();
    }

    public void imprimirEstado() {
        salida.println("Pasajeros en LPasajeros: " + pendientes.getSize());
        salida.println("CHECK-IN");
        if (mostradores.estaVacio()) {
            salida.println("(sin mostradores)");
        } else {
            salida.println(mostradores);
        }
        salida.println("BAGGAGE");
        if (vuelos.estaVacio()) {
            salida.println("(sin vuelos)");
        } else {
            salida.println(vuelos);
        }
        salida.println("Total pasajeros esperando: " + mostradores.totalElementos());
        salida.println("Total maletas esperando: " + vuelos.totalElementos());
        salida.println("Maletas en LMaletas: " + maletas.getSize());
    }
}

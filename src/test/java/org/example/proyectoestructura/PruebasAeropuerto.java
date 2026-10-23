package org.example.proyectoestructura;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.example.proyectoestructura.estructura.ListaEnlazada;
import org.example.proyectoestructura.estructura.ListaProcesos;
import org.example.proyectoestructura.estructura.NodoLista;
import org.example.proyectoestructura.estructura.NodoProceso;
import org.example.proyectoestructura.modelo.Maleta;
import org.example.proyectoestructura.modelo.Pasajero;
import org.example.proyectoestructura.simulacion.SimulacionAeropuerto;

public class PruebasAeropuerto {
    public static void main(String[] args) {
        probarLista();
        probarListaDoble();
        probarEliminarNodo();
        probarProcesos();
        probarCircular();
        probarBuscarProceso();
        probarRepartoYOrden();
        probarRangoAleatorio();
        probarMaletas();
        probarSimulaciones();
        System.out.println("Todas las pruebas pasaron.");
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private static void probarLista() {
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        comprobar(lista.estaVacio() && lista.getSize() == 0, "Lista inicialmente vacía");
        comprobar(lista.eliminarPrimero() == null, "Extracción vacía segura");
        lista.agregarUltimo(10);
        lista.agregarUltimo(20);
        NodoLista<Integer> retirado = lista.eliminarPrimero();
        comprobar(retirado.getValor() == 10 && retirado.getSig() == null, "Extracción FIFO");
        comprobar(lista.getSize() == 1 && lista.eliminarPrimero().getValor() == 20, "Tamaño y orden");
        lista.agregarUltimo(30);
        comprobar(lista.getPrimero().getValor() == 30 && lista.getSize() == 1,
                "Reutilización después de vaciar");
        comprobar(lista.toString().equals("[30]"), "Impresión de lista");
    }

    private static void probarListaDoble() {
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        comprobar(lista.eliminarUltimo() == null, "Extracción final vacía segura");
        lista.agregarUltimo(20);
        lista.agregarUltimo(30);
        lista.agregarPrimero(10);
        comprobar(lista.toString().equals("[10, 20, 30]"), "Recorrido hacia adelante");
        comprobar(lista.toStringInverso().equals("[30, 20, 10]"), "Recorrido hacia atrás");
        comprobar(lista.getPrimero().getAnt() == null && lista.getUltimo().getSig() == null,
                "Extremos sin enlaces externos");
        comprobar(lista.getPrimero().getSig().getAnt() == lista.getPrimero(), "Enlace anterior");
        NodoLista<Integer> retirado = lista.eliminarUltimo();
        comprobar(retirado.getValor() == 30 && retirado.getAnt() == null, "Extracción del último");
        comprobar(lista.getUltimo().getValor() == 20 && lista.getUltimo().getSig() == null,
                "Nuevo último");
        retirado = lista.eliminarPrimero();
        comprobar(retirado.getValor() == 10 && lista.getPrimero().getAnt() == null, "Nuevo primero");
        lista.eliminarUltimo();
        comprobar(lista.estaVacio() && lista.getUltimo() == null, "Lista vacía desde el final");
    }

    private static void probarEliminarNodo() {
        ListaEnlazada<Integer> lista = new ListaEnlazada<>();
        comprobar(lista.eliminarNodo(null) == null, "Eliminar null es seguro");
        for (int i = 1; i <= 4; i++) {
            lista.agregarUltimo(i * 10);
        }
        NodoLista<Integer> medio = lista.getPrimero().getSig();
        NodoLista<Integer> retirado = lista.eliminarNodo(medio);
        comprobar(retirado == medio && retirado.getSig() == null && retirado.getAnt() == null,
                "Nodo intermedio desconectado");
        comprobar(lista.toString().equals("[10, 30, 40]"), "Recorrido sin el nodo intermedio");
        comprobar(lista.toStringInverso().equals("[40, 30, 10]"), "Enlaces anteriores reparados");
        lista.eliminarNodo(lista.getPrimero());
        lista.eliminarNodo(lista.getUltimo());
        comprobar(lista.toString().equals("[30]") && lista.getPrimero() == lista.getUltimo(),
                "Eliminar extremos");
        lista.eliminarNodo(lista.getPrimero());
        comprobar(lista.estaVacio() && lista.getUltimo() == null, "Lista vacía tras eliminar nodos");
    }

    private static void probarBuscarProceso() {
        ListaProcesos<String, Integer> procesos = new ListaProcesos<>();
        comprobar(procesos.buscar("V1") == null, "Buscar en lista vacía");
        procesos.agregarUltimo("V1");
        procesos.agregarUltimo("V2");
        procesos.agregarUltimo("V3");
        comprobar(procesos.buscar("V3") == procesos.getUltimo(), "Encuentra el último proceso");
        comprobar(procesos.buscar("V1") == procesos.getPrimero(), "Encuentra el primer proceso");
        comprobar(procesos.buscar("V9") == null, "Valor inexistente sin ciclo infinito");
    }

    private static void probarCircular() {
        ListaProcesos<String, Integer> procesos = new ListaProcesos<>();
        procesos.agregarUltimo("M1");
        comprobar(procesos.getPrimero().getSig() == procesos.getPrimero(), "Un proceso apunta a sí mismo");
        procesos.agregarUltimo("M2");
        procesos.agregarUltimo("M3");
        comprobar(procesos.getUltimo().getSig() == procesos.getPrimero(), "El último vuelve al primero");
        NodoProceso<String, Integer> nodo = procesos.getPrimero();
        for (int i = 0; i < 7; i++) {
            nodo = nodo.getSig();
        }
        comprobar(nodo.getValor().equals("M2"), "Rotación continua sin null");
        comprobar(procesos.toString().equals("M1: []" + System.lineSeparator() + "M2: []"
                + System.lineSeparator() + "M3: []"), "Impresión de una sola vuelta");
    }

    private static void probarProcesos() {
        ListaProcesos<String, Integer> procesos = new ListaProcesos<>();
        comprobar(procesos.estaVacio() && procesos.estanVaciasLA(), "Sin procesos");
        for (int i = 1; i <= 3; i++) {
            procesos.agregarUltimo("M" + i);
        }
        comprobar(!procesos.estaVacio() && procesos.estanVaciasLA(), "Procesos con filas vacías");
        comprobar(procesos.getSize() == 3 && procesos.getNodoXId(2).getValor().equals("M3"),
                "Posiciones desde cero");
        procesos.getNodoXId(1).getLista().agregarUltimo(42);
        comprobar(!procesos.estanVaciasLA() && procesos.getNodoXId(0).getLista().estaVacio(),
                "Filas independientes");
        for (int posicion : new int[] {-1, 3}) {
            try {
                procesos.getNodoXId(posicion);
                throw new AssertionError("Debe rechazar posición " + posicion);
            } catch (IndexOutOfBoundsException esperado) {
            }
        }
    }

    private static ListaEnlazada<Pasajero> pasajeros(int cantidad) {
        ListaEnlazada<Pasajero> lista = new ListaEnlazada<>();
        for (int i = 1; i <= cantidad; i++) {
            lista.agregarUltimo(new Pasajero("P" + i, "Nombre", "Apellido", "Documento"));
        }
        return lista;
    }

    private static Random controlado() {
        return new Random(0) {
            private int llamadas;
            @Override
            public int nextInt(int limite) {
                return llamadas++ % 2 == 0 ? limite - 1 : 0;
            }
        };
    }

    private static void probarRepartoYOrden() {
        ListaEnlazada<Pasajero> origen = pasajeros(12);
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, new ListaEnlazada<>(),
                controlado(), new PrintStream(registro));
        simulacion.ejecutarRonda();
        comprobar(origen.getSize() == 6, "Lote de la mitad del origen");
        comprobar(ids(simulacion, 0).equals("P4"), "Mostrador 1 atiende P1 y conserva P4");
        comprobar(ids(simulacion, 1).equals("P2 P5"), "Filas iguales: va al primer mostrador");
        comprobar(ids(simulacion, 2).equals("P3 P6"), "Filas iguales: reparto parejo");
        simulacion.ejecutarRonda();
        comprobar(ids(simulacion, 0).equals("P7 P8"), "P7 y P8 van a la fila más corta; atención FIFO");
        comprobar(ids(simulacion, 1).equals("P2 P5 P9"), "Empate entre 2 y 3: primer mostrador encontrado");
        comprobar(ids(simulacion, 2).equals("P3 P6"), "Se preservan pasajeros anteriores");
    }

    private static void probarMaletas() {
        ListaEnlazada<Maleta> maletas = new ListaEnlazada<>();
        maletas.agregarUltimo(new Maleta("A", 10.0, "P1", "V1"));
        maletas.agregarUltimo(new Maleta("B", 10.0, "P2", "V2"));
        maletas.agregarUltimo(new Maleta("C", 10.0, "P1", "V1"));
        maletas.agregarUltimo(new Maleta("D", 10.0, "P3", "V1"));
        maletas.agregarUltimo(new Maleta("E", 10.0, "PX", "V3"));
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = new SimulacionAeropuerto(pasajeros(4), maletas,
                controlado(), new PrintStream(registro));
        simulacion.ejecutarRonda();
        comprobar(maletas.getSize() == 3, "Solo salen las maletas del pasajero atendido");
        ListaProcesos<String, Maleta> vuelos = simulacion.getVuelos();
        comprobar(vuelos.getSize() == 1 && vuelos.buscar("V1").getLista().getSize() == 2,
                "Se crea el vuelo V1 con las dos maletas de P1");
        simulacion.ejecutar();
        comprobar(maletas.getSize() == 1 && maletas.getPrimero().getValor().getId().equals("E"),
                "La maleta sin pasajero queda en LMaletas");
        comprobar(vuelos.getSize() == 2, "No se repiten vuelos en LM");
        comprobar(vuelos.buscar("V1").getLista().getSize() == 3, "La maleta de P3 se suma al vuelo existente");
        comprobar(vuelos.buscar("V2").getLista().getPrimero().getValor().getId().equals("B"),
                "La maleta de P2 crea el vuelo V2");
        comprobar(registro.toString().contains("P4 no tiene maletas"), "Pasajero sin maletas");
    }

    private static String ids(SimulacionAeropuerto simulacion, int posicion) {
        StringBuilder ids = new StringBuilder();
        for (NodoLista<Pasajero> nodo = simulacion.getMostradores().getNodoXId(posicion)
                .getLista().getPrimero(); nodo != null; nodo = nodo.getSig()) {
            if (!ids.isEmpty()) {
                ids.append(' ');
            }
            ids.append(nodo.getValor().getId());
        }
        return ids.toString();
    }

    private static void probarRangoAleatorio() {
        for (boolean maximo : new boolean[] {false, true}) {
            Random extremo = new Random(0) {
                @Override
                public int nextInt(int limite) {
                    comprobar(limite > 0, "Límite aleatorio positivo");
                    return maximo ? limite - 1 : 0;
                }
            };
            ListaEnlazada<Pasajero> origen = pasajeros(9);
            SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, new ListaEnlazada<>(),
                    extremo, new PrintStream(new ByteArrayOutputStream()));
            simulacion.ejecutarRonda();
            comprobar(origen.getSize() == (maximo ? 5 : 8), "Lote inclusivo entre 1 y mitad entera");
        }
    }

    private static void probarSimulaciones() {
        for (int cantidad : new int[] {0, 1, 2, 3, 12, 50}) {
            for (int semilla = 0; semilla < 20; semilla++) {
                ListaEnlazada<Pasajero> origen = pasajeros(cantidad);
                ListaEnlazada<Maleta> maletas = new ListaEnlazada<>();
                int totalMaletas = 0;
                for (int i = 1; i <= cantidad; i++) {
                    for (int j = 0; j < i % 3; j++) {
                        maletas.agregarUltimo(new Maleta("M" + i + "-" + j, 10.0, "P" + i, "V" + i % 4));
                        totalMaletas++;
                    }
                }
                ByteArrayOutputStream registro = new ByteArrayOutputStream();
                SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, maletas,
                        new Random(semilla), new PrintStream(registro));
                Set<String> atendidos = new HashSet<>();
                int rondas = 0;
                while (!simulacion.haTerminado()) {
                    comprobar(++rondas <= cantidad, "Finalización sin turnos perdidos");
                    registro.reset();
                    simulacion.ejecutarRonda();
                    long atenciones = 0;
                    for (String linea : registro.toString().lines().toList()) {
                        if (linea.startsWith("Atendido ")) {
                            String id = linea.split(" ")[1];
                            comprobar(atendidos.add(id), "Atención única: " + id);
                            atenciones++;
                        }
                    }
                    comprobar(atenciones == 1, "Una atención por ronda");
                    Set<String> presentes = new HashSet<>(atendidos);
                    for (NodoLista<Pasajero> nodo = origen.getPrimero(); nodo != null; nodo = nodo.getSig()) {
                        comprobar(presentes.add(nodo.getValor().getId()), "Origen sin duplicados");
                    }
                    for (int i = 0; i < simulacion.getMostradores().getSize(); i++) {
                        NodoProceso<String, Pasajero> mostrador = simulacion.getMostradores().getNodoXId(i);
                        for (NodoLista<Pasajero> nodo = mostrador.getLista().getPrimero();
                             nodo != null; nodo = nodo.getSig()) {
                            comprobar(presentes.add(nodo.getValor().getId()), "Filas sin duplicados");
                        }
                    }
                    comprobar(presentes.size() == cantidad, "Conservación de pasajeros en cada ronda");
                    if (cantidad <= 2) {
                        comprobar(origen.estaVacio(), "Trasladar todos cuando quedan dos o menos");
                    }
                }
                comprobar(atendidos.size() == cantidad && origen.estaVacio()
                        && simulacion.getMostradores().estanVaciasLA(), "Todos atendidos");
                int enVuelos = 0;
                for (int i = 0; i < simulacion.getVuelos().getSize(); i++) {
                    NodoProceso<String, Maleta> vuelo = simulacion.getVuelos().getNodoXId(i);
                    for (NodoLista<Maleta> nodo = vuelo.getLista().getPrimero(); nodo != null; nodo = nodo.getSig()) {
                        comprobar(nodo.getValor().getVuelo().equals(vuelo.getValor()), "Maleta en su vuelo");
                        enVuelos++;
                    }
                }
                comprobar(maletas.estaVacio() && enVuelos == totalMaletas, "Todas las maletas llegan a LM");
                registro.reset();
                simulacion.ejecutarRonda();
                comprobar(registro.size() == 0, "Ronda terminada no hace cambios");
            }
        }
    }
}

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
import org.example.proyectoestructura.modelo.Pasajero;
import org.example.proyectoestructura.simulacion.SimulacionAeropuerto;

// Pruebas sin dependencias externas. Lanzan AssertionError si algo falla.
public class PruebasAeropuerto {
    public static void main(String[] args) {
        probarLista();
        probarProcesos();
        probarRepartoYOrden();
        probarRangoAleatorio();
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
                // Posición inválida correctamente rechazada.
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

    private static void probarRepartoYOrden() {
        // Máximo lote permitido, y atención siempre en el primer mostrador ocupado.
        Random controlado = new Random(0) {
            private int llamadas;
            @Override
            public int nextInt(int limite) {
                return llamadas++ % 2 == 0 ? limite - 1 : 0;
            }
        };
        ListaEnlazada<Pasajero> origen = pasajeros(12);
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, controlado,
                new PrintStream(registro));
        simulacion.ejecutarRonda();
        comprobar(origen.getSize() == 6, "Lote de la mitad del origen");
        comprobar(ids(simulacion, 0).equals("P4"), "Mostrador 1 atiende P1 y conserva P4");
        comprobar(ids(simulacion, 1).equals("P2 P5"), "Rotación al mostrador 2");
        comprobar(ids(simulacion, 2).equals("P3 P6"), "Rotación al mostrador 3");
        simulacion.ejecutarRonda();
        comprobar(ids(simulacion, 0).equals("P7"), "Nuevo lote reinicia en mostrador 1; atención FIFO");
        comprobar(ids(simulacion, 1).equals("P2 P5 P8"), "Se agrega al final de la fila");
        comprobar(ids(simulacion, 2).equals("P3 P6 P9"), "Se preservan pasajeros anteriores");
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
            SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, extremo,
                    new PrintStream(new ByteArrayOutputStream()));
            simulacion.ejecutarRonda();
            comprobar(origen.getSize() == (maximo ? 5 : 8), "Lote inclusivo entre 1 y mitad entera");
        }
    }

    private static void probarSimulaciones() {
        for (int cantidad : new int[] {0, 1, 2, 3, 12, 50}) {
            for (int semilla = 0; semilla < 20; semilla++) {
                ListaEnlazada<Pasajero> origen = pasajeros(cantidad);
                ByteArrayOutputStream registro = new ByteArrayOutputStream();
                SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen,
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
                    for (NodoProceso<String, Pasajero> mostrador = simulacion.getMostradores().getPrimero();
                         mostrador != null; mostrador = mostrador.getSig()) {
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
                registro.reset();
                simulacion.ejecutarRonda();
                comprobar(registro.size() == 0, "Ronda terminada no hace cambios");
            }
        }
    }
}

package org.example.proyectoestructura;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
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
        probarRangoAleatorio();
        probarOperacionesListaProcesos();
        escenario1TresMostradoresVacios();
        escenario2FilaMasCortaYDesempate();
        escenario3AtenderVariosDeUnMostrador();
        escenario4CerrarMostradoresVacios();
        escenario5CerrarMostradorConPasajeros();
        escenario6NoCerrarUnicoMostrador();
        escenario7MaletasASusVuelos();
        escenario8ProcesarMaletasDeUnVuelo();
        escenario9ProcesoDeMaletasVacio();
        escenario10EliminarVueloSoloVacio();
        escenario11EstadoDeLPyLM();
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
            lista.agregarUltimo(new Pasajero("P" + i, "Nombre", "V1", 0));
        }
        return lista;
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
            simulacion.distribuirPasajeros();
            comprobar(origen.getSize() == (maximo ? 5 : 8), "Lote inclusivo entre 1 y mitad entera");
        }
    }


    private static Random maximo() {
        return new Random(0) {
            @Override
            public int nextInt(int limite) {
                return limite - 1;
            }
        };
    }

    private static SimulacionAeropuerto simulacion(ListaEnlazada<Pasajero> origen, ListaEnlazada<Maleta> maletas,
                                                   ByteArrayOutputStream registro) {
        return new SimulacionAeropuerto(origen, maletas, new Random(1), new PrintStream(registro));
    }

    private static String salto() {
        return System.lineSeparator();
    }

    private static void probarOperacionesListaProcesos() {
        ListaProcesos<String, Integer> procesos = new ListaProcesos<>();
        comprobar(procesos.obtenerProcesoMasCorto() == null, "Sin procesos no hay más corto");
        comprobar(procesos.totalElementos() == 0 && procesos.totalDeProcesos() == 0, "Totales en cero");
        comprobar(procesos.agregarProceso("A") && procesos.agregarProceso("B"), "Agregar procesos");
        comprobar(!procesos.agregarProceso("A"), "No se repite un proceso");
        comprobar(procesos.agregar("A", 1) && procesos.agregar("A", 2) && procesos.agregar("B", 3),
                "Agregar elementos");
        comprobar(!procesos.agregar("Z", 9), "No agrega a un proceso inexistente");
        comprobar(procesos.revisarPrimero("A") == 1 && procesos.tamanoProceso("A") == 2,
                "Revisar primero no retira");
        comprobar(procesos.obtenerProcesoMasCorto().equals("B"), "Proceso más corto");
        comprobar(procesos.obtenerProcesoMasCorto("B").equals("A"), "Más corto excluyendo uno");
        comprobar(procesos.totalElementos() == 3 && procesos.totalDeProcesos() == 2, "Totales");
        comprobar(procesos.eliminar("A") == 1 && procesos.eliminar("A") == 2, "Eliminar en orden FIFO");
        comprobar(procesos.tamanoProceso("Z") == -1, "Tamaño de proceso inexistente");
        comprobar(procesos.encontrarProceso("B") == procesos.getUltimo(), "Encontrar proceso");
    }

    private static void escenario1TresMostradoresVacios() {
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), new ByteArrayOutputStream());
        ListaProcesos<String, Pasajero> lp = simulacion.getMostradores();
        comprobar(lp.totalDeProcesos() == 3 && lp.estanVaciasLA(), "Tres mostradores vacíos");
        comprobar(lp.toString().equals("Mostrador 1: []" + salto() + "Mostrador 2: []" + salto()
                + "Mostrador 3: []"), "Recorrido en orden");
        comprobar(lp.getValorXId(0).equals("Mostrador 1") && lp.getValorXId(2).equals("Mostrador 3"),
                "Posiciones de los mostradores");
    }

    private static void escenario2FilaMasCortaYDesempate() {
        ListaEnlazada<Pasajero> origen = pasajeros(6);
        SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, new ListaEnlazada<>(), maximo(),
                new PrintStream(new ByteArrayOutputStream()));
        simulacion.distribuirPasajeros();
        ListaProcesos<String, Pasajero> lp = simulacion.getMostradores();
        comprobar(lp.revisarPrimero("Mostrador 1").getId().equals("P1")
                && lp.revisarPrimero("Mostrador 2").getId().equals("P2")
                && lp.revisarPrimero("Mostrador 3").getId().equals("P3"), "Filas iguales: primer mostrador");
        lp.eliminar("Mostrador 2");
        simulacion.distribuirPasajeros();
        comprobar(lp.tamanoProceso("Mostrador 2") == 1 && lp.revisarPrimero("Mostrador 2").getId().equals("P4"),
                "Va a la fila más corta");
        simulacion.distribuirPasajeros();
        comprobar(lp.tamanoProceso("Mostrador 1") == 2, "Empate entre todos: primer mostrador encontrado");
    }

    private static void escenario3AtenderVariosDeUnMostrador() {
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), registro);
        ListaProcesos<String, Pasajero> lp = simulacion.getMostradores();
        ListaEnlazada<Pasajero> fila = pasajeros(3);
        for (NodoLista<Pasajero> nodo = fila.getPrimero(); nodo != null; nodo = nodo.getSig()) {
            lp.agregar("Mostrador 2", nodo.getValor());
        }
        simulacion.atenderPasajero();
        simulacion.atenderPasajero();
        comprobar(lp.revisarPrimero("Mostrador 2").getId().equals("P3"), "Se atiende en orden de llegada");
        simulacion.atenderPasajero();
        simulacion.atenderPasajero();
        String texto = registro.toString();
        comprobar(texto.indexOf("Atendido P1") < texto.indexOf("Atendido P2")
                && texto.indexOf("Atendido P2") < texto.indexOf("Atendido P3"), "Orden FIFO");
        comprobar(simulacion.getAtendidos() == 3 && lp.estanVaciasLA(), "Atender sin pasajeros no falla");
    }

    private static void escenario4CerrarMostradoresVacios() {
        ListaProcesos<String, Pasajero> lp = new ListaProcesos<>();
        for (int i = 1; i <= 5; i++) {
            lp.agregarProceso("M" + i);
        }
        comprobar(lp.removerProceso("M1"), "Cerrar al inicio");
        comprobar(lp.getPrimero().getValor().equals("M2") && lp.getUltimo().getSig() == lp.getPrimero(),
                "Nuevo primero y circular");
        comprobar(lp.removerProceso("M3"), "Cerrar en el medio");
        comprobar(lp.removerProceso("M5"), "Cerrar al final");
        comprobar(lp.getUltimo().getValor().equals("M4") && lp.getUltimo().getSig() == lp.getPrimero(),
                "Nuevo último y circular");
        comprobar(lp.toString().equals("M2: []" + salto() + "M4: []"), "Quedan M2 y M4");
        comprobar(!lp.removerProceso("M9"), "Cerrar inexistente");
        lp.removerProceso("M2");
        lp.removerProceso("M4");
        comprobar(lp.estaVacio() && lp.getUltimo() == null, "Se cierran todos");
    }

    private static void escenario5CerrarMostradorConPasajeros() {
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), registro);
        ListaProcesos<String, Pasajero> lp = simulacion.getMostradores();
        ListaEnlazada<Pasajero> lista = pasajeros(4);
        lp.agregar("Mostrador 1", lista.eliminarPrimero().getValor());
        lp.agregar("Mostrador 1", lista.eliminarPrimero().getValor());
        lp.agregar("Mostrador 1", lista.eliminarPrimero().getValor());
        lp.agregar("Mostrador 2", lista.eliminarPrimero().getValor());
        comprobar(simulacion.cerrarMostrador("Mostrador 1"), "Se cierra con pasajeros");
        comprobar(lp.encontrarProceso("Mostrador 1") == null && lp.totalDeProcesos() == 2, "Nodo eliminado");
        comprobar(lp.totalElementos() == 4, "No se pierden pasajeros");
        comprobar(lp.toString().equals("Mostrador 2: [P4, P2]" + salto() + "Mostrador 3: [P1, P3]"),
                "Redistribución uno por uno a la fila más corta");
        String texto = registro.toString();
        comprobar(texto.indexOf("Reasignado P1") < texto.indexOf("Reasignado P2")
                && texto.indexOf("Reasignado P2") < texto.indexOf("Reasignado P3"), "Salen en su orden");
    }

    private static void escenario6NoCerrarUnicoMostrador() {
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), registro);
        ListaProcesos<String, Pasajero> lp = simulacion.getMostradores();
        simulacion.cerrarMostrador("Mostrador 2");
        simulacion.cerrarMostrador("Mostrador 3");
        lp.agregar("Mostrador 1", new Pasajero("P1", "Nombre", "V1", 0));
        comprobar(!simulacion.cerrarMostrador("Mostrador 1"), "Se rechaza cerrar el único mostrador");
        comprobar(lp.totalDeProcesos() == 1 && lp.tamanoProceso("Mostrador 1") == 1, "El mostrador sigue abierto");
        comprobar(registro.toString().contains("No se puede cerrar"), "Mensaje de rechazo");
    }

    private static void escenario7MaletasASusVuelos() {
        ListaEnlazada<Maleta> maletas = new ListaEnlazada<>();
        maletas.agregarUltimo(new Maleta("A", 10.0, "P1", "V1"));
        maletas.agregarUltimo(new Maleta("B", 12.0, "P2", "V1"));
        maletas.agregarUltimo(new Maleta("C", 15.0, "P1", "V2"));
        maletas.agregarUltimo(new Maleta("D", 8.0, "P1", "V1"));
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), maletas, new ByteArrayOutputStream());
        simulacion.getMostradores().agregar("Mostrador 1", new Pasajero("P1", "Juan", "V1", 3));
        simulacion.atenderPasajero();
        ListaProcesos<String, Maleta> lm = simulacion.getVuelos();
        comprobar(lm.toString().equals("V1: [A, D]" + salto() + "V2: [C]"), "Cada maleta a su vuelo");
        comprobar(maletas.getSize() == 1 && maletas.getPrimero().getValor().getId().equals("B"),
                "Solo salen las maletas del pasajero atendido");
    }

    private static void escenario8ProcesarMaletasDeUnVuelo() {
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), new ByteArrayOutputStream());
        ListaProcesos<String, Maleta> lm = simulacion.getVuelos();
        lm.agregarProceso("V1");
        lm.agregar("V1", new Maleta("A", 10.0, "P1", "V1"));
        lm.agregar("V1", new Maleta("B", 10.0, "P1", "V1"));
        comprobar(simulacion.procesarMaleta("V1").getId().equals("A"), "Se procesa la primera");
        comprobar(lm.tamanoProceso("V1") == 1, "Queda una maleta");
        comprobar(simulacion.procesarMaleta("V1").getId().equals("B"), "Se procesa la segunda");
        comprobar(simulacion.getMaletasProcesadas() == 2, "Contador de maletas procesadas");
    }

    private static void escenario9ProcesoDeMaletasVacio() {
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), registro);
        ListaProcesos<String, Maleta> lm = simulacion.getVuelos();
        lm.agregarProceso("V1");
        comprobar(lm.eliminar("V1") == null && lm.revisarPrimero("V1") == null, "Proceso vacío devuelve null");
        comprobar(simulacion.procesarMaleta("V1") == null, "Procesar vuelo vacío sin error");
        comprobar(simulacion.procesarMaleta("NO-EXISTE") == null, "Procesar vuelo inexistente sin error");
        simulacion.procesarMaleta();
        comprobar(registro.toString().contains("No hay maletas por procesar."), "LM vacía sin error");
    }

    private static void escenario10EliminarVueloSoloVacio() {
        SimulacionAeropuerto simulacion = simulacion(pasajeros(0), new ListaEnlazada<>(), new ByteArrayOutputStream());
        ListaProcesos<String, Maleta> lm = simulacion.getVuelos();
        lm.agregarProceso("V1");
        lm.agregarProceso("V2");
        lm.agregar("V1", new Maleta("A", 10.0, "P1", "V1"));
        lm.agregar("V1", new Maleta("B", 10.0, "P1", "V1"));
        comprobar(!lm.removerProceso("V1"), "No se elimina un vuelo con maletas");
        simulacion.procesarMaleta("V1");
        comprobar(lm.encontrarProceso("V1") != null, "Con una maleta sigue existiendo");
        simulacion.procesarMaleta("V1");
        comprobar(lm.encontrarProceso("V1") == null && lm.totalDeProcesos() == 1, "Vacío se elimina");
    }

    private static void escenario11EstadoDeLPyLM() {
        ListaEnlazada<Pasajero> origen = new ListaEnlazada<>();
        origen.agregarUltimo(new Pasajero("P1", "Juan", "GYE-UIO", 2));
        origen.agregarUltimo(new Pasajero("P2", "Pepe", "GYE-LIM", 1));
        ListaEnlazada<Maleta> maletas = SimulacionAeropuerto.crearMaletas(origen, new Random(3));
        comprobar(maletas.getSize() == 3, "Se crean las maletas según cada pasajero");
        ByteArrayOutputStream registro = new ByteArrayOutputStream();
        SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, maletas, maximo(),
                new PrintStream(registro));
        simulacion.distribuirPasajeros();
        simulacion.atenderPasajero();
        registro.reset();
        simulacion.imprimirEstado();
        String texto = registro.toString();
        comprobar(texto.contains("CHECK-IN") && texto.contains("Mostrador 1: [P1]")
                && texto.contains("BAGGAGE") && texto.contains("GYE-LIM: [M003]"), "LP y LM al mismo tiempo");
        comprobar(simulacion.getMostradores().getClass() == simulacion.getVuelos().getClass(),
                "La misma clase genérica con tipos distintos");
    }

    private static void probarSimulaciones() {
        for (int cantidad : new int[] {0, 1, 2, 3, 12, 50}) {
            for (int semilla = 0; semilla < 20; semilla++) {
                ListaEnlazada<Pasajero> origen = new ListaEnlazada<>();
                int totalMaletas = 0;
                for (int i = 1; i <= cantidad; i++) {
                    origen.agregarUltimo(new Pasajero("P" + i, "Nombre", "V" + i % 4, i % 3));
                    totalMaletas = totalMaletas + i % 3;
                }
                Random aleatorio = new Random(semilla);
                ListaEnlazada<Maleta> maletas = SimulacionAeropuerto.crearMaletas(origen, aleatorio);
                SimulacionAeropuerto simulacion = new SimulacionAeropuerto(origen, maletas, aleatorio,
                        new PrintStream(new ByteArrayOutputStream()));
                int rondas = 0;
                while (!simulacion.haTerminado()) {
                    rondas++;
                    comprobar(rondas <= cantidad, "Un pasajero atendido por ciclo");
                    simulacion.ejecutarRonda();
                    comprobar(origen.getSize() + simulacion.getMostradores().totalElementos()
                            + simulacion.getAtendidos() == cantidad, "No se pierden pasajeros");
                }
                comprobar(simulacion.getAtendidos() == cantidad, "Todos atendidos");
                comprobar(maletas.estaVacio() && simulacion.getVuelos().totalElementos() == totalMaletas,
                        "Todas las maletas llegan a LM");
                simulacion.procesarTodasLasMaletas();
                comprobar(simulacion.equipajeTerminado() && simulacion.getMaletasProcesadas() == totalMaletas,
                        "Todas las maletas se procesan");
            }
        }
    }
}

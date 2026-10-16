# ProyectoEstructura

Proyecto Java independiente para ejercicios de estructuras de datos y una simulación de check-in de aeropuerto. Requiere JDK 21 o superior; Maven es opcional. No utiliza bibliotecas externas.

## Organización

- `src/main/java/org/example/proyectoestructura/Main.java`: consultas originales, datos de ejemplo e inicio de la simulación.
- `estructura/ListaArreglo.java`: lista de capacidad fija usada por las consultas originales.
- `estructura/NodoLista.java` y `ListaEnlazada.java`: lista enlazada simple para las filas de pasajeros.
- `estructura/NodoProceso.java` y `ListaProcesos.java`: lista enlazada de procesos, cada uno con su propia lista enlazada interna.
- `modelo/`: `Pasajero` y `Maleta`, vinculados mediante `Maleta.idPasajero`.
- `simulacion/SimulacionAeropuerto.java`: reparto y atención de pasajeros.
- `src/test/java/`: pruebas ejecutables sin dependencias externas.

Los paquetes de la lista anterior están dentro de `src/main/java/org/example/proyectoestructura/`.

## Compilar y ejecutar

Desde la raíz, con Maven:

```bash
mvn package
java -cp target/classes org.example.proyectoestructura.Main
```

O directamente con el JDK:

```bash
mkdir -p target/classes
javac -encoding UTF-8 --release 21 -d target/classes $(find src/main/java -name '*.java')
java -cp target/classes org.example.proyectoestructura.Main
```

Se puede pasar una semilla para repetir la misma simulación:

```bash
java -cp target/classes org.example.proyectoestructura.Main 42
```

También puedes abrir esta carpeta como proyecto Maven en IntelliJ IDEA.

## Comportamiento

Primero se conservan las consultas originales sobre tres pasajeros y cinco maletas: se muestra `M011`, cuyo código numérico es mayor que 10, y los pasajeros `P001` y `P002`, que tienen más maletas que `P003`. La cantidad de maletas se calcula a partir de sus registros, sin duplicarla en `Pasajero`.

Después se simula el check-in con doce pasajeros y tres mostradores:

1. Si quedan más de dos pasajeros en origen, se elige un lote entre 1 y la mitad entera de los restantes, ambos límites incluidos. Con uno o dos, se trasladan todos.
2. Se extraen desde la cabeza y se agregan al final de las filas en orden 1 → 2 → 3 → 1. Cada lote empieza en el mostrador 1, según el resumen de clase.
3. Se elige aleatoriamente, con igual probabilidad, uno de los mostradores ocupados y se atiende al primero de su fila. Esta es la mejora recomendada para evitar turnos vacíos.
4. Se continúa hasta vaciar tanto el origen como todas las filas, mostrando las asignaciones, las atenciones y el estado de los mostradores.

`ListaProcesos.getNodoXId(pos)` usa posiciones desde cero y rechaza posiciones inválidas. `ListaEnlazada.eliminarPrimero()` devuelve el nodo retirado, desconectado, o `null` si está vacía. Los mostradores permanecen creados al terminar: `estanVaciasLA()` comprueba sus filas, no la existencia de mostradores.

Los datos siguen definidos en código, sin persistencia. El procesamiento posterior de equipaje queda pendiente de la segunda parte de clase. `ListaArreglo` conserva su capacidad fija y descarta inserciones al llenarse; las nuevas listas enlazadas no tienen ese límite.

## Pruebas

Después de compilar las clases principales, compila y ejecuta las pruebas:

```bash
mkdir -p target/test-classes
javac -encoding UTF-8 --release 21 -cp target/classes -d target/test-classes $(find src/test/java -name '*.java')
java -cp target/classes:target/test-classes org.example.proyectoestructura.PruebasAeropuerto
```

Las pruebas lanzan `AssertionError` ante un fallo. Verifican listas vacías y reutilización, posiciones inválidas, filas independientes, rotación, atención FIFO, límites de lotes y conservación de pasajeros. También prueban 120 simulaciones con diferentes tamaños y semillas para comprobar atención única y finalización. No hay framework de pruebas configurado: `mvn test` por sí solo no ejecuta este programa de pruebas.

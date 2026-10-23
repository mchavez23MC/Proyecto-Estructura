# ProyectoEstructura

Proyecto Java independiente para ejercicios de estructuras de datos y una simulación de check-in de aeropuerto. Requiere JDK 21 o superior; Maven es opcional. No utiliza bibliotecas externas.

## Organización

- `src/main/java/org/example/proyectoestructura/Main.java`: consultas originales, datos de ejemplo e inicio de la simulación.
- `estructura/ListaArreglo.java`: lista de capacidad fija usada por las consultas originales.
- `estructura/NodoLista.java` y `ListaEnlazada.java`: lista doblemente enlazada para las filas de pasajeros.
- `estructura/NodoProceso.java` y `ListaProcesos.java`: lista circular de procesos, cada uno con su propia lista enlazada interna.
- `modelo/`: `Pasajero` y `Maleta`, vinculados mediante `Maleta.idPasajero`. Cada `Maleta` tiene además su `vuelo` de destino.
- `simulacion/SimulacionAeropuerto.java`: reparto y atención de pasajeros, y paso de sus maletas a los vuelos.
- `src/test/java/`: pruebas ejecutables sin dependencias externas.

Los paquetes de la lista anterior están dentro de `src/main/java/org/example/proyectoestructura/`.

## Contexto de clase y consigna

[Resumen-videos.md](Resumen-videos.md) reúne el contexto de los cuatro videos de clase. La consigna de seleccionar pasajeros, repartirlos y atenderlos corresponde al mismo algoritmo; está implementada en `SimulacionAeropuerto`.

| Paso de la consigna | Implementación |
|---|---|
| Elegir una cantidad aleatoria `np`. | `distribuirPasajeros()` calcula el lote y controla el caso de uno o dos pasajeros. |
| Sacar `np` pasajeros de `LPasajeros`. | Se extraen de la cabeza de la lista `pendientes`. |
| Colocarlos en el mostrador con la fila más corta (avance 4). | `buscarFilaMasCorta()` recorre `mostradores`; si hay empate gana el primero encontrado. |
| Elegir el mostrador `nm`. | `atenderPasajero()` elige aleatoriamente entre los mostradores ocupados. |
| Atender al pasajero sacándolo de `LP`. | Se extrae al primero de la fila interna del mostrador elegido. |
| Pasar sus maletas de `LMaletas` a `LM` (avance 4). | `moverMaletas()` saca de `maletas` todas las del pasajero y las agrega al vuelo en `vuelos`; si el vuelo no existe, lo crea. |

En el vocabulario de clase, `LP` es la lista de procesos que contiene los mostradores; atender elimina un pasajero de una fila interna, sin eliminar el mostrador. La versión básica permite elegir un mostrador vacío y controla la extracción `null`. El proyecto aplica la mejora recomendada en los videos: seleccionar solamente mostradores ocupados.

El ejemplo `EjercicioMostradores.java` compartido en la conversación ilustra la versión básica; no está incorporado al repositorio. La aplicación se ejecuta mediante `Main` y utiliza la simulación modularizada.

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
2. Se extraen desde la cabeza y cada pasajero va al final del mostrador con la fila más corta. Si hay empate, va al primer mostrador encontrado.
3. Se elige aleatoriamente, con igual probabilidad, uno de los mostradores ocupados y se atiende al primero de su fila. Esta es la mejora recomendada para evitar turnos vacíos.
4. Al atender a un pasajero se recorre `LMaletas`, se sacan todas sus maletas con `eliminarNodo()` y se colocan en `LM`: con `buscar()` se ve si su vuelo ya existe; si no existe, se crea un nuevo `NodoProceso` con el nombre del vuelo.
5. Se continúa hasta vaciar tanto el origen como todas las filas, mostrando las asignaciones, las atenciones, el paso de maletas y el estado de mostradores, `LMaletas` y `LM`.

`ListaEnlazada` es doblemente enlazada: cada `NodoLista` conoce su siguiente (`getSig()`) y su anterior (`getAnt()`). Permite agregar y eliminar por ambos extremos (`agregarPrimero`, `agregarUltimo`, `eliminarPrimero`, `eliminarUltimo`) y recorrerla al revés con `toStringInverso()`. `eliminarPrimero()` y `eliminarUltimo()` devuelven el nodo retirado, desconectado, o `null` si está vacía. `eliminarNodo(nodo)` saca un nodo de cualquier posición usando sus enlaces `ant` y `sig`.

`ListaProcesos` es circular: el último mostrador apunta al primero y un único `NodoProceso` apunta a sí mismo. Los recorridos completos terminan al regresar a `getPrimero()`. `buscar(valor)` recorre una vuelta completa y devuelve el proceso con ese valor o `null`. `getNodoXId(pos)` usa posiciones desde cero y rechaza posiciones inválidas. Los mostradores permanecen creados al terminar: `estanVaciasLA()` comprueba sus filas, no la existencia de mostradores.

Los datos siguen definidos en código, sin persistencia. `ListaArreglo` conserva su capacidad fija y descarta inserciones al llenarse; las listas enlazadas no tienen ese límite.

## Pruebas

Después de compilar las clases principales, compila y ejecuta las pruebas:

```bash
mkdir -p target/test-classes
javac -encoding UTF-8 --release 21 -cp target/classes -d target/test-classes $(find src/test/java -name '*.java')
java -cp target/classes:target/test-classes org.example.proyectoestructura.PruebasAeropuerto
```

Las pruebas lanzan `AssertionError` ante un fallo. Verifican listas vacías y reutilización, enlaces dobles y recorrido inverso, circularidad de los mostradores, posiciones inválidas, filas independientes, eliminación de nodos intermedios, búsqueda de procesos, reparto a la fila más corta, atención FIFO, límites de lotes, paso de maletas a vuelos y conservación de pasajeros y maletas. También prueban 120 simulaciones con diferentes tamaños y semillas para comprobar atención única y finalización. No hay framework de pruebas configurado: `mvn test` por sí solo no ejecuta este programa de pruebas.

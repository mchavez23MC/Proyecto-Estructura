# Airport Flow Manager

Proyecto 1 de Estructuras de Datos. Simulación en consola del check-in de pasajeros y del procesamiento de equipaje usando la misma estructura genérica `ListaProcesos`.

Requiere JDK 21 o superior. No usa librerías externas.

## Archivos

Todo el código está en `src/main/java/org/example/proyectoestructura/`:

- `estructura/`: `NodoLista`, `ListaEnlazada` (doblemente enlazada), `NodoProceso` y `ListaProcesos` (circular, cada nodo tiene su propia lista).
- `modelo/`: `Pasajero` y `Maleta`.
- `simulacion/SimulacionAeropuerto.java`: reparto, atención, cierre de mostradores y procesamiento de maletas.
- `Main.java`: datos de los pasajeros y menú.

Las pruebas están en `src/test/java/org/example/proyectoestructura/PruebasAeropuerto.java`.

`ejercicios/`, `Pila`, `Cola` y `ListaArreglo` son ejercicios de clase y no forman parte de la simulación.

## Compilar y ejecutar

Desde la raíz del repositorio, con Maven:

```
mvn package
java -cp target/classes org.example.proyectoestructura.Main
```

O directamente con el JDK:

```
javac -encoding UTF-8 -d target/classes $(find src/main/java -name "*.java")
java -cp target/classes org.example.proyectoestructura.Main
```

Se puede pasar una semilla para repetir la misma simulación:

```
java -cp target/classes org.example.proyectoestructura.Main 42
```

## Ejecutar las pruebas

```
javac -encoding UTF-8 -cp target/classes -d target/test-classes $(find src/test/java -name "*.java")
java -cp target/classes:target/test-classes org.example.proyectoestructura.PruebasAeropuerto
```

En Windows se usa `;` en lugar de `:` dentro de `-cp`. Si todo está bien se imprime `Todas las pruebas pasaron.`

## Funcionamiento

- `LPasajeros` es la lista de pasajeros por atender y `LMaletas` la lista con todas sus maletas.
- `LP` es `ListaProcesos<String, Pasajero>`: cada nodo es un mostrador con su fila.
- `LM` es `ListaProcesos<String, Maleta>`: cada nodo es un vuelo con sus maletas.

En cada ciclo de check-in:

1. Llega un número aleatorio de pasajeros (entre 1 y la mitad de los que quedan) y cada uno va al mostrador con la fila más corta; si hay empate, al primero.
2. Se atiende al primer pasajero de un mostrador aleatorio que tenga gente. Sus maletas salen de `LMaletas` y entran a `LM` en el vuelo correspondiente; si el vuelo no existe se crea.
3. Se decide al azar si se cierra un mostrador. Sus pasajeros se reasignan en orden, uno por uno, a la fila más corta y luego se elimina el nodo. No se puede cerrar el único mostrador abierto.

Esto se repite hasta que no queden pasajeros en `LPasajeros` ni en los mostradores. Después se elige un vuelo al azar y se procesa su siguiente maleta; cuando un vuelo queda vacío se elimina de `LM`. Termina cuando no quedan maletas.

## Menú

1. Ejecutar un ciclo de check-in
2. Ejecutar todo el check-in
3. Cerrar un mostrador
4. Procesar una maleta
5. Procesar todas las maletas
6. Ver estado de LP y LM
7. Ejecutar la simulación completa
0. Salir

## Operaciones de ListaProcesos

| Operación | Método |
|---|---|
| Agregar proceso | `agregarProceso(id)` |
| Remover proceso (solo si está vacío) | `removerProceso(id)` |
| Agregar elemento al final | `agregar(id, elemento)` |
| Sacar el primer elemento | `eliminar(id)` |
| Ver el primer elemento | `revisarPrimero(id)` |
| Buscar proceso | `encontrarProceso(id)` |
| Tamaño de un proceso | `tamanoProceso(id)` |
| Proceso más corto | `obtenerProcesoMasCorto()` |
| Cantidad de procesos | `totalDeProcesos()` |
| Total de elementos | `totalElementos()` |
| Todas las filas vacías | `estanVaciasLA()` |

# Airport Flow Manager

Proyecto 1 - Estructuras de Datos

## Compilar y ejecutar

Con Maven:

```
mvn package
java -cp target/classes org.example.proyectoestructura.Main
```

## Pruebas

```
javac -encoding UTF-8 -cp target/classes -d target/test-classes src/test/java/org/example/proyectoestructura/PruebasAeropuerto.java
java -cp "target/classes;target/test-classes" org.example.proyectoestructura.PruebasAeropuerto
```

## Carpetas

En `src/main/java/org/example/proyectoestructura`:

- `estructura`: NodoLista, ListaEnlazada, NodoProceso y ListaProcesos
- `modelo`: Pasajero y Maleta
- `simulacion`: SimulacionAeropuerto
- `Main`: menú
- `ejercicios`: ejercicios de clase

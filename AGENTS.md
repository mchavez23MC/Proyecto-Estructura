# Repository Guidelines

## Project Structure & Module Organization

This Java 21 airport project keeps sources under `src/main/java/org/example/proyectoestructura/`:

- `Main.java`: original luggage queries, sample data, and simulation startup.
- `estructura/`: fixed-capacity `ListaArreglo`, simple `ListaEnlazada`/`NodoLista`, and generic `ListaProcesos`/`NodoProceso` with independent internal lists.
- `modelo/`: `Pasajero` and `Maleta`, linked by `idPasajero`.
- `simulacion/`: `SimulacionAeropuerto`, which distributes and serves passengers.

Executable tests live under `src/test/java`. Keep generated output in `target/`. `README.md` describes usage; `Resumen-videos.md` records the classroom context. Subsequent luggage processing remains pending.

## Build, Test, and Development Commands

Use JDK 21+, UTF-8, and the repository root. Maven is optional; no external libraries are required.

- `mvn package`: compile sources and package the application.
- `java -cp target/classes org.example.proyectoestructura.Main 42`: run queries and simulation with a reproducible seed; omit `42` for random runs.

Without Maven, compile sources, then compile and run the standalone tests:

```bash
mkdir -p target/classes target/test-classes
javac -encoding UTF-8 --release 21 -d target/classes $(find src/main/java -name '*.java')
javac -encoding UTF-8 --release 21 -cp target/classes -d target/test-classes $(find src/test/java -name '*.java')
java -cp target/classes:target/test-classes org.example.proyectoestructura.PruebasAeropuerto
```

`mvn test` alone does not execute `PruebasAeropuerto`; no testing framework or coverage threshold is configured.

## Coding Style & Naming Conventions

Use four spaces, opening braces on declaration lines, `PascalCase` classes, `camelCase` members, and lowercase packages. Preserve Spanish domain names. Keep collections in `estructura`, models in `modelo`, and simulation behavior in `simulacion`. No formatter or linter is configured.

## Simulation & Testing Guidelines

Preserve three independent FIFO queues. Each batch restarts distribution at counter 1. Move all remaining passengers when there are one or two; otherwise choose between 1 and half the remaining count, inclusive. Serve one randomly selected occupied counter after distribution. Finish only when the source and all queues are empty.

Extend the standalone tests for behavior changes: empty lists, index bounds, rotation, FIFO, unique service, and passenger conservation. Existing tests include 120 seeded simulations. Original queries should still select `M011`, `P001`, and `P002`. Derive luggage counts from luggage records. Document deliberate changes to `ListaArreglo` capacity behavior.

## Commit & Pull Request Guidelines

No Git metadata is available to verify historical conventions. Use imperative messages such as `Fix passenger distribution`. Describe behavior changes, related issues, validation commands and results; include console output for simulation changes.

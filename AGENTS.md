# Repository Guidelines

## Project Structure & Module Organization

This standalone Java 21 project demonstrates data structures using passengers and luggage. Sources live under `src/main/java/org/example/proyectoestructura/`:

- `Main.java`: sample data, comparators, queries, and console output.
- `estructura/ListaArreglo.java`: generic, fixed-capacity array list.
- `modelo/`: passenger (`Pasajero`) and luggage (`Maleta`) models.

`pom.xml` defines the Maven project and UTF-8 encoding. `README.md` documents execution and current behavior. There are no test sources or application assets. Keep generated classes and packaged output in `target/`.

## Build, Test, and Development Commands

Use JDK 21 or newer. Run commands from the repository root:

- `mvn package`: compile and package the project; also runs tests when configured.
- `java -cp target/classes org.example.proyectoestructura.Main`: run the console example after compilation.
- `mvn test`: invoke Maven's test phase; currently there are no automated tests.

Maven is optional. Compile directly with:

```bash
mkdir -p target/classes
javac -encoding UTF-8 --release 21 -d target/classes $(find src/main/java -name '*.java')
```

## Coding Style & Naming Conventions

Use four-space indentation, opening braces on the declaration line, and UTF-8 source files. Use `PascalCase` for classes, `camelCase` for methods and fields, and lowercase package names. Follow existing Spanish domain names such as `idPasajero` and `buscarMayores`. Keep model classes in `modelo` and reusable collections in `estructura`. No formatter or linter is configured; keep edits consistent with surrounding code.

## Testing Guidelines

No testing framework or coverage threshold is configured. For changes, compile and run `Main`; the sample should select luggage `M011` and passengers `P001` and `P002`. When introducing automated tests, place them under `src/test/java` with matching packages and names such as `ListaArregloTest`, and configure the framework in `pom.xml`. Cover empty lists, capacity limits, index bounds, insertion/removal order, and comparator filtering.

## Commit & Pull Request Guidelines

This checkout has no Git metadata, so historical commit conventions cannot be verified. Use concise, imperative messages such as `Fix passenger luggage counting`. Keep changes focused. Pull requests should explain the behavior change, list validation commands and results, and link related issues when available. Include console output for query changes. Document intentional changes to fixed-capacity behavior, including the current silent rejection of insertions when full.

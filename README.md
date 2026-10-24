# Lights Off

A Java Swing implementation of the Lights Out puzzle with 3×3, 4×4 and 5×5
boards. Every generated board is solvable. An optional hint system uses Gaussian
elimination over GF(2) to calculate a valid set of moves.

## Requirements

- JDK 8 or newer
- Maven 3.6 or newer

## Build and test

```bash
mvn test
mvn package
```

## Run

```bash
java -jar target/lights-off-1.0.0-SNAPSHOT.jar
```

## Structure

- `LightsOutBoard` contains game state and move rules without Swing dependencies.
- `BoardUtils` generates solvable boards and contains the GF(2) solver.
- `GamePanel` renders any supported board size.
- `Game` creates the application screens and navigation.

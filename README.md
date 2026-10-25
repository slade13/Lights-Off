# Lights Off

**Version 2.0**

A JavaFX implementation of the Lights Out puzzle with 3×3, 4×4 and 5×5
boards. Every generated board is solvable. An optional hint system uses Gaussian
elimination over GF(2) to calculate a valid set of moves.

## Requirements

- JDK 17 or newer
- Maven 3.6 or newer

## Build and test

```bash
mvn test
mvn package
```

## Run with JavaFX

```bash
mvn javafx:run
```

## Structure

- `LightsOutBoard` contains game state and move rules without Swing dependencies.
- `BoardUtils` generates solvable boards and contains the GF(2) solver.
- `GamePanel` renders every supported board size with JavaFX controls.
- `BoardView` is the shared board renderer used by gameplay and the live tutorial.
- `Game` is the IntelliJ-safe entry point and creates the JavaFX scenes and navigation.
- `game.css` contains the visual theme without mixing styles into Java code.
- `module-info.java` runs JavaFX on the module path instead of the unsupported classpath mode.

package pl.realcode.lightsoff;

import javafx.application.Application;

/** Plain Java entry point that also works from a packaged JAR. */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        Application.launch(Game.class, args);
    }
}

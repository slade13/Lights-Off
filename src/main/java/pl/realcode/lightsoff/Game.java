package pl.realcode.lightsoff;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class Game {

    private Game() {
    }

    public static void main(String[] args) {
        Application.launch(FxApplication.class, args);
    }

    public static final class FxApplication extends Application {
    private static final double WINDOW_WIDTH = 520;
    private static final double WINDOW_HEIGHT = 620;

    private Stage stage;
    private Scene scene;

        @Override
        public void start(Stage primaryStage) {
        stage = primaryStage;
        scene = new Scene(createMainMenu(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(requireResource("/styles/game.css"));
        stage.setTitle("Lights Off — Main menu");
        stage.setScene(scene);
        stage.setMinWidth(420);
        stage.setMinHeight(520);
        stage.show();
        }

    private VBox createMainMenu() {
        Label title = new Label("LIGHTS OFF");
        title.getStyleClass().add("app-title");
        Label subtitle = new Label("Turn every light off");
        subtitle.getStyleClass().add("subtitle");

        VBox menu = new VBox(14, title, subtitle, spacer(18),
                navigationButton("Start game", this::showDifficulty),
                navigationButton("Tutorial", this::showTutorial),
                navigationButton("About game", () -> showTextScreen("About game", gameDescription())),
                navigationButton("Author", () -> showTextScreen("Author", authorDescription())));
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(45));
        menu.getStyleClass().addAll("screen", "dark-screen");
        return menu;
    }

    private void showDifficulty() {
        Label title = new Label("Choose difficulty");
        title.getStyleClass().add("screen-title");
        ToggleGroup difficulties = new ToggleGroup();
        RadioButton easy = difficultyOption("Easy — 3×3", 3, difficulties, true);
        RadioButton medium = difficultyOption("Medium — 4×4", 4, difficulties, false);
        RadioButton hard = difficultyOption("Hard — 5×5", 5, difficulties, false);

        Button start = navigationButton("Start game", () -> {
            RadioButton selected = (RadioButton) difficulties.getSelectedToggle();
            int size = (int) selected.getUserData();
            showGame(size, difficultyName(size));
        });
        start.getStyleClass().add("accent-button");
        VBox content = new VBox(18, title, spacer(10), easy, medium, hard,
                spacer(22), start, secondaryButton("Main menu", this::showMainMenu));
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(45));
        content.getStyleClass().addAll("screen", "light-screen");
        show(content, "Choose difficulty");
    }

    private void showGame(int size, String difficulty) {
        show(new GamePanel(size, difficulty, this::showDifficulty), difficulty);
    }

    private void showTutorial() {
        VBox steps = new VBox(22);
        steps.setAlignment(Pos.TOP_CENTER);
        steps.setPadding(new Insets(24));
        steps.getStyleClass().add("tutorial-content");
        Label title = new Label("How to play");
        title.getStyleClass().add("screen-title");
        title.setFocusTraversable(true);
        steps.getChildren().add(title);
        Label explanation = new Label(
                "Pressing a square changes it and its direct neighbours. "
                        + "Follow the orange suggestion to solve this example.");
        explanation.setWrapText(true);
        explanation.setMaxWidth(400);
        explanation.getStyleClass().add("tutorial-description");
        steps.getChildren().add(explanation);
        addTutorialBoards(steps);
        Button backButton = secondaryButton("Main menu", this::showMainMenu);
        backButton.setFocusTraversable(false);
        steps.getChildren().add(backButton);

        ScrollPane scrollPane = new ScrollPane(steps);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setVvalue(0);
        scrollPane.getStyleClass().add("screen");
        show(scrollPane, "Tutorial");
        Platform.runLater(() -> {
            title.requestFocus();
            scrollPane.applyCss();
            scrollPane.layout();
            scrollPane.setVvalue(scrollPane.getVmin());
        });
    }

    private void showTextScreen(String titleText, String body) {
        Label title = new Label(titleText);
        title.getStyleClass().add("screen-title");
        Text text = new Text(body);
        text.getStyleClass().add("body-text");
        text.wrappingWidthProperty().bind(scene.widthProperty().subtract(100));
        VBox content = new VBox(28, title, text, secondaryButton("Main menu", this::showMainMenu));
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(50));
        content.getStyleClass().add("screen");
        show(content, titleText);
    }

    private void showMainMenu() {
        show(createMainMenu(), "Main menu");
    }

    private void show(Region root, String title) {
        root.setOpacity(0);
        root.setTranslateY(18);
        scene.setRoot(root);
        stage.setTitle("Lights Off — " + title);

        FadeTransition fade = new FadeTransition(Duration.millis(280), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(280), root);
        slide.setFromY(18);
        slide.setToY(0);
        new ParallelTransition(fade, slide).play();
    }

    private static Button navigationButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("primary-button");
        button.setMaxWidth(250);
        button.setOnAction(event -> action.run());
        return button;
    }

    private static Button secondaryButton(String text, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().add("secondary-button");
        button.setOnAction(event -> action.run());
        return button;
    }

    private static RadioButton difficultyOption(String text, int size, ToggleGroup group,
                                                boolean selected) {
        RadioButton button = new RadioButton(text);
        button.setUserData(size);
        button.setToggleGroup(group);
        button.setSelected(selected);
        button.getStyleClass().add("difficulty-option");
        return button;
    }

    private static void addTutorialBoards(VBox parent) {
        int[] moves = {2, 8, 4, 0};
        int[][] state = new int[3][3];
        for (int move : moves) {
            BoardUtils.toggle(state, move / 3, move % 3);
        }

        addTutorialStep(parent, "Starting board", state, moves[0]);
        for (int index = 0; index < moves.length; index++) {
            int move = moves[index];
            BoardUtils.toggle(state, move / 3, move % 3);
            Integer nextMove = index + 1 < moves.length ? moves[index + 1] : null;
            String description = nextMove == null
                    ? "All lights are off — puzzle solved"
                    : "Board after move " + (index + 1);
            addTutorialStep(parent, description, state, nextMove);
        }
    }

    private static void addTutorialStep(VBox parent, String description,
                                        int[][] state, Integer highlightedCell) {
        BoardView preview = new BoardView(3, 220, null);
        int[] highlight = null;
        if (highlightedCell != null) {
            highlight = new int[9];
            highlight[highlightedCell] = 1;
        }
        preview.render(copyBoard(state), highlight);
        Label label = new Label(description);
        label.getStyleClass().add("tutorial-label");
        VBox step = new VBox(8, label, preview);
        step.setAlignment(Pos.CENTER);
        parent.getChildren().add(step);
    }

    private static int[][] copyBoard(int[][] source) {
        int[][] copy = new int[source.length][source.length];
        for (int row = 0; row < source.length; row++) {
            System.arraycopy(source[row], 0, copy[row], 0, source[row].length);
        }
        return copy;
    }

    private static Region spacer(double height) {
        Region spacer = new Region();
        spacer.setMinHeight(height);
        return spacer;
    }

    private static String difficultyName(int size) {
        return size == 3 ? "Easy" : size == 4 ? "Medium" : "Hard";
    }

    private static String gameDescription() {
        return "Lights Off 2.0 is a one-player logic puzzle for JavaFX. Choose a 3×3, 4×4 "
                + "or 5×5 board. Pressing a square toggles that light and its direct horizontal "
                + "and vertical neighbours. Turn every light off to win. Every generated board "
                + "is guaranteed to have a solution, and optional hints show one valid sequence.";
    }

    private static String authorDescription() {
        return "Created by Mateusz Lubiński as a computer science project. Version 2.0 replaces "
                + "the original Swing interface with JavaFX and shares one responsive board view "
                + "between gameplay and the tutorial. The puzzle solver uses Gaussian elimination "
                + "over GF(2).";
    }

    private static String requireResource(String path) {
        if (Game.class.getResource(path) == null) {
            throw new IllegalStateException("Missing resource: " + path);
        }
        return Game.class.getResource(path).toExternalForm();
    }

    }
}

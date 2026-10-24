package pl.realcode.lightsoff;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

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
        VBox content = new VBox(18, title, spacer(10), easy, medium, hard,
                spacer(22), start, secondaryButton("Main menu", this::showMainMenu));
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(45));
        content.getStyleClass().addAll("screen", "dark-screen");
        show(content, "Choose difficulty");
    }

    private void showGame(int size, String difficulty) {
        show(new GamePanel(size, difficulty, this::showDifficulty), difficulty);
    }

    private void showTutorial() {
        VBox steps = new VBox(22);
        steps.setAlignment(Pos.CENTER);
        steps.setPadding(new Insets(24));
        steps.getStyleClass().add("tutorial-content");
        Label title = new Label("How to play");
        title.getStyleClass().add("screen-title");
        steps.getChildren().add(title);
        addTutorialStep(steps, "/Images/1.png", "Example of a 3×3 puzzle");
        addTutorialStep(steps, "/Images/11.png", "Step 1");
        addTutorialStep(steps, "/Images/21.png", "Step 2");
        addTutorialStep(steps, "/Images/31.png", "Step 3");
        addTutorialStep(steps, "/Images/41.png", "Step 4");
        addTutorialStep(steps, "/Images/5.png", "Solved puzzle");
        addTutorialStep(steps, "/Images/animacja.gif", "Animated solution");
        steps.getChildren().add(secondaryButton("Main menu", this::showMainMenu));

        ScrollPane scrollPane = new ScrollPane(steps);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("screen");
        show(scrollPane, "Tutorial");
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
        scene.setRoot(root);
        stage.setTitle("Lights Off — " + title);
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

    private static void addTutorialStep(VBox parent, String resource, String description) {
        InputStream stream = Game.class.getResourceAsStream(resource);
        if (stream == null) {
            return;
        }
        ImageView imageView = new ImageView(new Image(stream));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(320);
        Label label = new Label(description);
        label.getStyleClass().add("tutorial-label");
        VBox step = new VBox(8, label, imageView);
        step.setAlignment(Pos.CENTER);
        parent.getChildren().add(step);
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
        return "Lights Off is a one-player puzzle played on a grid of lamps. "
                + "Pressing a square toggles that lamp and its horizontal and vertical neighbours. "
                + "Turn every lamp off to win. Every generated board has at least one solution.";
    }

    private static String authorDescription() {
        return "Created by Mateusz Lubiński as a computer science project. "
                + "The hint system solves the puzzle using Gaussian elimination over GF(2).";
    }

    private static String requireResource(String path) {
        if (Game.class.getResource(path) == null) {
            throw new IllegalStateException("Missing resource: " + path);
        }
        return Game.class.getResource(path).toExternalForm();
    }

    }
}

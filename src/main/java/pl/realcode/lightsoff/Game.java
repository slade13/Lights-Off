package pl.realcode.lightsoff;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;

public final class Game extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String MENU = "MENU";
    private static final String DIFFICULTY = "DIFFICULTY";
    private static final String TUTORIAL = "TUTORIAL";
    private static final String ABOUT = "ABOUT";
    private static final String AUTHOR = "AUTHOR";
    private static final String EASY = "EASY";
    private static final String MEDIUM = "MEDIUM";
    private static final String HARD = "HARD";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new Game().setVisible(true);
            } catch (RuntimeException exception) {
                JOptionPane.showMessageDialog(null,
                        "Unable to start the game: " + exception.getMessage(),
                        "Lights Off", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public Game() {
        super("lightsOFF::Main menu");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 400);
        setLocationRelativeTo(null);

        cards.add(createMainMenu(), MENU);
        cards.add(createDifficultyScreen(), DIFFICULTY);
        cards.add(createTutorial(), TUTORIAL);
        cards.add(createTextScreen("About game", gameDescription()), ABOUT);
        cards.add(createTextScreen("Author", authorDescription()), AUTHOR);
        cards.add(new GamePanel(3, "Easy", () -> showCard(DIFFICULTY, "Start game")), EASY);
        cards.add(new GamePanel(4, "Medium", () -> showCard(DIFFICULTY, "Start game")), MEDIUM);
        cards.add(new GamePanel(5, "Hard", () -> showCard(DIFFICULTY, "Start game")), HARD);
        setContentPane(cards);
    }

    private JPanel createMainMenu() {
        JPanel panel = verticalPanel(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 70, 25, 70));

        JLabel title = new JLabel("Lights Off Game", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 29));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(20));
        panel.add(menuButton("Start game", () -> showCard(DIFFICULTY, "Start game")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(menuButton("Tutorial", () -> showCard(TUTORIAL, "Tutorial")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(menuButton("About game", () -> showCard(ABOUT, "About game")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(menuButton("Author", () -> showCard(AUTHOR, "Author")));
        return panel;
    }

    private JPanel createDifficultyScreen() {
        JPanel panel = verticalPanel(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(35, 80, 25, 80));

        JLabel label = new JLabel("Difficulty:");
        label.setForeground(Color.WHITE);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 17));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(10));

        ButtonGroup group = new ButtonGroup();
        JRadioButton easy = difficultyOption("Easy", EASY, true, group);
        JRadioButton medium = difficultyOption("Medium", MEDIUM, false, group);
        JRadioButton hard = difficultyOption("Hard", HARD, false, group);
        panel.add(easy);
        panel.add(medium);
        panel.add(hard);
        panel.add(Box.createVerticalGlue());

        JButton start = menuButton("Let the game begin...",
                () -> showCard(group.getSelection().getActionCommand(), "Game"));
        panel.add(start);
        panel.add(Box.createVerticalStrut(12));
        panel.add(menuButton("Main menu", () -> showCard(MENU, "Main menu")));
        return panel;
    }

    private JScrollPane createTutorial() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        addTutorialStep(content, "/Images/1.png", "Example of solving 3x3 puzzle");
        addTutorialStep(content, "/Images/11.png", "Step 1");
        addTutorialStep(content, "/Images/21.png", "Step 2");
        addTutorialStep(content, "/Images/31.png", "Step 3");
        addTutorialStep(content, "/Images/41.png", "Step 4");
        addTutorialStep(content, "/Images/5.png", "Solved puzzle");
        addTutorialStep(content, "/Images/animacja.gif", "Animation of the solution");

        JButton back = menuButton("Main menu", () -> showCard(MENU, "Main menu"));
        content.add(back);
        return new JScrollPane(content);
    }

    private JPanel createTextScreen(String heading, String text) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel(heading, SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        panel.add(title, BorderLayout.NORTH);

        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        textArea.setBackground(panel.getBackground());
        panel.add(textArea, BorderLayout.CENTER);

        JButton back = new JButton("Main menu");
        back.addActionListener(event -> showCard(MENU, "Main menu"));
        panel.add(back, BorderLayout.SOUTH);
        return panel;
    }

    private static JPanel verticalPanel(Color background) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(background);
        return panel;
    }

    private static JButton menuButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(240, 42));
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.addActionListener(event -> action.run());
        return button;
    }

    private static JRadioButton difficultyOption(String text, String command,
                                                  boolean selected, ButtonGroup group) {
        JRadioButton button = new JRadioButton(text, selected);
        button.setActionCommand(command);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setForeground(Color.WHITE);
        button.setBackground(Color.BLACK);
        group.add(button);
        return button;
    }

    private static void addTutorialStep(JPanel parent, String resource, String description) {
        URL imageUrl = Game.class.getResource(resource);
        JLabel label = new JLabel(description,
                imageUrl == null ? null : new ImageIcon(imageUrl), SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.TOP);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        parent.add(label);
    }

    private void showCard(String card, String title) {
        cardLayout.show(cards, card);
        setTitle("lightsOFF::" + title);
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
}

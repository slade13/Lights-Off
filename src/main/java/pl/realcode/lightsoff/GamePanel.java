package pl.realcode.lightsoff;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Reusable game view for every supported board size. */
public final class GamePanel extends JPanel {

    private static final int BOARD_PIXELS = 300;
    private static final Color GRID_COLOR = Color.GRAY;

    private final LightsOutBoard board;
    private final BoardCanvas canvas = new BoardCanvas();
    private final JButton hintButton = new JButton("Show hints");

    private boolean hintsVisible;
    private boolean hintsUsed;
    private int[] solution;

    public GamePanel(int size, String difficultyName, Runnable onBack) {
        board = new LightsOutBoard(size);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel difficultyLabel = new JLabel(difficultyName, SwingConstants.CENTER);
        add(difficultyLabel, BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(createControls(onBack), BorderLayout.SOUTH);
    }

    private JPanel createControls(Runnable onBack) {
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        controls.setBackground(Color.WHITE);

        hintButton.addActionListener(event -> toggleHints());
        controls.add(hintButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> onBack.run());
        controls.add(backButton);
        return controls;
    }

    private void toggleHints() {
        hintsVisible = !hintsVisible;
        hintsUsed |= hintsVisible;
        solution = hintsVisible ? board.findSolution() : null;
        hintButton.setText(hintsVisible ? "Hide hints" : "Show hints");
        canvas.repaint();
    }

    private void pressCell(int row, int column) {
        board.press(row, column);
        solution = hintsVisible ? board.findSolution() : null;
        canvas.repaint();

        if (board.isSolved()) {
            SwingUtilities.invokeLater(this::finishGame);
        }
    }

    private void finishGame() {
        String message = hintsUsed ? "You won! But you used hints." : "You won!";
        JOptionPane.showMessageDialog(Game.contentPane, message);
        board.reset();
        hintsVisible = false;
        hintsUsed = false;
        solution = null;
        hintButton.setText("Show hints");
        canvas.repaint();
    }

    private final class BoardCanvas extends JComponent {

        private BoardCanvas() {
            setPreferredSize(new Dimension(BOARD_PIXELS, BOARD_PIXELS));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    Point cell = cellAt(event.getPoint());
                    if (cell != null) {
                        pressCell(cell.y, cell.x);
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            try {
                graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                int cellSize = cellSize();
                int originX = (getWidth() - cellSize * board.getSize()) / 2;
                int originY = (getHeight() - cellSize * board.getSize()) / 2;

                for (int row = 0; row < board.getSize(); row++) {
                    for (int column = 0; column < board.getSize(); column++) {
                        paintCell(graphics2D, row, column, originX, originY, cellSize);
                    }
                }
            } finally {
                graphics2D.dispose();
            }
        }

        private void paintCell(Graphics2D graphics, int row, int column,
                               int originX, int originY, int cellSize) {
            int x = originX + column * cellSize;
            int y = originY + row * cellSize;
            boolean lightOn = board.isLightOn(row, column);

            graphics.setColor(lightOn ? Color.WHITE : Color.BLACK);
            graphics.fillRect(x, y, cellSize, cellSize);
            graphics.setColor(GRID_COLOR);
            graphics.drawRect(x, y, cellSize, cellSize);

            int index = row * board.getSize() + column;
            if (hintsVisible && solution != null && solution[index] == 1) {
                int diameter = Math.max(8, cellSize / 3);
                graphics.setColor(lightOn ? Color.BLACK : Color.WHITE);
                graphics.fillOval(x + (cellSize - diameter) / 2,
                        y + (cellSize - diameter) / 2, diameter, diameter);
            }
        }

        private Point cellAt(Point point) {
            int cellSize = cellSize();
            int boardSize = cellSize * board.getSize();
            int originX = (getWidth() - boardSize) / 2;
            int originY = (getHeight() - boardSize) / 2;
            if (point.x < originX || point.x >= originX + boardSize
                    || point.y < originY || point.y >= originY + boardSize) {
                return null;
            }
            return new Point((point.x - originX) / cellSize, (point.y - originY) / cellSize);
        }

        private int cellSize() {
            return Math.max(1, Math.min(getWidth(), getHeight()) / board.getSize());
        }
    }
}

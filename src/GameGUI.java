import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameGUI extends JFrame {
    private final Simulation simulation;
    private final GamePanel gamePanel;
    private final JButton saveButton;
    private final JButton loadButton;
    private final JButton quitButton;
    private final Timer refreshTimer;
    private static final int CELL_SIZE = 40;
    private static final int REFRESH_RATE = 100;

    public GameGUI(int fieldSize, int numFarmers) {
        this.simulation = new Simulation(fieldSize, numFarmers);
        this.gamePanel = new GamePanel(fieldSize);

        // Setup frame
        setTitle("Carrot Farm Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create control panel
        JPanel controlPanel = new JPanel();
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        quitButton = new JButton("Quit");

        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        controlPanel.add(quitButton);

        // Add components
        add(gamePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Setup button actions
        setupButtonActions();

        // Setup refresh timer
        refreshTimer = new Timer(REFRESH_RATE, e -> {
            gamePanel.updateGrid(simulation.getGrid());
            repaint();
        });

        // Size and display
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setupButtonActions() {
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                simulation.saveState(fileChooser.getSelectedFile().getPath());
            }
        });

        loadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                simulation.loadState(fileChooser.getSelectedFile().getPath());
            }
        });

        quitButton.addActionListener(e -> {
            simulation.stopSimulation();
            dispose();
            System.exit(0);
        });
    }

    public void start() {
        setVisible(true);
        simulation.startSimulation();
        refreshTimer.start();
    }

    private static class GamePanel extends JPanel {
        private Grid grid;
        private final int fieldSize;
        private static final Color CARROT_COLOR = new Color(255, 140, 0); // Bright orange
        private static final Color FARMER_COLOR = new Color(30, 144, 255); // Bright blue
        private static final Font ENTITY_FONT = new Font("Arial", Font.BOLD, 14);

        public GamePanel(int fieldSize) {
            this.fieldSize = fieldSize;
            setPreferredSize(new Dimension(fieldSize * CELL_SIZE, fieldSize * CELL_SIZE));
        }

        public void updateGrid(Grid grid) {
            this.grid = grid;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (grid == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setFont(ENTITY_FONT);

            // Draw grid
            for (int x = 0; x < fieldSize; x++) {
                for (int y = 0; y < fieldSize; y++) {
                    drawCell(g2d, x, y);
                }
            }

            // Draw entities
            for (Entity entity : grid.getEntities()) {
                if (entity.isActive()) {
                    drawEntity(g2d, entity);
                }
            }
        }

        private void drawCell(Graphics2D g, int x, int y) {
            Cell cell = grid.getCell(x, y);
            int px = x * CELL_SIZE;
            int py = y * CELL_SIZE;

            // Draw cell background
            g.setColor(Color.WHITE);
            g.fillRect(px, py, CELL_SIZE, CELL_SIZE);
            g.setColor(Color.LIGHT_GRAY);
            g.drawRect(px, py, CELL_SIZE, CELL_SIZE);

            // Draw cell content
            switch (cell.getState()) {
                case EMPTY:
                    break;
                case GROWING:
                    g.setColor(new Color(144, 238, 144)); // Light green
                    g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g.setColor(Color.BLACK);
                    String growthText = String.valueOf(cell.getGrowthStage());
                    drawCenteredString(g, growthText, px, py);
                    break;
                case READY:
                    g.setColor(CARROT_COLOR);
                    g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g.setColor(Color.WHITE);
                    drawCenteredString(g, "C", px, py);
                    break;
                case DAMAGED:
                    g.setColor(Color.RED);
                    g.drawLine(px + 5, py + 5, px + CELL_SIZE - 5, py + CELL_SIZE - 5);
                    g.drawLine(px + 5, py + CELL_SIZE - 5, px + CELL_SIZE - 5, py + 5);
                    break;
            }
        }

        private void drawEntity(Graphics2D g, Entity entity) {
            int[] pos = entity.getPosition();
            int px = pos[0] * CELL_SIZE;
            int py = pos[1] * CELL_SIZE;

            if (entity instanceof Farmer) {
                g.setColor(FARMER_COLOR);
                g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                g.setColor(Color.WHITE);
                drawCenteredString(g, "F", px, py);
            } else if (entity instanceof Dog) {
                g.setColor(Color.BLACK);
                g.fillRect(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                g.setColor(Color.WHITE);
                drawCenteredString(g, "D", px, py);
            } else if (entity instanceof Rabbit) {
                g.setColor(Color.RED);
                int[] xPoints = {px + CELL_SIZE/2, px + 10, px + CELL_SIZE - 10};
                int[] yPoints = {py + 10, py + CELL_SIZE - 10, py + CELL_SIZE - 10};
                g.fillPolygon(xPoints, yPoints, 3);
                g.setColor(Color.WHITE);
                FontMetrics metrics = g.getFontMetrics();
                String text = "R";
                int textWidth = metrics.stringWidth(text);
                int textHeight = metrics.getHeight();

                // Calculate center point of triangle (average of the three points)
                int centerX = (xPoints[0] + xPoints[1] + xPoints[2]) / 3;
                int centerY = (yPoints[0] + yPoints[1] + yPoints[2]) / 3;

                // Draw the text centered in the triangle
                g.drawString(text,
                        centerX - textWidth/2,
                        centerY + textHeight/4);
            }
        }

        private void drawCenteredString(Graphics2D g, String text, int cellX, int cellY) {
            FontMetrics metrics = g.getFontMetrics();
            int x = cellX + (CELL_SIZE - metrics.stringWidth(text)) / 2;
            int y = cellY + ((CELL_SIZE - metrics.getHeight()) / 2) + metrics.getAscent();
            g.drawString(text, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int fieldSize = 10;
            int numFarmers = 2;

            try {
                fieldSize = Integer.parseInt(JOptionPane.showInputDialog(
                        null,
                        "Enter field size (5-20):",
                        "Setup",
                        JOptionPane.QUESTION_MESSAGE
                ));

                numFarmers = Integer.parseInt(JOptionPane.showInputDialog(
                        null,
                        "Enter number of farmers (1-5):",
                        "Setup",
                        JOptionPane.QUESTION_MESSAGE
                ));
            } catch (NumberFormatException | NullPointerException e) {
                // Use default values if input is invalid or cancelled
            }

            GameGUI game = new GameGUI(
                    Math.min(Math.max(fieldSize, 5), 20),
                    Math.min(Math.max(numFarmers, 1), 5)
            );
            game.start();
        });
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;

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
        private final Random random = new Random();
        private final Map<Farmer, Boolean> farmerImages = new HashMap<>();
        // Image fields
        private BufferedImage rabbitImage;
        private BufferedImage farmerImage;
        private BufferedImage farmer1Image;
        private BufferedImage carrotImage;
        private BufferedImage dogImage;

        public GamePanel(int fieldSize) {
            this.fieldSize = fieldSize;
            setPreferredSize(new Dimension(fieldSize * CELL_SIZE, fieldSize * CELL_SIZE));
            loadImages();
        }

        private void loadImages() {
            try {
                rabbitImage = ImageIO.read(new File("./img/rabbit.jpg"));
                System.out.println("Rabbit image loaded successfully");
            } catch (IOException e) {
                System.out.println("Rabbit image could not be loaded. Using fallback shape.");
            }
            boolean atLeastOneFarmerImageLoaded = false;
            try {
                farmerImage = ImageIO.read(new File("./img/farmer.jpg"));
                System.out.println("Farmer image loaded successfully");
            } catch (IOException e) {
                System.out.println("Farmer image could not be loaded. Using fallback shape.");
            }
            try {
                farmer1Image = ImageIO.read(new File("./img/farmer1.jpg"));
                atLeastOneFarmerImageLoaded = true;
                System.out.println("Farmer1 image loaded successfully");
            } catch (IOException e) {
                System.out.println("farmer1.jpg could not be loaded.");
            }

            try {
                carrotImage = ImageIO.read(new File("./img/carrot.png"));
                System.out.println("Carrot image loaded successfully");
            } catch (IOException e) {
                System.out.println("Carrot image could not be loaded. Using fallback shape.");
            }

            try {
                dogImage = ImageIO.read(new File("./img/dog.jpg"));
                System.out.println("Dog image loaded successfully");
            } catch (IOException e) {
                System.out.println("Dog image could not be loaded. Using fallback shape.");
            }
        }

        public void updateGrid(Grid grid) {
            for (Entity entity : grid.getEntities()) {
                if (entity instanceof Farmer && !farmerImages.containsKey(entity)) {
                    // Assign a random image choice (true/false) for new farmers
                    farmerImages.put((Farmer)entity, random.nextBoolean());
                }
            }
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
                    g.setColor(new Color(92, 236, 92)); // Light green
                    g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g.setColor(Color.BLACK);
                    String growthText = String.valueOf(cell.getGrowthStage());
                    drawCenteredString(g, growthText, px, py);
                    break;
                case READY:
                    if (carrotImage != null) {
                        g.drawImage(carrotImage, px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10, null);
                    } else {
                        g.setColor(CARROT_COLOR);
                        g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                        g.setColor(Color.WHITE);
                        drawCenteredString(g, "C", px, py);
                    }
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
                if (farmerImage != null || farmer1Image != null) {
                    BufferedImage selectedImage;
                    if (farmerImage != null && farmer1Image != null) {
                        // Use the stored choice for this farmer
                        boolean useFirstImage = farmerImages.getOrDefault(entity, true);
                        selectedImage = useFirstImage ? farmerImage : farmer1Image;
                    } else {
                        selectedImage = farmerImage != null ? farmerImage : farmer1Image;
                    }
                    g.drawImage(selectedImage, px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10, null);
                } else {
                    g.setColor(FARMER_COLOR);
                    g.fillOval(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g.setColor(Color.WHITE);
                    drawCenteredString(g, "F", px, py);
                }}
                else if (entity instanceof Dog) {
                if (dogImage != null) {
                    g.drawImage(dogImage, px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10, null);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10);
                    g.setColor(Color.WHITE);
                    drawCenteredString(g, "D", px, py);
                }
            } else if (entity instanceof Rabbit) {
                if (rabbitImage != null) {
                    g.drawImage(rabbitImage, px + 5, py + 5, CELL_SIZE - 10, CELL_SIZE - 10, null);
                } else {
                    g.setColor(Color.RED);
                    int[] xPoints = {px + CELL_SIZE/2, px + 10, px + CELL_SIZE - 10};
                    int[] yPoints = {py + 10, py + CELL_SIZE - 10, py + CELL_SIZE - 10};
                    g.fillPolygon(xPoints, yPoints, 3);
                    g.setColor(Color.WHITE);
                    drawCenteredString(g, "R", px, py + 10);
                }
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
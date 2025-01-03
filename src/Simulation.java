import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {
    private final Grid grid;
    private final List<Thread> entityThreads;
    private final List<Entity> entities;
    private volatile boolean running;
    private final Random random;
    private final int numFarmers; // Store the number of farmers for reinitialization
    private Thread growthThread; // Store reference to the growth/rabbit spawning thread

    public Grid getGrid() {
        return this.grid;
    }

    public Simulation(int fieldSize, int numFarmers) {
        this.grid = new Grid(fieldSize);
        this.entityThreads = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.random = new Random();
        this.numFarmers = numFarmers;

        // Initialize farmers
        initializeFarmers();
        initializeGrowthThread();
    }
    private void initializeGrowthThread() {
        // Initialize (but don't start) the growth thread
        growthThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                    spawnRabbit();
                    grid.updateGrowth();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }
    private void initializeFarmers() {
        // Clear existing entities and threads
        entities.clear();
        entityThreads.clear();

        // Initialize farmers
        for (int i = 0; i < numFarmers; i++) {
            Farmer farmer = new Farmer(
                    random.nextInt(grid.getSize()),
                    random.nextInt(grid.getSize()),
                    grid
            );
            entities.add(farmer);
            grid.addEntity(farmer);
        }
    }

    public void startSimulation() {
        running = true;

        // Start entity threads
        for (Entity entity : entities) {
            Thread thread = new Thread(entity);
            entityThreads.add(thread);
            thread.start();
        }

        // Rabbit spawning and growth update thread
        if (growthThread == null || !growthThread.isAlive()) {
            initializeGrowthThread();
        }
        growthThread.start();
    }

    private void spawnRabbit() {
        if (random.nextDouble() < 0.3) { // 30% chance to spawn a rabbit
            Rabbit rabbit = new Rabbit(
                    random.nextInt(grid.getSize()),
                    random.nextInt(grid.getSize()),
                    grid
            );
            grid.addEntity(rabbit);
            Thread rabbitThread = new Thread(rabbit);
            entityThreads.add(rabbitThread);
            rabbitThread.start();
        }
    }

    public void stopSimulation() {
        running = false;
        for (Entity entity : entities) {
            entity.stopRunning();
        }
        for (Thread thread : entityThreads) {
            thread.interrupt();
            try {
                thread.join(1000); // Wait for threads to finish with timeout
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        if (growthThread != null) {
            growthThread.interrupt();
            try {
                growthThread.join(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            growthThread = null;
        }

        // Clear thread lists
        entityThreads.clear();
    }

    public void saveState(String filename) {
        // Create a serializable state object
        GridState state = new GridState(grid);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(state);
            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save game state: " + e.getMessage());
        }
    }

    public void loadState(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            GridState loadedState = (GridState) ois.readObject();

            // Stop current simulation
            stopSimulation();
            grid.clearEntities();

            // Apply the loaded state
            loadedState.applyTo(this.grid);
            initializeFarmers();

            System.out.println("Game state loaded successfully.");

            // Restart simulation
            startSimulation();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game state: " + e.getMessage());
        }
    }
}
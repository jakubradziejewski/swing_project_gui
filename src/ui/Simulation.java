
package ui;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import config.GameConfig;
import core.GameObject;
import entities.*;
import grid.*;
public class Simulation {
    private final Grid grid;
    private final List<Thread> entityThreads;
    private final List<Entity> entities;
    private volatile boolean running;
    private final Random random;
    private final int numFarmers;
    private Thread growthThread;

    public Grid getGrid() {
        return this.grid;
    }

    public Simulation(int fieldSize, int numFarmers) {
        this.grid = new Grid(fieldSize);
        this.entityThreads = new ArrayList<>();
        this.entities = new ArrayList<>();
        this.random = new Random();
        this.numFarmers = numFarmers;

        initializeFarmers();
        initializeGrowthThread();
    }

    // Initialize (but don't start) the growth thread
    private void initializeGrowthThread() {
        growthThread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(GameConfig.getInstance().getCarrotGrowthTime());
                    spawnRabbit();
                    grid.updateGrowth();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    // Initialize farmers
    private void initializeFarmers() {
        entities.clear();
        entityThreads.clear();

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

    // Start threads; if state is loaded -> restart the simulation
    public void startSimulation() {
        running = true;

        for (Entity entity : entities) {
            Thread thread = new Thread(entity);
            entityThreads.add(thread);
            thread.start();
        }

        if (growthThread == null || !growthThread.isAlive()) {
            initializeGrowthThread();
        }
        growthThread.start();
    }

    // Spawn rabbits with user defined spawn rate
    private void spawnRabbit() {
        if (random.nextDouble() < GameConfig.getInstance().getRabbitSpawnRate()) {
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

    // Stop the simulation
    public void stopSimulation() {
        running = false;
        for (Entity entity : entities) {
            entity.stopRunning();
        }
        for (Thread thread : entityThreads) {
            thread.interrupt();
            try {
                thread.join(1000);
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

        entityThreads.clear();
    }

    // Save game state to file
    public void saveState(String filename) {
        GridState state = new GridState(grid);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(state);
            System.out.println("Game state saved successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save game state: " + e.getMessage());
        }
    }

    // Load game state from file
    public void loadState(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            GridState loadedState = (GridState) ois.readObject();

            stopSimulation();
            grid.clearEntities();

            loadedState.applyTo(this.grid);
            initializeFarmers();
            System.out.println("Game state loaded successfully.");
            startSimulation();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to load game state: " + e.getMessage());
        }
    }
}
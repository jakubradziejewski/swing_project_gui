package entities;

import core.GameObject;
import grid.Grid;
import java.util.Random;

// Base class for all entities in the simulation
public abstract class Entity extends GameObject implements Runnable {
    protected Grid grid;
    protected Random random;
    protected volatile boolean running;
    protected int speed;

    public Entity(int x, int y, Grid grid, int speed) {
        super(x, y);
        this.grid = grid;
        this.random = new Random();
        this.running = true;
        this.speed = speed;
    }

    protected void moveRandomly() {
        int newX = x + random.nextInt(3) - 1;
        int newY = y + random.nextInt(3) - 1;

        if (grid.isValidPosition(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void stopRunning() {
        running = false;
    }

    @Override
    public abstract void run();
}
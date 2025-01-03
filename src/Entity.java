import java.util.Random;

// Base class for all entities in the simulation
public abstract class Entity extends Object implements Runnable {
    protected int speed;
    protected Random random;
    protected Grid grid;
    protected volatile boolean running;

    // Initialize entity with position, grid, and speed
    public Entity(int x, int y, Grid grid, int speed) {
        super(x, y);
        this.grid = grid;
        this.speed = speed;
        this.random = new Random();
        this.running = true;
    }

    // Move entity randomly within the grid
    protected void moveRandomly() {
        int newX = x + random.nextInt(3) - 1;
        int newY = y + random.nextInt(3) - 1;

        if (grid.isValidPosition(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    // Stop the entity's running thread
    public void stopRunning() {
        running = false;
    }

    @Override
    public abstract void run();
}
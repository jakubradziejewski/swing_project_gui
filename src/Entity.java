import java.util.Random;
public abstract class Entity extends Object implements Runnable {
    protected int speed;
    protected Random random;
    protected Grid grid;
    protected volatile boolean running;

    public Entity(int x, int y, Grid grid, int speed) {
        super(x, y);
        this.grid = grid;
        this.speed = speed;
        this.random = new Random();
        this.running = true;
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
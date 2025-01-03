public class Rabbit extends Entity {
    private int eatingDuration;
    private static final int EATING_TIME = 500;

    // Initialize rabbit with position and grid
    public Rabbit(int x, int y, Grid grid) {
        super(x, y, grid, 1);
        this.eatingDuration = random.nextInt(5) + 3;
    }

    @Override
    // Main behavior loop for the rabbit
    public void run() {
        int mealsEaten = 0;
        while (running && mealsEaten < eatingDuration && isActive) {
            try {
                moveRandomly();
                Cell currentCell = grid.getCell(x, y);

                if (currentCell.getState() == CellState.READY) {
                    Thread.sleep(EATING_TIME);
                    currentCell.damage();
                    mealsEaten++;
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        setActive(false);
    }
}
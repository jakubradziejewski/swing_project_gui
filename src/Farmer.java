public class Farmer extends Entity {
    private Dog companion;

    // Initialize farmer with position and grid
    public Farmer(int x, int y, Grid grid) {
        super(x, y, grid, 1);
        int size = grid.getSize();
        int dogX = (x + 1) % size;
        int dogY = (y + 1) % size;
        this.companion = new Dog(dogX, dogY, grid);
        new Thread(companion).start();
    }

    @Override
    // Main behavior loop for the farmer
    public void run() {
        while (running) {
            try {
                moveRandomly();
                Cell currentCell = grid.getCell(x, y);

                if (currentCell.getState() == CellState.DAMAGED) {
                    Thread.sleep(GameConfig.getInstance().getFarmerRepairTime());
                    currentCell.repair();
                } else if (currentCell.getState() == CellState.EMPTY) {
                    Thread.sleep(GameConfig.getInstance().getFarmerPlantTime());
                    currentCell.plant();
                }

                Rabbit nearbyRabbit = grid.findNearbyRabbit(x, y, 3);
                if (nearbyRabbit != null) {
                    alertDog(nearbyRabbit);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        companion.stopRunning();
    }

    // Alert the dog to chase a rabbit
    public void alertDog(Rabbit rabbit) {
        companion.setTarget(rabbit);
    }
}
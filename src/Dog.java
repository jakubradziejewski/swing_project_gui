public class Dog extends Entity {
    private Rabbit target;

    // Initialize dog with position and grid
    public Dog(int x, int y, Grid grid) {
        super(x, y, grid, 1);
        grid.addEntity(this);
    }

    // Set target rabbit to chase
    public void setTarget(Rabbit rabbit) {
        this.target = rabbit;
    }

    // Move towards the target rabbit
    private void moveTowardsTarget() {
        int[] targetPos = target.getPosition();
        x += Integer.compare(targetPos[0], x);
        y += Integer.compare(targetPos[1], y);
    }

    @Override
    public void run() {
        // Main behavior loop for the dog
        while (running) {
            try {
                if (target != null && target.isActive()) {
                    moveTowardsTarget();
                    if (x == target.getPosition()[0] && y == target.getPosition()[1]) {
                        target.setActive(false);
                        target = null;
                    }
                } else {
                    int detectionRange = GameConfig.getInstance().getDogDetectionRange();
                    target = grid.findNearbyRabbit(x, y, detectionRange);
                    moveRandomly();
                }
                Thread.sleep(800);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


}
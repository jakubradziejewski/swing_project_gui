package grid;

public class Cell {
    private CellState state;
    private int growthStage;
    private static final int MAX_GROWTH = 5;

    public Cell() {
        this.state = CellState.EMPTY;
        this.growthStage = 0;
    }

    public synchronized void plant() {
        this.state = CellState.GROWING;
        this.growthStage = 0;
    }

    public synchronized void damage() {
        this.state = CellState.DAMAGED;
        this.growthStage = 0;
    }

    public synchronized void repair() {
        this.state = CellState.EMPTY;
        this.growthStage = 0;
    }

    public synchronized void grow() {
        if (state == CellState.GROWING && growthStage < MAX_GROWTH) {
            growthStage++;
            if (growthStage == MAX_GROWTH) {
                state = CellState.READY;
            }
        }
    }

    public synchronized CellState getState() {
        return state;
    }

    public synchronized int getGrowthStage() {
        return growthStage;
    }
}
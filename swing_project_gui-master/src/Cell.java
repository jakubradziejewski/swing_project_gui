public class Cell {
    private CellState state;
    private int growthStage;
    private static final int MAX_GROWTH = 5;

    public Cell() {
        this.state = CellState.EMPTY;
        this.growthStage = 0;
    }

    public void plant() {
        this.state = CellState.GROWING;
        this.growthStage = 0;
    }

    public void damage() {
        this.state = CellState.DAMAGED;
        this.growthStage = 0;
    }

    public void repair() {
        this.state = CellState.EMPTY;
        this.growthStage = 0;
    }

    public void grow() {
        if (state == CellState.GROWING && growthStage < MAX_GROWTH) {
            growthStage++;
            if (growthStage == MAX_GROWTH) {
                state = CellState.READY;
            }
        }
    }

    public CellState getState() {
        return state;
    }

    public int getGrowthStage() {
        return growthStage;
    }
}
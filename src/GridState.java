import java.io.Serializable;

class GridState implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int size;
    private final CellState[][] cellStates;
    private final int[][] growthStages;

    public GridState(Grid grid) {
        this.size = grid.getSize();
        this.cellStates = new CellState[size][size];
        this.growthStages = new int[size][size];

        // Store the state of each cell
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = grid.getCell(i, j);
                cellStates[i][j] = cell.getState();
                growthStages[i][j] = cell.getGrowthStage();
            }
        }
    }

    public void applyTo(Grid grid) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Cell cell = grid.getCell(i, j);
                // Reconstruct cell state
                switch (cellStates[i][j]) {
                    case EMPTY:
                        cell.repair(); // This sets to EMPTY
                        break;
                    case GROWING:
                        cell.plant();
                        // Simulate growth to reach the saved stage
                        for (int k = 0; k < growthStages[i][j]; k++) {
                            cell.grow();
                        }
                        break;
                    case READY:
                        cell.plant();
                        // Grow to full
                        for (int k = 0; k < 5; k++) {
                            cell.grow();
                        }
                        break;
                    case DAMAGED:
                        cell.damage();
                        break;
                }
            }
        }
    }
}
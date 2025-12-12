package ui;

import grid.Cell;
import grid.CellState;
import grid.Grid;
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
                switch (cellStates[i][j]) {
                    case EMPTY:
                        cell.repair();
                        break;
                    case GROWING:
                        cell.plant();
                        for (int k = 0; k < growthStages[i][j]; k++) {
                            cell.grow();
                        }
                        break;
                    case READY:
                        cell.plant();
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
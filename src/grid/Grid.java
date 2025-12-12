package grid;

import entities.Entity;
import entities.Rabbit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Grid {
    private final int size;
    private final Cell[][] grid;
    private final List<Entity> entities;

    public Grid(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        this.entities = new CopyOnWriteArrayList<>();
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public int getSize() {
        return size;
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }

    public void clearEntities() {
        entities.clear();
    }

    public Rabbit findNearbyRabbit(int x, int y, int range) {
        for (Entity entity : entities) {
            if (entity instanceof Rabbit && entity.isActive()) {
                int[] pos = entity.getPosition();
                if (isInRange(x, y, pos[0], pos[1], range)) {
                    return (Rabbit) entity;
                }
            }
        }
        return null;
    }

    private boolean isInRange(int x1, int y1, int x2, int y2, int range) {
        return Math.abs(x2 - x1) <= range && Math.abs(y2 - y1) <= range;
    }

    public void updateGrowth() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j].grow();
            }
        }
    }
}
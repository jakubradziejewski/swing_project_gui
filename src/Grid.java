//import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.io.Serializable;

public class Grid implements Serializable{
    private final int size;
    private final Cell[][] grid;
    private transient List<Entity> entities;
    private static final long serialVersionUID = 1L;

    public Grid(int size) {
        this.size = size;
        this.grid = new Cell[size][size];
        this.entities = new CopyOnWriteArrayList<>();
        //this.random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = new Cell();
            }
        }
    }
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.entities = new CopyOnWriteArrayList<>();
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

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public Rabbit findNearbyRabbit(int x, int y, int range) {
        for (Entity entity : entities) {
            if (entity instanceof Rabbit && entity.isActive()) {
                int[] pos = entity.getPosition();
                int dx = Math.abs(pos[0] - x);
                int dy = Math.abs(pos[1] - y);
                if (dx <= range && dy <= range) {
                    return (Rabbit) entity;
                }
            }
        }
        return null;
    }

    public void updateGrowth() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j].grow();
            }
        }
    }
    public void clearEntities() {
        entities.clear();
    }

    public int getSize() {
        return size;
    }

    public List<Entity> getEntities() {
        return new ArrayList<>(entities);
    }
}
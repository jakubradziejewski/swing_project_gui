package core;

public abstract class GameObject {
    protected int x;
    protected int y;
    protected volatile boolean isActive;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }

    public int[] getPosition() {
        return new int[]{x, y};
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
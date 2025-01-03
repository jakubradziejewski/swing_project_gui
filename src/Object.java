public abstract class Object {
    protected int x;
    protected int y;
    protected boolean isActive;

    public Object(int x, int y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }

    public void move(int newX, int newY) {
        this.x = newX;
        this.y = newY;
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
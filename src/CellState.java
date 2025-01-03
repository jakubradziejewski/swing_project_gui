import java.io.Serializable;

public enum CellState implements Serializable{
    EMPTY,
    GROWING,
    READY,
    DAMAGED
}
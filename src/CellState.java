import java.io.Serializable;

// Enum representing possible cell states - carrot growth stages
public enum CellState implements Serializable{
    EMPTY,
    GROWING,
    READY,
    DAMAGED
}
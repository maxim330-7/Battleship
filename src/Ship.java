import java.util.ArrayList;
import java.util.List;

public class Ship {
    private final int size;
    private Cell firstCell;
    private final List<Cell> cells;
    private Orientation orientation = Orientation.HORIZONTAL;

    public Ship(int size) {
        this.size = size;
        this.cells = new ArrayList<>();
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getSize() {
        return size;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(Cell cell) {
        this.cells.add(cell);
    }

    public boolean isDestroyed() {
        for (Cell cell : cells) {
            if (cell.getStatus() != CellState.HIT) {
                return false;
            }
        }

        return true;
    }

    public Cell getFirstCell() {
        return firstCell;
    }

    public void setFirstCell(Cell firstCell) {
        this.firstCell = firstCell;
    }
}
public class Cell {
    private CellState status;
    private Ship ship;
    private final int col;
    private final int row;

    public Cell(int col, int row){
        status = CellState.EMPTY;
        this.col = col;
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Coordinate getCoordinate(){
        return new Coordinate(col, row);
    }

    public void setStatus(CellState status) {
        this.status = status;
    }

    public CellState getStatus() {
        return status;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
}

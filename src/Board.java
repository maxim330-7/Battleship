import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Cell[][] cells;
    private final int size;
    private final List<Ship> ships;

    public List<Ship> getShips() {
        return ships;
    }

    public Board(int size){
        cells = new Cell[size][size];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(j, i);
            }
        }
        ships = new ArrayList<>();
        createFleet();
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean placeShip(Ship ship, Orientation orientation, Coordinate coordinate){
        int row = coordinate.getRow();
        int col = coordinate.getCol();


        // Проверка границ
        if (orientation == Orientation.HORIZONTAL) {
            if (col + ship.getSize() > cells[0].length) {
                return false;
            }
        } else {
            if (row + ship.getSize() > cells.length) {
                return false;
            }
        }

        // Проверка свободных клеток
        for (int i = 0; i < ship.getSize(); i++) {

            int currentRow = row;
            int currentCol = col;

            if (orientation == Orientation.HORIZONTAL) {
                currentCol += i;
            } else {
                currentRow += i;
            }

            if (cells[currentRow][currentCol].getStatus() != CellState.EMPTY) {
                return false;
            }
        }

        ship.setOrientation(orientation);
        ship.setFirstCell(cells[row][col]);

        // Размещение корабля
        for (int i = 0; i < ship.getSize(); i++) {

            int currentRow = row;
            int currentCol = col;

            if (orientation == Orientation.HORIZONTAL) {
                currentCol += i;
            } else {
                currentRow += i;
            }

            cells[currentRow][currentCol].setStatus(CellState.SHIP);
            cells[currentRow][currentCol].setShip(ship);
            ship.setCells(cells[currentRow][currentCol]);
        }

        // Пометка соседних клеток
        neighborBlock(ship, orientation);

        return true;
    }

    private void neighborBlock(Ship ship, Orientation orientation){
        Coordinate coordinate = ship.getFirstCell().getCoordinate();
        int row = coordinate.getRow();
        int col = coordinate.getCol();
        for (int i = -1; i <= ship.getSize(); i++) {
            for (int j = -1; j <= 1; j++) {
                int r;
                int c;
                if (orientation == Orientation.HORIZONTAL) {
                    r = row + j;
                    c = col + i;
                } else {
                    r = row + i;
                    c = col + j;
                }
                if (r >= 0 &&
                        r < cells.length &&
                        c >= 0 &&
                        c < cells[0].length &&
                        cells[r][c].getStatus() == CellState.EMPTY) {

                    cells[r][c].setStatus(CellState.BLOCKED);
                }
            }
        }
    }


    public void print() {
        System.out.print("   ");
        for (int i = 0; i < cells.length; i++) {
            System.out.print((char)('A' + i) + " ");
        }
        System.out.println();

        for (int i = 0; i < cells.length; i++) {
            System.out.print((i + 1) < 10 ? " " + (i + 1) + " " : (i + 1) + " ");
            for (int j = 0; j < cells[i].length; j++) {
                System.out.print(switchState(cells[i][j].getStatus()) + " ");
            }
            System.out.println();
        }
    }

    public String boardToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");

        for (int i = 0; i < cells.length; i++) {
            sb.append((char) ('A' + i)).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < cells.length; i++) {
            if ((i + 1) < 10) {
                sb.append(" ").append(i + 1).append(" ");
            } else {
                sb.append(i + 1).append(" ");
            }
            for (int j = 0; j < cells[i].length; j++) {
                sb.append(switchState(cells[i][j].getStatus()))
                        .append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getSizeBoard(CellState cell){
        int count = 0;
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getStatus() == cell){
                    count++;
                }
            }
        }
        return count;
    }

    public CellState getCellState(int a, int b){
        return cells[a][b].getStatus();
    }

    public CellState getCellState(Coordinate coordinate){
        return cells[coordinate.getRow()][coordinate.getCol()].getStatus();
    }

    public Cell getCell(Coordinate coordinate){
        return cells[coordinate.getRow()][coordinate.getCol()];
    }

    public CellState shoot(Coordinate coordinate) {

        Cell cell = cells[coordinate.getRow()][coordinate.getCol()];
        System.out.println(cell.getStatus());

        if (cell.getStatus() == CellState.SHIP) {
            cell.setStatus(CellState.HIT);
            if (cell.getShip().isDestroyed()) {
                System.out.println("убит");
                return CellState.DEATH;
            }
            System.out.println("ранил");
            return CellState.HIT;

        }else if(cell.getStatus() == CellState.EMPTY){
            cell.setStatus(CellState.MISS);
            System.out.println("мимо");
            return CellState.MISS;
        }
        System.out.println("error");
        return cell.getStatus();
    }

    private static String switchState(CellState status){
        return switch (status){
            case EMPTY -> "~";
            case HIT, DEATH -> "x";
            case MISS, BLOCKED -> ".";
            case SHIP -> "@";
        };
    }

    private void createFleet() {
        ships.add(new Ship(6));

        ships.add(new Ship(5));
        ships.add(new Ship(5));

        ships.add(new Ship(4));
        ships.add(new Ship(4));
        ships.add(new Ship(4));

        ships.add(new Ship(3));
        ships.add(new Ship(3));
        ships.add(new Ship(3));
        ships.add(new Ship(3));

        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));
        ships.add(new Ship(2));

        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
        ships.add(new Ship(1));
    }

    public Ship takeShip(int size) {
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).getSize() == size) {
                return ships.remove(i);
            }
        }
        return null;
    }

    public void clearBlocked() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (cells[row][col].getStatus() == CellState.BLOCKED) {
                    cells[row][col].setStatus(CellState.EMPTY);
                }
            }
        }
    }

    public boolean hasShips() {
        return !ships.isEmpty();
    }

    public void setCellState(Coordinate coordinate, CellState state, Board shipBoard){
        Cell cell = cells[coordinate.getRow()][coordinate.getCol()];
        cell.setStatus(state);
        if (state == CellState.DEATH){
            Ship ship = shipBoard.getCell(coordinate).getShip();
            neighborBlock(ship, ship.getOrientation());
        }
    }

    public void setCellState(Coordinate coordinate, CellState state){
        Cell cell = cells[coordinate.getRow()][coordinate.getCol()];
        cell.setStatus(state);
        if (state == CellState.DEATH){
            Ship ship = cell.getShip();
            neighborBlock(ship, ship.getOrientation());
        }
    }


    public boolean allShipsDestroyed() {
        return getSizeBoard(CellState.SHIP) == 0;
    }

}

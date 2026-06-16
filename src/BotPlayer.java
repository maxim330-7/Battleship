import java.util.Random;

public class BotPlayer implements Player{
    private final Board board;
    private final Board boardView;
    private int shots = 0;
    private int hits = 0;
    private int misses = 0;
    private final Random random = new Random();

    public BotPlayer(Board board, Board boardView) {
        this.board = board;
        this.boardView = boardView;
    }

    public Board getBoardView() {
        return boardView;
    }

    public Board getBoard() {
        return board;
    }

    public String getName() {
        return "Бот";
    }

    public Coordinate move(){
        if (boardView.getSizeBoard(CellState.EMPTY) == boardView.getSize() * boardView.getSize()) {

            while (true) {

                int col = random.nextInt(boardView.getSize());
                int row = random.nextInt(boardView.getSize());

                if (boardView.getCellState(row, col) == CellState.EMPTY) {
                    return new Coordinate(col, row);
                }
            }
        }

        for (int row = 0; row < boardView.getSize(); row++) {
            for (int col = 0; col < boardView.getSize(); col++) {
                if (boardView.getCellState(row, col) != CellState.HIT) {
                    continue;
                }

                // горизонталь
                if (col > 0 && boardView.getCellState(row, col - 1) == CellState.HIT) {
                    if (col + 1 < boardView.getSize() &&
                            boardView.getCellState(row, col + 1) == CellState.EMPTY) {
                        return new Coordinate(col + 1, row);
                    }

                    int left = col - 1;
                    while (left > 0 && boardView.getCellState(row, left - 1) == CellState.HIT) {
                        left--;
                    }

                    if (left > 0 && boardView.getCellState(row, left - 1) == CellState.EMPTY) {
                        return new Coordinate(left - 1, row);
                    }
                }

                // вертикаль

                if (row > 0 && boardView.getCellState(row - 1, col) == CellState.HIT) {
                    if (row + 1 < boardView.getSize() && boardView.getCellState(row + 1, col) == CellState.EMPTY) {
                        return new Coordinate(col, row + 1);
                    }

                    int top = row - 1;
                    while (top > 0 && boardView.getCellState(top - 1, col) == CellState.HIT) {
                        top--;
                    }

                    if (top > 0 && boardView.getCellState(top - 1, col) == CellState.EMPTY) {
                        return new Coordinate(col, top - 1);
                    }
                }

                // одно попадание
                if (row > 0 && boardView.getCellState(row - 1, col) == CellState.EMPTY) {
                    return new Coordinate(col, row - 1);
                }

                if (row < boardView.getSize() - 1 && boardView.getCellState(row + 1, col) == CellState.EMPTY) {
                    return new Coordinate(col, row + 1);
                }

                if (col > 0 && boardView.getCellState(row, col - 1) == CellState.EMPTY) {
                    return new Coordinate(col - 1, row);
                }

                if (col < boardView.getSize() - 1 && boardView.getCellState(row, col + 1) == CellState.EMPTY) {
                    return new Coordinate(col + 1, row);
                }
            }
        }

        // Шахматный поиск
        if (hasCheckerCells()) {
            while (true) {

                int col = random.nextInt(boardView.getSize());
                int row = random.nextInt(boardView.getSize());

                if ((row + col) % 2 == 0 && boardView.getCellState(row, col) == CellState.EMPTY) {
                    return new Coordinate(col, row);
                }
            }
        } else {

            while (true) {

                int col = random.nextInt(boardView.getSize());
                int row = random.nextInt(boardView.getSize());

                if (boardView.getCellState(row, col) == CellState.EMPTY) {
                    return new Coordinate(col, row);
                }
            }
        }

    }

    @Override
    public Orientation selectionPosition(){
        int result = random.nextInt(2);
        if (result == 0){
            return Orientation.VERTICAL;
        }else{
            return Orientation.HORIZONTAL;
        }
    }

    private boolean hasCheckerCells() {

        for (int row = 0; row < boardView.getSize(); row++) {
            for (int col = 0; col < boardView.getSize(); col++) {

                if ((row + col) % 2 == 0 &&
                        boardView.getCellState(row, col) == CellState.EMPTY) {
                    return true;
                }
            }
        }

        return false;
    }

    public int getMisses() {
        return misses;
    }

    public int getHits() {
        return hits;
    }

    public int getShots() {
        return shots;
    }

    public double getAccuracy() {
        if (shots == 0) {
            return 0;
        }
        return hits * 100.0 / shots;
    }

    public void addShot() {
        shots++;
    }

    public void addHit() {
        hits++;
    }

    public void addMisses() {
        misses++;
    }
}

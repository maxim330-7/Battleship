import java.util.Scanner;

public class HumanPlayer implements Player{
    private final String name;
    private final Board board;
    private final Board boardView;
    private int shots = 0;
    private int hits = 0;
    private int misses = 0;
    Scanner sc = new Scanner(System.in);

    public HumanPlayer(String name, Board board, Board boardView){
        this.name = name;
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
        return name;
    }

    @Override
    public Coordinate move(){
        System.out.println("Введите координаты (БУКВА - столбец), (ЦИФРА - строка)");
        while (true){
            String col = sc.nextLine().trim();

            int row;
            try {
                row = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка! Введите число.");
                continue;
            }

            Coordinate result = new Coordinate(col, row);
            if (result.getRow() >= board.getSize() ||
                    result.getCol() >= board.getSize() ||
                    result.getRow() < 0 ||
                    result.getCol() < 0
            ){
                System.out.println("Ошибка! Попробуйте ещё раз.");
            } else {
                return result;
            }
        }
    }

    @Override
    public Orientation selectionPosition() {

        while (true) {
            System.out.println("Выберите положение корабля (H-горизонтальное, V-вертикальное)");

            String position = sc.nextLine();
            if (position.equalsIgnoreCase("H")) {
                return Orientation.HORIZONTAL;
            }

            if (position.equalsIgnoreCase("V")) {
                return Orientation.VERTICAL;
            }

            System.out.println("Ошибка! Попробуйте ещё раз");
        }
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

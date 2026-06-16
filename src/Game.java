import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Game {

    private final String nameGame;
    private final Player player1;
    private final Player player2;
    Scanner sc = new Scanner(System.in);

    public Game(Player player1, Player player2) {
        this.nameGame = player1.getName() + " vs " + player2.getName();
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startGame() throws IOException {
        String nameFile = generateGameName();
        GameLogger.createFile(nameFile);
        String file = GameLogger.getNameFile(nameFile);

        separator("#", 100);
        System.out.println("Игра: " + nameGame);
        GameLogger.outputFail(file, "Игра: " + nameGame + "\n");
        for (Player player : new Player[]{player1, player2}) {
            while (true) {
                separator("=", 80);
                System.out.println("\nИгрок " + player.getName());
                System.out.println(player.getName().equals("Бот") ? "" : "\nРасстановка короблей\n Самостоятельно (0) или автоматически (1)?");
                int choice = player.getName().equals("Бот") ? 1 : sc.nextInt();
                sc.nextLine();

                if (choice == 0) {
                    shipPlacement(player);
                    break;
                }
                if (choice == 1) {
                    autoShipPlacement(player);
                    break;
                }

                System.out.println(player.getName().equals("Бот") ? "" : "Неверный ввод!");
            }
        }

        Player currentPlayer = player1;
        Player enemyPlayer = player2;

        while (true) {
            separator("=", 80);
            System.out.println("\nХод игрока: " + currentPlayer.getName());

            Coordinate shot;
            CellState result = null;
            do {
                System.out.println("Ваше поле:");
                currentPlayer.getBoard().print();
                System.out.println("Поле противника:");
                currentPlayer.getBoardView().print();

                shot = currentPlayer.move();
                currentPlayer.addShot();

                if (currentPlayer.getBoardView().getCellState(shot.getRow(), shot.getCol())
                        != CellState.EMPTY) {
                    System.out.println(currentPlayer.getName().equals("Бот") ? "" : "Вы уже стреляли сюда!");
                    continue;
                }

                result = enemyPlayer.getBoard().shoot(shot);
                String shotLetter = shot.getLetterCol() + String.valueOf(shot.getNumberRow());
                GameLogger.logMove(file, currentPlayer.getName(), shotLetter, switchStateLetter(result));

                if (result == CellState.HIT || result == CellState.DEATH){
                    currentPlayer.addHit();
                }else if(result == CellState.MISS){
                    currentPlayer.addMisses();
                }

                if (enemyPlayer.getBoard().allShipsDestroyed()) {
                    System.out.println("\nПобедил " + currentPlayer.getName());
                    String titleCurrent = "Поле игрока " + currentPlayer.getName() + ":";
                    String titleEnemy = "Поле игрока " + enemyPlayer.getName() + ":";
                    GameLogger.logBoard(file, titleCurrent, currentPlayer.getBoard());
                    GameLogger.logBoard(file, titleEnemy, enemyPlayer.getBoard());
                    GameLogger.logStats(file, currentPlayer);
                    GameLogger.logStats(file, enemyPlayer);
                    return;
                }

                separator("-", 60);
                currentPlayer.getBoardView().setCellState(shot, result, enemyPlayer.getBoard());
                enemyPlayer.getBoard().setCellState(shot, result);
                System.out.println("Ваше поле:");
                currentPlayer.getBoard().print();
                System.out.println("Поле противника:");
                currentPlayer.getBoardView().print();

                separator("-", 60);
                System.out.println("Ход игрока " + currentPlayer.getName() + " : "
                        + shot.getLetterCol() + shot.getNumberRow() + " - "
                        + switchStateLetter(result));
                separator("-", 60);
            } while (result != CellState.MISS);

            if (currentPlayer.getName().equals("Бот")) {
                clearScreenAuto();
            } else {
                clearScreen();
            }
            Player temp = currentPlayer;
            currentPlayer = enemyPlayer;
            enemyPlayer = temp;
            GameLogger.outputFail(file, "\n-------------------------------------------\n");
        }
    }

    public void shipPlacement(Player player) {
        Board board = player.getBoard();
        System.out.println("\nИгрок " + player.getName() + " расставляет корабли");
        while (board.hasShips()) {
            separator("-", 60);
            board.print();
            printShips(board);
            System.out.print("\nВведите размер корабля: ");
            int size = sc.nextInt();
            sc.nextLine();
            Ship ship = board.takeShip(size);
            if (ship == null) {
                System.out.println("Ошибка! Попробуйте ещё раз.");
                continue;
            }
            boolean placed = false;
            while (!placed) {
                Orientation orientation;
                if (size == 1){
                    orientation = Orientation.HORIZONTAL;
                }else{
                    orientation = player.selectionPosition();
                }
                Coordinate coordinate = player.move();
                placed = board.placeShip(ship, orientation, coordinate);
                if (!placed) {
                    System.out.println("Неверное размещение!");
                    System.out.println("Введите координаты ещё раз.");
                }
            }
        }
        System.out.println("\nКорабли расставлены!");
        board.print();
        board.clearBlocked();
        clearScreen();
    }

    private void printShips(Board board) {
        int[] count = new int[7];
        for (Ship ship : board.getShips()) {
            count[ship.getSize()]++;
        }
        System.out.println("Осталось:");
        for (int size = 6; size >= 1; size--) {
            if (count[size] > 0) {
                System.out.println(size + " палубных - " + count[size] + " шт.");
            }
        }
    }

    public void autoShipPlacement(Player player) {
        Board board = player.getBoard();

        for (Ship ship : board.getShips()) {
            boolean placed = false;
            while (!placed) {
                int row = (int)(Math.random() * board.getSize());
                int col = (int)(Math.random() * board.getSize());

                Orientation orientation = Math.random() < 0.5 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
                placed = board.placeShip(ship, orientation, new Coordinate(col, row));
            }
        }
        System.out.println("\nАвторасстановка для " + player.getName());
        board.clearBlocked();
        board.print();
        if (player.getName().equals("Бот")){
            clearScreenAuto();
        }else{
            clearScreen();
        }
    }

    private static String switchStateLetter(CellState status){
        return switch (status){
            case HIT -> "попал";
            case MISS -> "мимо";
            case DEATH -> "убил";
            default -> "error!";
        };
    }

    private void clearScreen() {
        System.out.print("\nНажмите Enter и передайте ход следующему игроку");
        sc.nextLine();
        for (int i = 0; i < 1000; i++) {
            System.out.println();
        }
    }

    private void clearScreenAuto() {
        for (int i = 0; i < 1000; i++) {
            System.out.println();
        }
    }

    private String generateGameName() {
        int count = 1;
        while (new File("gameList", "game_" + count + ".txt").exists()) {
            count++;
        }

        return "game_" + count;
    }

    public String getNameGame() {
        return nameGame;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    private void separator(String separator, int size){
        System.out.println();
        for (int i = 0; i < size; i++) {
            System.out.print(separator);
        }
        System.out.println();
    }
}
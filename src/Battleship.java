import java.io.IOException;
import java.util.Scanner;

public class Battleship {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("----МОРСКОЙ БОЙ----");
        while (true){
            System.out.println("Режим игры: ");
            System.out.println("0 - с ботом");
            System.out.println("1 - с напарником");
            System.out.println("2 - два бота");
            System.out.println("3 - выйти из игры");
            int mode = sc.nextInt();

            Board poleH1 = new Board(16);
            Board poleH2 = new Board(16);
            Board poleB1 = new Board(16);
            Board poleB2 = new Board(16);

            switch (mode){
                case 0:
                    String humanName = printName();
                    if (humanName.equals("admin")){
                        adminMode();
                        break;
                    }
                    Player human = new HumanPlayer(humanName, poleH1, poleH2);
                    Player bot = new BotPlayer(poleB1, poleB2);
                    Game gameHumanBot = new Game(human, bot);
                    gameHumanBot.startGame();
                    break;
                case 1:
                    System.out.println("Регистрация игрока 1");
                    String player1Name = printName();
                    if (player1Name.equals("admin")){
                        adminMode();
                        break;
                    }
                    System.out.println("Регистрация игрока 2");
                    String player2Name = printName();
                    if (player2Name.equals("admin")){
                        adminMode();
                        break;
                    }
                    Player player1 = new HumanPlayer(player1Name, poleH1, poleH2);
                    Player player2 = new HumanPlayer(player2Name, poleB1, poleB2);
                    Game gameP2P = new Game(player1, player2);
                    gameP2P.startGame();
                    break;
                case 2:
                    Player bot1 = new BotPlayer(poleB1, poleB2);
                    Player bot2 = new BotPlayer(poleH1, poleH2);
                    Game gameBots = new Game(bot1, bot2);
                    gameBots.startGame();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Неверный ввод! Попробуйте ещё раз");
                    break;
            }
        }


    }

    private static String printName(){
        Scanner sc = new Scanner(System.in);
        String name;
        while (true){
            System.out.println("Введите ваше имя: ");
            name = sc.nextLine();
            if (name.equals("Бот")){
                name = "Бот ";
            }
            return name;
        }
    }

    private static void adminMode(){
        System.out.println("Активирован режим администратора");
        Scanner sc = new Scanner(System.in);
        helpAdmin();

        while (true) {

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("Список игр:");
                    GameLogger.printListGame();
                    helpAdmin();
                    break;

                case 2:
                    System.out.println("Список архивированных игр:");
                    GameLogger.printArchiveListGame();
                    helpAdmin();
                    break;

                case 3:
                    System.out.println("Введите имя файла:");
                    String viewFile = sc.nextLine();
                    GameLogger.readFile("gameList/" + viewFile);
                    helpAdmin();
                    break;

                case 4:
                    System.out.println("Введите имя файла:");
                    String archiveFile = sc.nextLine();
                    GameLogger.archiveGame(archiveFile);
                    break;

                case 5:
                    System.out.println("Введите имя файла:");
                    String restoreFile = sc.nextLine();
                    GameLogger.restoreGame(restoreFile);
                    break;

                case 6:
                    System.out.println("Введите имя файла:");
                    String deleteFile = sc.nextLine();
                    GameLogger.deleteGame(deleteFile);
                    break;

                case 0:
                    return;

                default:
                    System.out.println("Неверный ввод");
            }
        }
    }

    private static void helpAdmin(){
        System.out.println("1 - Список игр");
        System.out.println("2 - Список архива");
        System.out.println("3 - Просмотреть игру");
        System.out.println("4 - Архивировать игру");
        System.out.println("5 - Восстановить игру");
        System.out.println("6 - Удалить игру");
        System.out.println("0 - Выход");
    }

}

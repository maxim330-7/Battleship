import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class GameLogger {
    private static final File dir = new File("gameList");
    private static final File dirArchive = new File("gameListArchive");

    public static void createFile(String nameFile) throws IOException {
        if (!dir.exists()) dir.mkdir();
        File f = new File(dir, nameFile +".txt");
        f.createNewFile();
    }

    public static void outputFail(String file, String text){
        byte[] buffer = text.getBytes();
        try (FileOutputStream stream = new FileOutputStream(file, true)){
            for (byte eachBufferElement : buffer) {
                stream.write(eachBufferElement);
            }
        } catch (IOException exception){
            System.out.println("Input / Output error");
        }
    }

    public static void logStats(String fileName, Player player) throws IOException {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write("\n");
            writer.write("Статистика\n");
            writer.write("Игрок: " + player.getName() + "\n");
            writer.write("Выстрелов: " + player.getShots() + "\n");
            writer.write("Попаданий: " + player.getHits() + "\n");
            writer.write("Промахов: " + player.getMisses() + "\n");
            writer.write(String.format("Точность: %.2f%%\n", player.getAccuracy()));
        }
    }

    public static void logBoard(String file, String title, Board board) {

        String text = "\n" + title + "\n" + board.boardToString() + "\n";

        try (FileOutputStream stream = new FileOutputStream(file, true)) {
            stream.write(text.getBytes());
        } catch (IOException exception) {
            System.out.println("Error");
        }
    }

    public static void readFile(String file){
        try (FileReader reader = new FileReader(file)){
            int symbol;

            while ((symbol = reader.read()) != -1){
                System.out.print((char) symbol);
            }
        } catch (IOException exception){
            System.out.println("Error");
        }
    }

    public static void printListGame() {
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String firstLine = reader.readLine();
                System.out.println(f.getName() + " - " + (firstLine != null ? firstLine : "без названия"));
            } catch (IOException e) {
                System.out.println(f.getName() + " - ошибка чтения");
            }
        }
    }

    public static void printArchiveListGame(){
        for (File f : Objects.requireNonNull(dirArchive.listFiles())) {
            try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
                String firstLine = reader.readLine();
                System.out.println(f.getName() + " - " + (firstLine != null ? firstLine : "без названия"));
            } catch (IOException e) {
                System.out.println(f.getName() + " - ошибка чтения");
            }
        }
    }

    public static void archiveGame(String nameFile){
        String pathOld = dir.getPath() + "/" + nameFile;
        String pathNew = dirArchive.getPath() + "/" + nameFile;
        try(FileInputStream inputStream = new FileInputStream(pathOld)){
            if (!dirArchive.exists()) dirArchive.mkdir();

            try(FileOutputStream outputStream = new FileOutputStream(pathNew)){
                byte[] bytes = new byte[inputStream.available()];
                int length;

                while ((length = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes, 0, length);
                }
            }catch (IOException exception){
                System.out.println("Error");
            }
        }catch (IOException exception){
            System.out.println("Error");
        }

        File f = new File(pathOld);
        f.delete();
        System.out.println("The game has been archived.");
    }

    public static void logMove(String file, String player, String shot, String result) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        String line = "[" + LocalDateTime.now().format(formatter) + "] "
                        + player + " -> "
                        + shot + " -> "
                        + result + System.lineSeparator();

        try (FileOutputStream stream = new FileOutputStream(file, true)) {
            stream.write(line.getBytes());
        } catch (IOException exception) {
            System.out.println("Input / Output error");
        }
    }

    public static void deleteGame(String name) {

        File file = new File(dir, name);

        if (!file.exists()) {
            System.out.println("File not found");
            return;
        }

        if (file.delete()) {
            System.out.println("Game deleted");
        } else {
            System.out.println("Delete error");
        }
    }

    public static void restoreGame(String name) {

        File archiveFile = new File(dirArchive, name);
        File restoreFile = new File(dir, name);

        if (!archiveFile.exists()) {
            System.out.println("Archive file not found");
            return;
        }

        try (FileInputStream inputStream = new FileInputStream(archiveFile);
             FileOutputStream outputStream = new FileOutputStream(restoreFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException exception) {
            System.out.println("Restore error");
            return;
        }
        if (archiveFile.delete()) {
            System.out.println("Game restored");
        }
    }

    public static String getNameFile(String nameGame){
        String nameFile = nameGame + ".txt";
        return dir.getPath() + "/" + nameFile;
    }

}

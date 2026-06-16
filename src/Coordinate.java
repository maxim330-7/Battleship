public class Coordinate {
    private final int col;
    private final int row;
    private final char letterCol;
    private final int numberRow;

    public Coordinate(String col, int row) {
        this.row = row-1;
        char letter = col.toUpperCase().charAt(0);
        this.col = Character.toUpperCase(letter) - 'A';
        this.letterCol = col.charAt(0);
        this.numberRow = row;
    }
    public Coordinate(int col, int row) {
        this.row = row;
        this.col = col;
        this.letterCol = (char) ('A' + col);
        this.numberRow = row + 1;
    }

    public int getCol() {
        return col;
    }

    public int getRow(){
        return row;
    }

    public char getLetterCol() {
        return letterCol;
    }

    public int getNumberRow() {
        return numberRow;
    }
}

public interface Player {

    Coordinate move();

    Orientation selectionPosition();

    String getName();

    Board getBoard();

    Board getBoardView();

    void addShot();

    void addHit();

    void addMisses();

    int getShots();

    int getHits();

    int getMisses();

    double getAccuracy();

}

public enum CellState{
    EMPTY, //пусто
    SHIP, //хранит корабль
    BLOCKED, //крайняя к короблю
    HIT, //попал
    MISS, //промах
    DEATH //убил
}
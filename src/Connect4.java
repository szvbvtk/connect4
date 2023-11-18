import sac.game.GameState;
import sac.game.GameStateImpl;

import java.util.List;

public class Connect4 extends GameStateImpl {
    public int numberOfRows;
    public int numberOfColumns;

    final char[][] board;

    public Connect4(int numberOfRows, int numberOfColumns) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.board = new char[numberOfRows][numberOfColumns];
    }


    public void makeMove(int c) {
//        jezeli ruch poprawny
        maximizingTurnNow = !maximizingTurnNow;
    }

    @Override
    public List<GameState> generateChildren() {
        return null;
    }
}

//do zrobienia zad 3.1 strona 72 w pdf
//w konstruktorze kopiujacym kopiowac flage maximizingturnnow
//heurystyka czy wygrany zwracac + i - nieskonczonosc
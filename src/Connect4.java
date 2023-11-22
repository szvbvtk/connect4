import sac.game.GameState;
import sac.game.GameStateImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Connect4 extends GameStateImpl {
    static public final int numberOfRows = 7;
    static public final int numberOfColumns = 6;
    final char[][] board;

    enum Tokens {
        EMPTY('.'),
        PLAYER('O'),
        AI_PLAYER('X');

        private final char token;

        Tokens(char token) {
            this.token = token;
        }

        public char getToken() {
            return token;
        }
    }

    public Connect4(char token) {

        if (token == Tokens.PLAYER.getToken()) {
            this.maximizingTurnNow = true;
        } else if (token == Tokens.AI_PLAYER.getToken()) {
            this.maximizingTurnNow = false;
        }

        this.board = new char[numberOfRows][numberOfColumns];

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                this.board[i][j] = Tokens.EMPTY.getToken();
            }
        }

    }

    public Connect4(Connect4 parent) {
        this.board = new char[numberOfRows][numberOfColumns];
        this.maximizingTurnNow = parent.maximizingTurnNow;

        for (int i = 0; i < numberOfRows; i++) {
            System.arraycopy(parent.board[i], 0, this.board[i], 0, numberOfColumns);
        }
    }

    public static Connect4 createGame() {
        System.out.print("Choose starting player: O - Player, X - AI: ");
        Scanner scanner = new Scanner(System.in);
        char token = scanner.next().charAt(0);
        if (token == 'X' || token == 'x') {
            return new Connect4(Tokens.AI_PLAYER.getToken());
        }
        return new Connect4(Tokens.PLAYER.getToken());
    }

    public boolean makeMove(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= numberOfColumns) {
            return false;
        }

        int row = numberOfRows - 1;
        while (row >= 0 && board[row][columnIndex] != Tokens.EMPTY.getToken()) {
            row--;
        }

        if (row > -1) {
            this.board[row][columnIndex] = maximizingTurnNow ? Tokens.PLAYER.getToken() : Tokens.AI_PLAYER.getToken();
            maximizingTurnNow = !maximizingTurnNow;
            return true;
        }

        return false;
    }

    @Override
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();

        for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            Connect4 child = new Connect4(this);
            if (child.makeMove(columnIndex)) {
                child.setMoveName(String.valueOf(columnIndex));
                children.add(child);
            }
        }

        return children;
    }

    public int checkGameResult() {
        if(countTokensInARow(Tokens.AI_PLAYER, 4, true) == 1) {
            return -1;
        } else if(countTokensInARow(Tokens.PLAYER, 4, true) == 1) {
            return 1;
        } else if(isBoardFull()) {
            return 2;
        }else{
            return 0;
        }
    }

    public boolean isBoardFull() {
        for(int i = 0; i < numberOfColumns; i++) {
            if(board[0][i] == Tokens.EMPTY.getToken()) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfRows; i++) {
            if (i == 0) {
                sb.append("  ");
                for (int j = 0; j < numberOfColumns; j++) {
                    sb.append(j).append("   ");
                }
                sb.append("\n");
            }
            sb.append("| ");
            for (int j = 0; j < numberOfColumns; j++) {

                if (board[i][j] == Tokens.EMPTY.getToken())
                    sb.append(board[i][j]).append(" | ");
                else if (board[i][j] == Tokens.PLAYER.getToken())
                    sb.append("\u001B[32m").append(board[i][j]).append("\u001B[0m | ");
                else if (board[i][j] == Tokens.AI_PLAYER.getToken())
                    sb.append("\u001B[31m").append(board[i][j]).append("\u001B[0m | ");
            }
            sb.append("\n");
        }
        sb.append("\"".repeat(4 * numberOfColumns + 1));
        return sb.toString();
    }

    double countTokensInARow(Tokens playerToken, int length, boolean firstOccuranceOnly) {
        int count = 0;

        // Sprawdzanie w poziomie
        for (int i = 0; i < Connect4.numberOfRows; i++) {
            for (int j = 0; j <= Connect4.numberOfColumns - length; j++) {
                boolean isMatch = true;
                for (int k = 0; k < length; k++) {
                    if (this.board[i][j + k] != playerToken.getToken()) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    if (firstOccuranceOnly) {
                        return 1;
                    }
                    count++;
                }
            }
        }

        // Sprawdzanie w pionie
        for (int i = 0; i <= Connect4.numberOfRows - length; i++) {
            for (int j = 0; j < Connect4.numberOfColumns; j++) {
                boolean isMatch = true;
                for (int k = 0; k < length; k++) {
                    if (this.board[i + k][j] != playerToken.getToken()) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    if (firstOccuranceOnly) {
                        return 1;
                    }
                    count++;
                }
            }
        }

        // Sprawdzanie na ukos
        for (int i = 0; i <= Connect4.numberOfRows - length; i++) {
            for (int j = 0; j <= Connect4.numberOfColumns - length; j++) {
                boolean isMatch = true;
                for (int k = 0; k < length; k++) {
                    if (this.board[i + k][j + k] != playerToken.getToken()) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    if (firstOccuranceOnly) {
                        return 1;
                    }
                    count++;
                }
            }
        }

        // Sprawdzanie na ukos (odwrotnie)
        for (int i = length - 1; i < Connect4.numberOfRows; i++) {
            for (int j = 0; j <= Connect4.numberOfColumns - length; j++) {
                boolean isMatch = true;
                for (int k = 0; k < length; k++) {
                    if (this.board[i - k][j + k] != playerToken.getToken()) {
                        isMatch = false;
                        break;
                    }
                }
                if (isMatch) {
                    if (firstOccuranceOnly) {
                        return 1;
                    }
                    count++;
                }
            }
        }

        return count;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }
}

//do zrobienia zad 3.1 strona 72 w pdf
//w konstruktorze kopiujacym kopiowac flage maximizingturnnow
//heurystyka czy wygrany zwracac + i - nieskonczonosc
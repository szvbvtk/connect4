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
        if(countTokensInARow(Tokens.AI_PLAYER, 4) > 0) {
            return -1;
        } else if(countTokensInARow(Tokens.PLAYER, 4) > 0) {
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

    public boolean checkHorizontal() {
        for (int i = 0; i < Connect4.numberOfRows; i++) {
            int counterPlayer = 0;
            int counterAI = 0;
            for (int j = 0; j < Connect4.numberOfColumns; j++) {
                if (this.board[i][j] == Connect4.Tokens.PLAYER.getToken()) {
                    counterPlayer++;
                    counterAI = 0;
                } else if (this.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    counterAI++;
                    counterPlayer = 0;
                } else {
                    counterPlayer = 0;
                    counterAI = 0;
                }


                if (counterPlayer == 4) {
//                    System.out.println("horizontal");
                    return true;
                } else if (counterAI == 4) {
//                    System.out.println("horizontal ai");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkVertical() {

        for (int i = 0; i < Connect4.numberOfColumns; i++) {
            int counterPlayer = 0;
            int counterAI = 0;
            for (int j = 0; j < Connect4.numberOfRows; j++) {
                if (this.board[j][i] == Connect4.Tokens.PLAYER.getToken()) {
                    counterPlayer++;
                    counterAI = 0;
                } else if (this.board[j][i] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    counterAI++;
                    counterPlayer = 0;
                } else {
                    counterPlayer = 0;
                    counterAI = 0;
                }

                if (counterPlayer == 4) {
//                    System.out.println("vertical");
                    return true;
                } else if (counterAI == 4) {
//                    System.out.println("vertical ai");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkDiagonal() {
        // Sprawdź ukosy od lewej górnej do prawej dolnej
        for (int i = 0; i < Connect4.numberOfRows - 3; i++) {
            for (int j = 0; j < Connect4.numberOfColumns - 3; j++) {
                if (this.board[i][j] == Connect4.Tokens.PLAYER.getToken() &&
                        this.board[i + 1][j + 1] == Connect4.Tokens.PLAYER.getToken() &&
                        this.board[i + 2][j + 2] == Connect4.Tokens.PLAYER.getToken() &&
                        this.board[i + 3][j + 3] == Connect4.Tokens.PLAYER.getToken()) {
                    return true;
                } else if (this.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        this.board[i + 1][j + 1] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        this.board[i + 2][j + 2] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        this.board[i + 3][j + 3] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    return true;
                }
            }
        }

        // Sprawdź ukosy od lewej dolnej do prawej górnej
        for (int i = 3; i < Connect4.numberOfRows; i++) {
            for (int j = 0; j < Connect4.numberOfColumns - 3; j++) {
                if (this.board[i][j] == Connect4.Tokens.PLAYER.getToken() &&
                        this.board[i - 1][j + 1] == Connect4.Tokens.PLAYER.getToken() &&
                        this.board[i - 2][j + 2] == Connect4.Tokens.PLAYER.getToken() &&
                        this.board[i - 3][j + 3] == Connect4.Tokens.PLAYER.getToken()) {
                    return true;
                } else if (this.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        this.board[i - 1][j + 1] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        this.board[i - 2][j + 2] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        this.board[i - 3][j + 3] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    return true;
                }
            }
        }

        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfRows; i++) {
            if (i == 0) {
                sb.append("  ");
                for (int j = 0; j < numberOfColumns; j++) {
                    sb.append(String.valueOf(j)).append("   ");
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

    double countTokensInARow(Tokens playerToken, int length) {
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
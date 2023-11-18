import sac.game.GameState;
import sac.game.GameStateImpl;

import java.util.ArrayList;
import java.util.List;

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

    public Connect4() {
//maximizingTurnNow = false;
        this.board = new char[numberOfRows][numberOfColumns];

        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++) {
                this.board[i][j] = Tokens.EMPTY.getToken();
            }
        }

    }
    public Connect4(Connect4 parent) {
        this.board = new char[numberOfRows][numberOfColumns];
        this.maximizingTurnNow = parent.maximizingTurnNow;

        for(int i = 0; i < numberOfRows; i++) {
            System.arraycopy(parent.board[i], 0, this.board[i], 0, numberOfColumns);
        }
    }


    // na razie boolean, byc moze zbedne, wtedy zmienic na void
    public boolean makeMove(int columnIndex) {
        int row = numberOfRows - 1;
        while(row >= 0 && board[row][columnIndex] != Tokens.EMPTY.getToken()) {
            row--;
        }

        if(row > -1) {
            this.board[row][columnIndex] = maximizingTurnNow ? Tokens.PLAYER.getToken() : Tokens.AI_PLAYER.getToken();
            maximizingTurnNow = !maximizingTurnNow;
            return true;
        }

        return false;
    }

    @Override
    public List<GameState> generateChildren() {
        List<GameState> children = new ArrayList<>();

        for(int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
            Connect4 child = new Connect4(this);
            if(child.makeMove(columnIndex)) {
//                child.setMoveName("col" + columnIndex);
                child.setMoveName(String.valueOf(columnIndex));
                children.add(child);
            }
        }

        return children;
    }

    public boolean checkWin() {
        return checkHorizontal() || checkVertical() || checkDiagonal();
    }
    
    public boolean checkHorizontal() {
        int counterPlayer = 0;
        int counterAI = 0;

        for(int i = 0; i < Connect4.numberOfRows; i++) {
            for(int j = 0; j < Connect4.numberOfColumns; j++) {
                if(this.board[i][j] == Connect4.Tokens.PLAYER.getToken()) {
                    counterPlayer++;
                } else {
                    counterPlayer = 0;
                }

                if(this.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    counterAI++;
                } else {
                    counterAI = 0;
                }

                if(counterPlayer == 4) {
                    return true;
                } else if (counterAI == 4) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean checkVertical() {
        int counterPlayer = 0;
        int counterAI = 0;

        for(int i = 0; i < Connect4.numberOfColumns; i++) {
            for(int j = 0; j < Connect4.numberOfRows; j++) {
                if(this.board[j][i] == Connect4.Tokens.PLAYER.getToken()) {
                    counterPlayer++;
                } else {
                    counterPlayer = 0;
                }

                if(this.board[j][i] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    counterAI++;
                } else {
                    counterAI = 0;
                }

                if(counterPlayer == 4) {
                    return true;
                } else if (counterAI == 4) {
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
        for(int i = 0; i < numberOfRows; i++) {
            sb.append("| ");
            for(int j = 0; j < numberOfColumns; j++) {
                sb.append(board[i][j]).append(" | ");
            }
            sb.append("\n");
        }
        sb.append("\"".repeat(4 * numberOfColumns + 1));
        return sb.toString();
    }

    public int hashCode() {
        return this.toString().hashCode();
    }
}

//do zrobienia zad 3.1 strona 72 w pdf
//w konstruktorze kopiujacym kopiowac flage maximizingturnnow
//heurystyka czy wygrany zwracac + i - nieskonczonosc
import sac.State;
import sac.StateFunction;

public class Connect4Heuristic extends StateFunction {
    @Override
    public double calculate(State state) {
        Connect4 c4game = (Connect4) state;
//        System.out.println("sskk");
        double score = 0;

//        double horizontalScore = checkHorizontal(c4game);
//        double verticalScore = checkVertical(c4game);
//        double diagonalScore = checkDiagonal(c4game);
//
//        score = horizontalScore + verticalScore + diagonalScore;

        if (c4game.checkWin() && !c4game.isMaximizingTurnNow()) { // być może trzeba zamienić ze sobą dwa returny
            return Double.POSITIVE_INFINITY;
        } else if (c4game.checkWin() && c4game.isMaximizingTurnNow()) {
            return Double.NEGATIVE_INFINITY;
        }

        double playerThreeInARow = countTokensInARow(c4game, Connect4.Tokens.PLAYER, 3);
        double aiThreeInARow = countTokensInARow(c4game, Connect4.Tokens.AI_PLAYER, 3);
        double playerTwoInARow = countTokensInARow(c4game, Connect4.Tokens.PLAYER, 2);
        double aiTwoInARow = countTokensInARow(c4game, Connect4.Tokens.AI_PLAYER, 2);
        double playerOneInARow = countTokensInARow(c4game, Connect4.Tokens.PLAYER, 1);
        double aiOneInARow = countTokensInARow(c4game, Connect4.Tokens.AI_PLAYER, 1);

        score =  -((aiThreeInARow * 100) - (playerThreeInARow * 100) + (aiTwoInARow * 10) - (playerTwoInARow * 10) + (aiOneInARow) - (playerOneInARow));
//        score = 0;
        return score;
    }

    public double checkHorizontal(Connect4 c4game) {
        double score = 0;
        int counterPlayer = 0;
        int counterAI = 0;

        for (int i = 0; i < Connect4.numberOfRows; i++) {
            for (int j = 0; j < Connect4.numberOfColumns; j++) {
                if (c4game.board[i][j] == Connect4.Tokens.PLAYER.getToken()) {
                    counterPlayer++;
                } else {
                    counterPlayer = 0;
                }

                if (c4game.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    counterAI++;
                } else {
                    counterAI = 0;
                }

                if (counterPlayer == 4) {
                    score = Double.POSITIVE_INFINITY;
                } else if (counterAI == 4) {
                    score = Double.NEGATIVE_INFINITY;
                }
            }
        }
        return score;
    }

    public double checkVertical(Connect4 c4game) {
        double score = 0;
        int counterPlayer = 0;
        int counterAI = 0;

        for (int i = 0; i < Connect4.numberOfColumns; i++) {
            for (int j = 0; j < Connect4.numberOfRows; j++) {
                if (c4game.board[j][i] == Connect4.Tokens.PLAYER.getToken()) {
                    counterPlayer++;
                } else {
                    counterPlayer = 0;
                }

                if (c4game.board[j][i] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    counterAI++;
                } else {
                    counterAI = 0;
                }

                if (counterPlayer == 4) {
                    score = Double.POSITIVE_INFINITY;
                } else if (counterAI == 4) {
                    score = Double.NEGATIVE_INFINITY;
                }
            }
        }
        return score;
    }

    public double checkDiagonal(Connect4 c4game) {
        double score = 0;

        // Sprawdź ukosy od lewej górnej do prawej dolnej
        for (int i = 0; i < Connect4.numberOfRows - 3; i++) {
            for (int j = 0; j < Connect4.numberOfColumns - 3; j++) {
                if (c4game.board[i][j] == Connect4.Tokens.PLAYER.getToken() &&
                        c4game.board[i + 1][j + 1] == Connect4.Tokens.PLAYER.getToken() &&
                        c4game.board[i + 2][j + 2] == Connect4.Tokens.PLAYER.getToken() &&
                        c4game.board[i + 3][j + 3] == Connect4.Tokens.PLAYER.getToken()) {
                    score = Double.POSITIVE_INFINITY;
                } else if (c4game.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        c4game.board[i + 1][j + 1] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        c4game.board[i + 2][j + 2] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        c4game.board[i + 3][j + 3] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    score = Double.NEGATIVE_INFINITY;
                }
            }
        }

        // Sprawdź ukosy od lewej dolnej do prawej górnej
        for (int i = 3; i < Connect4.numberOfRows; i++) {
            for (int j = 0; j < Connect4.numberOfColumns - 3; j++) {
                if (c4game.board[i][j] == Connect4.Tokens.PLAYER.getToken() &&
                        c4game.board[i - 1][j + 1] == Connect4.Tokens.PLAYER.getToken() &&
                        c4game.board[i - 2][j + 2] == Connect4.Tokens.PLAYER.getToken() &&
                        c4game.board[i - 3][j + 3] == Connect4.Tokens.PLAYER.getToken()) {
                    score = Double.POSITIVE_INFINITY;
                } else if (c4game.board[i][j] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        c4game.board[i - 1][j + 1] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        c4game.board[i - 2][j + 2] == Connect4.Tokens.AI_PLAYER.getToken() &&
                        c4game.board[i - 3][j + 3] == Connect4.Tokens.AI_PLAYER.getToken()) {
                    score = Double.NEGATIVE_INFINITY;
                }
            }
        }

        return score;
    }

    private double countTokensInARow(Connect4 c4game, Connect4.Tokens playerToken, int length) {
        int count = 0;

        // Sprawdzanie w poziomie
        for (int i = 0; i < Connect4.numberOfRows; i++) {
            for (int j = 0; j <= Connect4.numberOfColumns - length; j++) {
                boolean isMatch = true;
                for (int k = 0; k < length; k++) {
                    if (c4game.board[i][j + k] != playerToken.getToken()) {
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
                    if (c4game.board[i + k][j] != playerToken.getToken()) {
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
                    if (c4game.board[i + k][j + k] != playerToken.getToken()) {
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
                    if (c4game.board[i - k][j + k] != playerToken.getToken()) {
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

}


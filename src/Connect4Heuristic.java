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

        if (c4game.checkGameResult() != 0 && !c4game.isMaximizingTurnNow()) { // być może trzeba zamienić ze sobą dwa returny
            return Double.POSITIVE_INFINITY;
        } else if (c4game.checkGameResult() != 0  && c4game.isMaximizingTurnNow()) {
            return Double.NEGATIVE_INFINITY;
        }

        double playerThreeInARow = c4game.countTokensInARow(Connect4.Tokens.PLAYER, 3);
        double aiThreeInARow = c4game.countTokensInARow(Connect4.Tokens.AI_PLAYER, 3);
        double playerTwoInARow = c4game.countTokensInARow(Connect4.Tokens.PLAYER, 2);
        double aiTwoInARow = c4game.countTokensInARow(Connect4.Tokens.AI_PLAYER, 2);
        double playerOneInARow = c4game.countTokensInARow(Connect4.Tokens.PLAYER, 1);
        double aiOneInARow = c4game.countTokensInARow(Connect4.Tokens.AI_PLAYER, 1);

        score =  -((aiThreeInARow * 100) - (playerThreeInARow * 100) + (aiTwoInARow * 10) - (playerTwoInARow * 10) + (aiOneInARow) - (playerOneInARow));
        return score;
    }


}


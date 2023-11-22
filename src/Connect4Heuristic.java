import sac.State;
import sac.StateFunction;

public class Connect4Heuristic extends StateFunction {
    @Override
    public double calculate(State state) {
        Connect4 c4game = (Connect4) state;

        if (c4game.checkGameResult() != 0 && !c4game.isMaximizingTurnNow()) { // być może trzeba zamienić ze sobą dwa returny
            return Double.POSITIVE_INFINITY;
        } else if (c4game.checkGameResult() != 0  && c4game.isMaximizingTurnNow()) {
            return Double.NEGATIVE_INFINITY;
        }

        double playerThreeInARow = c4game.countTokensInARow(Connect4.Tokens.PLAYER, 3, false);
        double aiThreeInARow = c4game.countTokensInARow(Connect4.Tokens.AI_PLAYER, 3, false);
        double playerTwoInARow = c4game.countTokensInARow(Connect4.Tokens.PLAYER, 2, false);
        double aiTwoInARow = c4game.countTokensInARow(Connect4.Tokens.AI_PLAYER, 2, false);
        double playerOneInARow = c4game.countTokensInARow(Connect4.Tokens.PLAYER, 1, false);
        double aiOneInARow = c4game.countTokensInARow(Connect4.Tokens.AI_PLAYER, 1, false);

        return -((aiThreeInARow * 100) - (playerThreeInARow * 100) + (aiTwoInARow * 10) - (playerTwoInARow * 10) + (aiOneInARow) - (playerOneInARow));
    }


}


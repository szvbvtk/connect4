import sac.State;
import sac.StateFunction;

public class Connect4Heuristic extends StateFunction {
    @Override
    public double calculate(State state) {
        Connect4 c4game = (Connect4) state;

        int gameResult = c4game.checkGameResult();
        if (gameResult != 0 && !c4game.isMaximizingTurnNow()) {
            return Double.POSITIVE_INFINITY;
        } else if (gameResult != 0 && c4game.isMaximizingTurnNow()) {
            return Double.NEGATIVE_INFINITY;
        }

        int score = 0;
        int multiplier = 10;
        for (int i = 2; i < Connect4.numberOfTokensRequiredToWin; i++) {
            score += c4game.countSequences(Connect4.Tokens.AI_PLAYER.getToken(), i, false) * Math.pow(multiplier, i - 1);
            score -= c4game.countSequences(Connect4.Tokens.PLAYER.getToken(), i, false) * Math.pow(multiplier, i - 1);
        }
        score = -score;
        return score;

    }


}


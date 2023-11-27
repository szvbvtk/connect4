import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;
import sac.game.GameSearchConfigurator;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connect4.setHFunction(new Connect4Heuristic());
        Connect4 c4game = Connect4.createGame();

        if (c4game.isMaximizingTurnNow()) {
            System.out.println(c4game);
        }

        GameSearchConfigurator gsc = new GameSearchConfigurator();
        gsc.setDepthLimit(6);

        GameSearchAlgorithm gsa = new AlphaBetaPruning();
        gsa.setInitial(c4game);
        gsa.setConfigurator(gsc);

        int gameResult;
        while ((gameResult = c4game.checkGameResult()) == 0) {
            if (c4game.isMaximizingTurnNow()) {
                Scanner scanner = new Scanner(System.in);
                int column;
                do {
                    System.out.print("Your turn: ");
                    column = scanner.nextInt();
                } while (!c4game.makeMove(column));

                System.out.println(c4game);
            } else {
                gsa.execute();
                System.out.println("AI turn");
                Map<String, Double> bestMovesList = gsa.getMovesScores();
                for (Map.Entry<String, Double> entry : bestMovesList.entrySet()) {
                    System.out.println(entry.getKey() + "=" + entry.getValue());
                }

                int aiMove = Integer.parseInt(gsa.getFirstBestMove());
                c4game.makeMove(aiMove);
                System.out.println(c4game);
            }
        }

        if (gameResult == 1) {
            System.out.println("Game over\nThe winner is: " + Connect4.Tokens.PLAYER.getToken());
        } else if (gameResult == -1) {
            System.out.println("Game over\nThe winner is: " + Connect4.Tokens.AI_PLAYER.getToken());
        } else if (gameResult == 2) {
            System.out.println("Game over\nDraw");
        }

        System.out.println(c4game);
    }
}

import sac.game.AlphaBetaPruning;
import sac.game.GameSearchAlgorithm;

import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connect4 c4game = new Connect4();
        System.out.println(c4game);
        Connect4.setHFunction(new Connect4Heuristic());

//        System.out.println(c4game.isMaximizingTurnNow());
        GameSearchAlgorithm gsa = new AlphaBetaPruning();
        gsa.setInitial(c4game);

        while(!c4game.checkWin()) {
            if(c4game.isMaximizingTurnNow()) {
                System.out.println("Your turn: ");
                Scanner scanner = new Scanner(System.in);
                int column = scanner.nextInt();
                c4game.makeMove(column);
//                System.out.println(c4game);
            } else {
                System.out.println("AI turn");
                gsa.execute();
                Map<String, Double> bestMoves = gsa.getMovesScores();
                for (Map.Entry<String, Double> entry : bestMoves.entrySet())
                    System.out.println(entry.getKey() + " -> " + entry.getValue());
                String s = gsa.getFirstBestMove();
                int aiMove = Integer.parseInt(s);
                c4game.makeMove(aiMove);
//                while(!c4game.makeMove((int)(Math.random() * 6))) {
//                    continue;
//                }
                System.out.println(c4game);
            }
        }

        System.out.println("Game over\nThe winner is: " + (c4game.isMaximizingTurnNow() ? "AI" : "Player"));
    }
}
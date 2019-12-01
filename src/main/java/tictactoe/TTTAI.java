package tictactoe;

import java.util.*;

public class TTTAI {
    boolean player;
    int difficulty;
    Random rand = new Random();

    public TTTAI(boolean player, int difficulty) {
        this.player = player;
        this.difficulty = difficulty;
    }

    private static int evalGame(TicTacToe game) {
        if (!game.checkWin()) {
            return 0;
        }
        return game.getPlayer() ? -1 : 1; // -1 means O won, 1 means X won
    }

    private static List<int[]> possibleMoves(TicTacToe game) {
        LinkedList<int[]> ret = new LinkedList<>();

        for (int i = 0; i < game.edgeLength; i++) {
            for (int j = 0; j < game.edgeLength; j++) {
                if (game.checkValid(i, j)) {
                    ret.add(new int[]{i, j});
                }
            }
        }
        return ret;
    }

    private static TicTacToe moveToBoard(int[] move, TicTacToe game) {
        TicTacToe next = new TicTacToe(game);
        int i = move[0];
        int j = move[1];
        next.move(i, j);

        return next;
    }

    private static int minimaxScore(boolean player, TicTacToe game, int depth) {
        if (depth == 0 || game.checkGameOver()) {
            return evalGame(game);
        }
        LinkedList<Integer> scores = new LinkedList<>();
        List<int[]> moves = possibleMoves(game);
        for (int[] move : moves) {
            TicTacToe next = moveToBoard(move, game);
            int score = evalGame(next);
            if (score != 0 || next.checkDraw()) {
                scores.add(score);
            }
            else {
                scores.add(minimaxScore(!player, next, depth - 1));
            }
        }
        if (player) {
            return Collections.max(scores);
        }
        else {
            return Collections.min(scores);
        }
    }

    public int[] nextMove(TicTacToe game) {
        List<int[]> moves = possibleMoves(game);
        int multiplier = player ? 1 : -1;
        int max = Integer.MIN_VALUE;
        LinkedList<int[]> bestMoves = new LinkedList<>();

        for (int[] move : moves) {
            TicTacToe next = moveToBoard(move, game);
            int score = multiplier * minimaxScore(!player, next, difficulty);
            if (score > max) {
                max = score;
                bestMoves.clear();
                bestMoves.add(move);
            }
            else if (score == max) {
                bestMoves.add(move);
            }
        }
        if (bestMoves.isEmpty()) {
            return null;
        }
        int randIndex = rand.nextInt(bestMoves.size());
        return bestMoves.get(randIndex);
    }
}

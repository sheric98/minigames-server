package checkers;

import java.util.*;

import static checkers.CheckersConstants.*;

public class CheckersAI {
    boolean player;
    int difficulty;
    Random rand = new Random();

    public CheckersAI (boolean player, int difficulty) {
        this.player = player;
        this.difficulty = difficulty;
    }

    public static int evalGame(Checkers game) {
        int blackScore = (game.blackPieces - game.blackKings) + (KING_WORTH * game.blackKings);
        int whiteScore = (game.whitePieces - game.whiteKings) + (KING_WORTH * game.whiteKings);

        return blackScore - whiteScore;
    }

    public static List<List<Checkers.Move>> validMoves(Checkers game) {
        int playerInt = game.getPlayer() ? 1 : -1;
        List<List<Checkers.Move>> ret = new LinkedList<>();

        int i, j;
        List<Checkers.Move> movesForPiece, toAdd;
        List<List<Checkers.Move>> jumpToAdd;
        for (i = 0; i < NUM_ROWS; i++) {
            for (j = 0; j < NUM_COLS; j++) {
                if (game.board[i][j] * playerInt > 0) { // correct player
                    movesForPiece = game.validMovesForPiece(new int[]{i, j});
                    for (Checkers.Move move : movesForPiece) {
                        toAdd = new LinkedList<>();
                        toAdd.add(move);
                        if (Checkers.isJump(move)) {
                            Checkers copyGame = game.cloneGame();
                            copyGame.makeMove(move);
                            jumpToAdd = new LinkedList<>();
                            jumpChainMoves(copyGame, toAdd, jumpToAdd);
                            ret.addAll(jumpToAdd);
                        }
                        else {
                            ret.add(toAdd);
                        }
                    }
                }
            }
        }

        return ret;
    }

    public static void jumpChainMoves(Checkers game, List<Checkers.Move> prev,
                                      List<List<Checkers.Move>> ret) {
        if (game.validMoves.isEmpty()) {
            ret.add(prev);
            return;
        }

        for (Checkers.Move move : game.validMoves) {
            List<Checkers.Move> copyPrev = new LinkedList<>(prev);
            copyPrev.add(move);
            Checkers copyGame = game.cloneGame();
            copyGame.makeMove(move);
            jumpChainMoves(copyGame, copyPrev, ret);
        }
    }

    public static List<Integer> minimaxEval(Checkers game, List<List<Checkers.Move>> moves, int depth) {
        List<Integer> ret = new LinkedList<>();
        int playerInt = game.player ? 1 : -1;
        int otherPlayer = -1 * playerInt;
        int score;

        if (game.checkWin()) {
            score = otherPlayer * WIN_WORTH;
            ret.add(score);
            return ret;
        }

        if (moves.isEmpty()) {
            score = otherPlayer * WIN_WORTH;
            ret.add(score);
            return ret;
        }

        if (depth <= 0) {
            score = evalGame(game);
            ret.add(score);
            return ret;
        }

        List<Integer> scores;
        for (List<Checkers.Move> chain : moves) {
            Checkers copyGame = game.cloneGame();
            copyGame.applyChain(chain);
            scores = minimaxEval(copyGame, validMoves(copyGame), depth - 1);
            if (game.player) {
                score = Collections.min(scores);
            }
            else {
                score = Collections.max(scores);
            }
            ret.add(score);
        }
        return ret;
    }

    public List<Checkers.Move> nextMove(Checkers game) {
        List<List<Checkers.Move>> validMoves = validMoves(game);
        List<Integer> scores = minimaxEval(game, validMoves, difficulty);
        if (validMoves.isEmpty()) {
            System.out.println("cpu has no moves");
            return null;
        }
        if (validMoves.size() != scores.size()) {
            System.out.println("different number of moves and scores");
            return null;
        }

        int playerInt = game.player ? 1 : -1;
        List<List<Checkers.Move>> bestMoves = new LinkedList<>();

        int max = -1;
        int i, score;
        for (i = 0; i < validMoves.size(); i++) {
            score = playerInt * scores.get(i);
            if (score > max) {
                bestMoves.clear();
                bestMoves.add(validMoves.get(i));
                max = score;
            }
            else if (score == max) {
                bestMoves.add(validMoves.get(i));
            }
        }
        int randIndex = rand.nextInt(bestMoves.size());
        return bestMoves.get(randIndex);
    }

    public static void makeCPUMove(Checkers game) {
        if (game.againstCPU() && !game.checkWin()) {
            List<Checkers.Move> cpuMove = game.getCPU().nextMove(game);
            game.applyChain(cpuMove);
        }
    }

    public void resetCPU() {
        rand = new Random();
    }
}

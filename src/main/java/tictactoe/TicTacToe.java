package tictactoe;

public class TicTacToe {
    public final int NUM_ROWS = 3;
    public final int NUM_COLS = 3;
    public final int NUM_LINES = 8;
    public final int PLAYER_X = 1;
    public final int PLAYER_O = -1;

    int[][] board;
    int[] lines;
    boolean player; // true for x, false for o

    TicTacToe() {
        board = new int[NUM_ROWS][NUM_COLS];
        lines = new int[NUM_LINES];
        player = true;
    }

    private void move(int i, int j) {
        int playerNum = player ? PLAYER_X : PLAYER_O;

        // update board
        board[i][j] = playerNum;

        // update scores of each line
        // update row
        lines[i] += playerNum;
        // update column
        lines[NUM_ROWS + j] += playerNum;
        // update diagonals
        if (i == j) {
            lines[NUM_ROWS + NUM_COLS] += playerNum;
        }
        if (i + j == 2) {
            lines[NUM_ROWS + NUM_COLS] += playerNum;
        }
    }

    private boolean checkValid(int i, int j) {
        return board[i][j] == 0;
    }

    private boolean checkWin() {
        for (int score : lines) {
            if (Math.abs(score) == 3) {
                return true;
            }
        }
        return false;
    }
}

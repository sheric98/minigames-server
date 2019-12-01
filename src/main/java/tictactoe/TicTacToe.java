package tictactoe;

import static tictactoe.TTTConstants.*;

public class TicTacToe implements Cloneable {
    int edgeLength;
    int numLines;
    int[][] board;
    int[] lines;
    int emptyCells;
    boolean player; // true for x, false for o
    TTTAI CPU = new TTTAI(false, 1);

    public TicTacToe(int edgeLength) {
        this.edgeLength = edgeLength;
        numLines = 2* edgeLength + 2;
        board = new int[edgeLength][edgeLength];
        lines = new int[numLines];
        player = true;
        emptyCells = edgeLength * edgeLength;
    }

    public TicTacToe (TicTacToe toCopy) {
        this.edgeLength = toCopy.getEdgeLength();
        this.numLines = toCopy.getNumLines();
        board = new int[edgeLength][edgeLength];
        int[][] prevBoard = toCopy.getBoard();
        for (int i = 0; i < edgeLength; i++) {
            for (int j = 0; j < edgeLength; j++) {
                board[i][j] = prevBoard[i][j];
            }
        }
        lines = new int[numLines];
        int[] prevLines = toCopy.getLines();
        for (int i = 0; i < numLines; i++) {
            lines[i] = prevLines[i];
        }
        player = toCopy.getPlayer();
        emptyCells = toCopy.getEmptyCells();

    }

    public int[][] getBoard() {
        return this.board;
    }

    public int[] getLines() {
        return this.lines;
    }

    public boolean getPlayer() {
        return this.player;
    }

    public int getEdgeLength() {
        return this.edgeLength;
    }

    public int getNumLines() {
        return this.numLines;
    }

    public int getOtherPlayerNum() {
        return this.player ? 2 : 1;
    }

    public int getEmptyCells() {
        return this.emptyCells;
    }

    public TTTAI getCPU() {
        return this.CPU;
    }

    public boolean againstCPU() {
        return this.CPU != null;
    }

    public void move(int i, int j) {
        int playerNum = player ? PLAYER_X : PLAYER_O;

        // update board
        this.board[i][j] = playerNum;

        // update scores of each line
        // update row
        this.lines[i] += playerNum;
        // update column
        this.lines[edgeLength + j] += playerNum;
        // update diagonals
        if (i == j) {
            this.lines[2 * edgeLength] += playerNum;
        }
        if (i + j == edgeLength - 1) {
            this.lines[2 * edgeLength + 1] += playerNum;
        }

        this.player = !player;
        this.emptyCells--;
    }

    public boolean checkValid(int i, int j) {
        return board[i][j] == 0;
    }

    public boolean checkWin() {
        for (int score : this.lines) {
            if (Math.abs(score) == edgeLength) {
                return true;
            }
        }
        return false;
    }

    public boolean checkDraw() {
        return !this.checkWin() && emptyCells == 0;
    }

    public boolean checkGameOver() {
        return this.checkWin() || this.checkDraw();
    }
}

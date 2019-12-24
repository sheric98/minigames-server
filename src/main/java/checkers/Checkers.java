package checkers;

import java.util.*;

import static checkers.CheckersConstants.*;

public class Checkers {
    int[][] board = new int[NUM_ROWS][NUM_COLS]; // 1 for black, 0 for none, -1 for white
                                                 // 2 for black king, -2 for white king
    boolean player = true; // black goes first
    int blackPieces = 3 * NUM_COLS / 2;
    int whitePieces = 3 * NUM_COLS / 2;
    int blackKings = 0;
    int whiteKings = 0;
    List<Move> validMoves = new LinkedList<>();
    CheckersAI CPU;

    public Checkers() {
        defaultStart(this.board);
        CPU = null;
    }

    public Checkers(boolean player, int difficulty) {
        defaultStart(this.board);
        CPU = new CheckersAI(player, difficulty);
    }

    public Checkers cloneGame() {
        Checkers ret = new Checkers();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                ret.board[i][j] = this.board[i][j];
            }
        }
        ret.player = this.player;
        ret.blackPieces = this.blackPieces;
        ret.whitePieces = this.whitePieces;
        ret.blackKings = this.blackKings;
        ret.whiteKings = this.whiteKings;
        for (Move move : this.validMoves) {
            ret.validMoves.add(move);
        }
        return ret;
    }

    public static class Move {
        int[] piece;
        int[] location;

        public Move(int pieceX, int pieceY, int locationX, int locationY) {
            piece = new int[] {pieceX, pieceY};
            location = new int[] {locationX, locationY};
        }

        public int getPieceX() {
            return piece[0];
        }

        public int getPieceY() {
            return piece[1];
        }

        public int getLocationX() {
            return location[0];
        }

        public int getLocationY() {
            return location[1];
        }
    }

    public int[][] getBoard() {
        return this.board;
    }

    public boolean getPlayer() {
        return this.player;
    }

    public CheckersAI getCPU() {
        return this.CPU;
    }

    public boolean againstCPU() {
        return this.CPU != null;
    }

    public boolean checkValid(Move move) {
        int i = move.getPieceX();
        int j = move.getPieceY();
        int x = move.getLocationX();
        int y = move.getLocationY();

        if (validMoves.size() > 0) {
            for (Move validMove : validMoves) {
                if (coordsEquals(move.piece, validMove.piece) &&
                coordsEquals(move.location, validMove.location)) {
                    return true;
                }
            }
            return false;
        }

        int playerInt = player ? 1 : -1;

        if (!inBounds(move.location) || board[i][j] * playerInt <= 0 ||
                board[x][y] != EMPTY) { // moving out of bounds or wrong piece
                                        // or to occupied space
            return false;
        }

        for (int[] dir : DIRS) {
            if (validDir(move.piece, dir)) {
                int[] loc = {i + dir[0], j + dir[1]};
                if (coordsEquals(move.location, loc)) {
                    return true;
                }
                if (hasJumpInDir(move.piece, dir)) {
                    int[] jumpLoc = {i + 2*dir[0], j + 2*dir[1]};
                    if (coordsEquals(move.location, jumpLoc)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public List<Move> validMovesForPiece(int[] piece) {
        int i = piece[0];
        int j = piece[1];
        int playerInt = player ? 1 : -1;
        List<Move> ret = new LinkedList<>();

        if (board[i][j] * playerInt <= 0 ) { // invalid piece to move
            return ret;
        }

        for (int[] dir : DIRS) {
            if (validDir(piece, dir)) {
                if (checkDirForPiece(piece, dir, EMPTY)) {
                    ret.add(new Move(i, j, i + dir[0], j + dir[1]));
                }
                else if (hasJumpInDir(piece, dir)) {
                    ret.add(new Move(i, j, i + 2*dir[0], j + 2*dir[1]));
                }
            }
        }
        return ret;
    }

    public boolean checkDirForPiece(int[] cur, int[] dir, int piece) {
        int x = cur[0] + dir[0];
        int y = cur[1] + dir[1];

        if (piece != 0) {
            return (board[x][y] * piece) > 0;
        }
        return board[x][y] == piece;
    }

    public void makeMove(Move move) {
        int i = move.getPieceX();
        int j = move.getPieceY();
        int x = move.getLocationX();
        int y = move.getLocationY();
        int playerInt = player ? 1 : -1;

//        System.out.println(playerInt + " " + i + " " + j + " " + x + " " + y);

        if (validMoves.size() > 0) {
            validMoves.clear();
        }

        checkKing(move);

        board[x][y] = board[i][j];
        board[i][j] = EMPTY;

        if (isJump(move)) {
            makeJump(move);
        }
        else {
            player = !player;
        }
    }

    public void applyChain(List<Move> chain) {
        for (Move move : chain) {
            makeMove(move);
        }
    }

    public void checkKing(Move move) {
        int i = move.getPieceX();
        int j = move.getPieceY();
        int x = move.getLocationX();

        int kingRow = player ? 0 : (NUM_ROWS - 1);
        int playerInt = player ? 1 : -1;
        int kingPiece = playerInt * KING;
        if (x == kingRow) {
            board[i][j] = kingPiece;
            if (player) {
                blackKings++;
            }
            else {
                whiteKings++;
            }
        }
    }

    public void makeJump(Move move) {
        int i = move.getPieceX();
        int j = move.getPieceY();
        int x = move.getLocationX();
        int y = move.getLocationY();

        int dirX = (x - i) / 2;
        int dirY = (y - j) / 2;

        if (player) {
            whitePieces--;
            if (isKing(i + dirX, j + dirY)) {
                whiteKings--;
            }
        }
        else {
            blackPieces--;
            if (isKing(i + dirX, j + dirY)) {
                blackKings--;
            }
        }

        board[i + dirX][j + dirY] = EMPTY;

        if (!anotherJump(move)) {
            player = !player;
        }
    }

    public boolean anotherJump(Move move) {
        int playerInt = player ? 1 : -1;
        int otherPlayer = -1 * playerInt;
        int x = move.getLocationX();
        int y = move.getLocationY();

        for (int[] dir : DIRS) {
            if (validDir(move.location, dir)) {
                if (hasJumpInDir(move.location, dir)) {
                    validMoves.add(new Move(x, y, x + 2*dir[0], y + 2*dir[1]));
                }
            }
        }
        return validMoves.size() > 0;
    }

    public boolean hasJumpInDir(int[] loc, int[] dir) {
        int x = loc[0];
        int y = loc[1];
        int otherPlayer = player ? -1 : 1;

        if (checkDirForPiece(loc, dir, otherPlayer)) {
            int[] halfJump = {x + dir[0], y + dir[1]};
            int[] jumpLoc = {x + 2*dir[0], y + 2*dir[1]};
            if (inBounds(jumpLoc) && checkDirForPiece(halfJump, dir, EMPTY)) {
                return true;
            }
        }
        return false;
    }

    public boolean validDir(int[] loc, int[] dir) {
        int playerDir = player ? -1 : 1;
        int[] newLoc = {loc[0] + dir[0], loc[1] + dir[1]};
        return (inBounds(newLoc) && (isKing(loc[0], loc[1]) || dir[0] == playerDir));
    }

    public boolean inBounds(int[] loc) {
        int x = loc[0];
        int y = loc[1];

        return (x >= 0 && x < NUM_ROWS && y >= 0 && y < NUM_COLS);
    }

    public static boolean isJump(Move move) {
        int pieceX = move.getPieceX();
        int pieceY = move.getPieceY();
        int locX = move.getLocationX();
        int locY = move.getLocationY();

        return (Math.abs(pieceX - locX) == 2 && Math.abs(pieceY - locY) == 2);
    }

    public boolean isKing(int i, int j) {
        return Math.abs(board[i][j]) == KING;
    }

    public static boolean coordsEquals(int[] a, int[] b) {
        return (a[0] == b[0] && a[1] == b[1]);
    }

    public boolean checkWin() {
        return (blackPieces == 0 || whitePieces == 0);
    }

    public static void defaultStart(int[][] board) {
        int i, j;
        for (i = 0; i < NUM_COLS; i++) {
            for (j = 0; j < NUM_COLS; j++) {
                if ((i + j) % 2 == 1) {
                    if (i < 3) {
                        board[i][j] = WHITE;
                    }
                    else if (i > 4) {
                        board[i][j] = BLACK;
                    }
                    else {
                        board[i][j] = EMPTY;
                    }
                }
                else {
                    board[i][j] = EMPTY;
                }
            }
        }
    }

    public void resetGame() {
        defaultStart(board);
        player = true;
        blackPieces = 3 * NUM_COLS / 2;
        whitePieces = 3 * NUM_COLS / 2;
        blackKings = 0;
        whiteKings = 0;
        validMoves = new LinkedList<>();
        if (CPU != null) {
            CPU.resetCPU();
        }
    }
}

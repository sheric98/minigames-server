package checkers;

public final class CheckersConstants {
    public static final int NUM_ROWS = 8;
    public static final int NUM_COLS = 8;
    public static final int WHITE = -1;
    public static final int BLACK = 1;
    public static final int EMPTY = 0;
    public static final int KING = 2;
    public static final int[][] DIRS = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};

    // for AI
    public static final int KING_WORTH = 5;
    public static final int WIN_WORTH = 50;
}

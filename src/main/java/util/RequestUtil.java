package util;

import checkers.Checkers;
import database.Database;
import spark.*;
import tictactoe.TicTacToe;
import static util.UtilConstants.*;
import user.User;

public class RequestUtil {
    public static int[] getTile(Request req, TicTacToe game) {
        int tileNum = Integer.parseInt(req.queryParams("tile"));
        int edgeLength = game.getEdgeLength();
        int i = tileNum / edgeLength;
        int j = tileNum % edgeLength;
        return new int[] {i, j};
    }

    public static Checkers.Move getMove(Request req, Checkers game) {
        int pieceId = Integer.parseInt(req.queryParams("piece"));
        int moveId = Integer.parseInt(req.queryParams("move"));
        int edgeLength = game.getBoard().length;

        int pieceX = pieceId / edgeLength;
        int pieceY = pieceId % edgeLength;
        int locX = moveId / edgeLength;
        int locY = moveId % edgeLength;

        return new Checkers.Move(pieceX, pieceY, locX, locY);
    }

    public static boolean againstCPU(Request req) {
        return CPU.equals(req.queryParams("versus"));
    }

    public static int getDifficulty(Request req) {
        String diff = req.queryParams("diff");
        if (diff.equals(EASY)) {
            return 1;
        }
        if (diff.equals(MEDIUM)) {
            return 3;
        }
        if (diff.equals(HARD)) {
            return 5;
        }
        return -1;
    }

    public static boolean getCPUPlayer(Request req) {
        return O.equals(req.queryParams("player"));
    }

    public static boolean checkReset(Request req) {
        return RESET.equals(req.queryParams("reset"));
    }

    public static int getSize(Request req) {
        return Integer.parseInt(req.queryParams("size"));
    }

    public static User currentSessionUser(Request req, Response res) {
        String idStr = req.cookie("id");
        Database db = Database.getGlobalDB();
        if (idStr == null) {
            User user = new User(db);
            res.cookie("id", Long.toString(user.getId()));
            return user;
        }
        long id = Long.parseLong(idStr);
        return db.getUser(id);
    }
}

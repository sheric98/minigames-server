package util;

import checkers.Checkers;
import database.Database;
import spark.*;
import tictactoe.TicTacToe;
import static util.UtilConstants.*;
import static util.ServerUsersUtil.*;
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
        String versus = req.queryParams("versus");
        if (versus == null) {
            return false;
        }
        return CPU.equals(versus);
    }

    public static int getDifficulty(Request req) {
        String diff = req.queryParams("diff");
        if (diff.equals(EASY)) {
            return EASY_DIF;
        }
        if (diff.equals(MEDIUM)) {
            return MED_DIF;
        }
        if (diff.equals(HARD)) {
            return HARD_DIF;
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
        String size = req.queryParams("size");
        if (size == null) {
            return DEFAULT_SIZE;
        }
        return Integer.parseInt(req.queryParams("size"));
    }

    public static User currentSessionUser(Request req, Response res) {
        User user;
        String idStr = req.cookie("id");
//        Session session = req.session(true);
//        System.out.println("in currentSessionUser");
//        System.out.println(session);
        Database db = Database.getGlobalDB();
        if (idStr == null || idStr.length() == 0) {
            user = createUser(req, res);
            System.out.println("no id");
            return user;
        }
        long id = Long.parseLong(idStr);
        System.out.println("got user " + id);
        if (db.hasId(id)) {
            user = db.getUser(id);
        }
        else {
            user = createUser(req, id);
        }
        return user;
    }
}

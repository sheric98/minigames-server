package util;

import database.Database;
import spark.*;
import tictactoe.TicTacToe;
import user.User;

public class RequestUtil {
    public static int[] getTile(Request req, TicTacToe game) {
        int tileNum = Integer.parseInt(req.queryParams("tile"));
        int edgeLength = game.getEdgeLength();
        int i = tileNum / edgeLength;
        int j = tileNum % edgeLength;
        return new int[] {i, j};
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

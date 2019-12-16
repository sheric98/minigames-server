package checkers;

import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
import tictactoe.TicTacToe;
import user.User;

import java.util.HashMap;
import java.util.Map;

import static util.RequestUtil.*;
import static util.RequestUtil.getCPUPlayer;
import static util.ViewUtil.updateModel;

public class CheckersController {
    public static TemplateViewRoute handleCheckersGet(String templatePath) {
        return (Request req, Response res) -> {
            Checkers game = createCheckers(req, res);
            Map<String, Object> model = CheckersMap(game);
            return updateModel(model, templatePath);
        };
    }

    public static TemplateViewRoute handleCheckersPost(String templatePath) {
        return (Request req, Response res) -> {
            Checkers game = getCheckers(req, res);
            Checkers.Move move = getMove(req, game);
            if (game.checkValid(move)) {
                game.makeMove(move);
            }

            Map<String, Object> model = CheckersMap(game);
            return updateModel(model, templatePath);
        };
    }

    private static Checkers createCheckers(Request req, Response res) {
        User user = currentSessionUser(req, res);
        user.createCheckers();
        return user.getCheckers();
    }

    private static Checkers getCheckers(Request req, Response res) {
        User user = currentSessionUser(req, res);
        return user.getCheckers();
    }

    private static Map<String, Object> CheckersMap(Checkers game) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tiles", game.getBoard());
        map.put("m", game.getBoard().length);
        map.put("n", game.getBoard()[0].length);
        map.put("player", game.getPlayer());
        map.put("win", game.checkWin());
        return map;
    }
}

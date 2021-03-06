package checkers;

import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;
import user.User;

import java.util.HashMap;
import java.util.Map;

import static util.RequestUtil.*;
import static util.ViewUtil.updateModel;

public class CheckersController {
    public static TemplateViewRoute handleCheckersGet(String templatePath) {
        return (Request req, Response res) -> {
            Checkers game;
            if (againstCPU(req)) {
                int difficulty = getDifficulty(req);
                boolean cpuPlayer = getCPUPlayer(req);
                game = createCheckers(req, res, cpuPlayer, difficulty);
                if (cpuPlayer) {
                    CheckersAI.makeCPUMove(game);
                }
            }
            else {
                game = createCheckers(req, res);
            }
            Map<String, Object> model = CheckersMap(game);
            return updateModel(model, templatePath);
        };
    }

    public static TemplateViewRoute handleCheckersPost(String templatePath) {
        return (Request req, Response res) -> {
            Checkers game = getCheckers(req, res);
            if (checkReset(req)) {
                game.resetGame();
                if (game.againstCPU() && game.CPU.player) {
                    CheckersAI.makeCPUMove(game);
                }
            }
            else {
                Checkers.Move move = getMove(req, game);
                if (game.checkValid(move)) {
                    game.makeMove(move);
                    CheckersAI.makeCPUMove(game);
                }
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

    private static Checkers createCheckers(Request req, Response res, boolean player, int difficulty) {
        User user = currentSessionUser(req, res);
        user.createCheckers(player, difficulty);
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

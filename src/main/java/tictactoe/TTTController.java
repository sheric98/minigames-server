package tictactoe;

import spark.*;
import user.User;

import static util.ViewUtil.*;
import static util.RequestUtil.*;

import java.util.*;

public class TTTController {

    public static TemplateViewRoute handleTTTGet(String templatePath) {
        return (Request req, Response res) -> {
            TicTacToe game = getTTTGame(req, res);
            Map<String, Object> model = TTTMap(game);
            return updateModel(model, templatePath);
        };
    }

    public static TemplateViewRoute handleTTTPost(String templatePath) {
        return (Request req, Response res) -> {
            TicTacToe game = getTTTGame(req, res);
            int[] tile = getTile(req, game);
            if (game.checkValid(tile[0], tile[1])) {
                game.move(tile[0], tile[1]);
            }
            if (game.againstCPU() && !game.checkGameOver()) {
                int[] cpuMove = game.getCPU().nextMove(game);
                game.move(cpuMove[0], cpuMove[1]);
            }
            Map<String, Object> model = TTTMap(game);
            return updateModel(model, templatePath);
        };
    }

    private static TicTacToe getTTTGame(Request req, Response res) {
        User user = currentSessionUser(req, res);
        if (user.getTttGame() == null) {
            user.createTTT(3);
        }
        return user.getTttGame();
    }

    private static Map<String, Object> TTTMap(TicTacToe game) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tiles", game.getBoard());
        map.put("win", game.checkWin());
        map.put("draw", game.checkDraw());
        map.put("numEdges", game.getEdgeLength());
        map.put("otherPlayer", game.getOtherPlayerNum());
        return map;
    }
}

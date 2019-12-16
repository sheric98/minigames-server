package tictactoe;

import spark.*;
import user.User;
import tictactoe.TTTAI.*;

import static util.ViewUtil.*;
import static util.RequestUtil.*;

import java.util.*;

public class TTTController {

    public static TemplateViewRoute handleTTTGet(String templatePath) {
        return (Request req, Response res) -> {
            TicTacToe game;
            int size = getSize(req);
            if (againstCPU(req)) {
                int difficulty = getDifficulty(req);
                boolean cpuPlayer = getCPUPlayer(req);
                game = createTTTGame(req, res, size, cpuPlayer, difficulty);
                if (cpuPlayer) {
                    TTTAI.makeCPUMove(game);
                }
            }
            else {
                game = createTTTGame(req, res, size);
            }
            Map<String, Object> model = TTTMap(game);
            return updateModel(model, templatePath);
        };
    }

    public static TemplateViewRoute handleTTTPost(String templatePath) {
        return (Request req, Response res) -> {
            TicTacToe game = getTTTGame(req, res);
            if (checkReset(req)) {
                game.resetGame();
                if (game.againstCPU()) {
                    if (game.getCPU().getPlayer()) {
                        TTTAI.makeCPUMove(game);
                    }
                }
            }
            else {
                int[] tile = getTile(req, game);
                if (game.checkValid(tile[0], tile[1])) {
                    game.move(tile[0], tile[1]);
                }
                TTTAI.makeCPUMove(game);
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

    private static TicTacToe createTTTGame(Request req, Response res, int edgeLength, boolean player, int difficulty) {
        User user = currentSessionUser(req, res);
        user.createTTT(edgeLength, player, difficulty);
        return user.getTttGame();
    }

    private static TicTacToe createTTTGame(Request req, Response res, int edgeLength) {
        User user = currentSessionUser(req, res);
        user.createTTT(edgeLength);
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

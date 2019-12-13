package user;

import database.Database;
import tictactoe.TicTacToe;

public class User {
    long id;
    TicTacToe tttGame = null;

    public User (Database db) {
        this.id = db.generateKey();
        db.addUser(this);
    }

    public long getId() {
        return this.id;
    }

    public void createTTT(int edgeLength) {
        this.tttGame = new TicTacToe(edgeLength);
    }

    public void createTTT(int edgeLength, boolean player, int difficulty) {
        this.tttGame = new TicTacToe(edgeLength, player, difficulty);
    }

    public TicTacToe getTttGame() {
        return this.tttGame;
    }
}

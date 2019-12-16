package user;

import checkers.Checkers;
import database.Database;
import tictactoe.TicTacToe;

public class User {
    long id;
    TicTacToe tttGame = null;
    Checkers checkers = null;

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

    public void createCheckers() {
        this.checkers = new Checkers();
    }

    public Checkers getCheckers() {
        return this.checkers;
    }
}

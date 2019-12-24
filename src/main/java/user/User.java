package user;

import checkers.Checkers;
import database.Database;
import spark.Session;
import tictactoe.TicTacToe;

public class User {
    long id;
    Session session;
    TicTacToe tttGame = null;
    Checkers checkers = null;

    public User (Database db, Session session) {
        this.id = db.generateKey();
        this.session = session;
        db.addUser(this);
    }

    public User (Database db, Session session, Long id) {
        this.id = id;
        this.session = session;
        db.addUser(this);
    }

    public long getId() {
        return this.id;
    }

    public Session getSession() {
        return this.session;
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

    public void createCheckers(boolean player, int difficulty) {
        this.checkers = new Checkers(player, difficulty);
    }

    public Checkers getCheckers() {
        return this.checkers;
    }
}

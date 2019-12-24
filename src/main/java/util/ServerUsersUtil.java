package util;

import database.Database;
import spark.*;
import user.User;

public class ServerUsersUtil {
    public static User createUser(Request req, Response res) {
        Database db = Database.getGlobalDB();
        Session session = req.session(true);
        User user = new User(db, session);
        res.cookie("id", Long.toString(user.getId()));
        return user;
    }

    public static User createUser(Request req, Long id) {
        Database db = Database.getGlobalDB();
        Session session = req.session(false);
        User user = new User(db, session, id);
        return user;
    }
}

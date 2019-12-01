package index;

import database.Database;
import spark.*;
import user.User;

import static util.ViewUtil.*;

public class IndexController {
    public static Route handleIndexGet(String templatePath) {
        return (Request req, Response res) -> {
            Database db = Database.getGlobalDB();
            User user = new User(db);
            res.cookie("id", Long.toString(user.getId()));
            return renderContent(templatePath);
        };
    }
}

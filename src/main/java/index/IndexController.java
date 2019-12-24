package index;

import spark.*;

import static util.ServerUsersUtil.*;

import static util.ViewUtil.*;

public class IndexController {
    public static Route handleIndexGet(String templatePath) {
        return (Request req, Response res) -> {
            createUser(req, res);
            return renderContent(templatePath);
        };
    }
}

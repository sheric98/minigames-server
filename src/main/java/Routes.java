import static spark.Spark.*;
import static tictactoe.TTTController.*;
import static index.IndexController.*;
import spark.template.velocity.VelocityTemplateEngine;


public class Routes {
    public static void main(String[] args) {
        port(1234);
        staticFiles.location("/css");

        get("/", handleIndexGet("layout/index.html"));
        get("/TTT", handleTTTGet("layout/ttt.vm"), new VelocityTemplateEngine());
        post("/TTT", handleTTTPost("layout/ttt.vm"), new VelocityTemplateEngine());

        get("/butt", (req, res) -> "don't go to this link");
    }
}

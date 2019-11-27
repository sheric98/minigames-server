import static spark.Spark.*;
import static util.ViewUtil.*;

public class Routes {
    public static void main(String[] args) {
        port(1234);

        get("/", (req, res) -> renderContent("layout/index.html"));
        get("/hello", (req, res) -> "I Love Nami");

        get("/butt", (req, res) -> "don't go to this link");
    }
}

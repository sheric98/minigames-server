package util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.velocity.*;
import org.eclipse.jetty.http.*;
import spark.*;
import spark.template.velocity.*;
import java.util.*;



public class ViewUtil {
    public static String renderContent(String htmlFile) {
        try {
            URL url = ViewUtil.class.getResource("../" + htmlFile);

            Path path = Paths.get(url.toURI());

            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ModelAndView updateModel(Map<String, Object> model, String templatePath) {
        ModelAndView mnv = new ModelAndView(model, templatePath);
        return mnv;
    }
}

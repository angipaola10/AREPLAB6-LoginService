package edu.escuelaing.arep;

import edu.escuelaing.arep.model.User;

import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;
import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;

public class LoginService {

    static Map<String, String> users = new HashMap<>();

    public static void main(String[] args){

        users.put("angi", DigestUtils.md5Hex("angi123"));
        users.put("paola",  DigestUtils.md5Hex("paola456"));
        try {
            SecureURLReader.enableConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        port(getPort());
        staticFileLocation("/");
        secure("keystore/arepkeystore.p12", "123456", null, null);


        before("/private/*", (req, res) ->{
            req.session(true);
            if(req.session().isNew())
                req.session().attribute("isLogged",false);
            if(req.session().attribute("isLogged"))
                return;
            halt(401, "<h1>401 Unauthorized</h1>");
        });

        get("/", (req, res) -> {
            res.redirect("/login.html");
            res.status(200);
            return null;
        });

        post("/login", (req, res) -> {
            req.session(true);
            User user = (new Gson()).fromJson(req.body(), User.class);
            if (users.containsKey(user.getUsername()) && users.get(user.getUsername()).equals(DigestUtils.md5Hex(user.getPassword()))){
                req.session().attribute("User", user.getUsername());
                req.session().attribute("isLogged", true);
                res.status(200);
                return "logged";
            }
            res.status(404);
            return "not logged";
        });

        get("/private/otherService",(request, response) -> {
            try{
                return "<h1>"+ SecureURLReader.readURL("https://localhost:5001/otherService")+"</h1>";
            }
            catch(Exception e){
                e.printStackTrace();
            }
            response.status(500);
            return "<h1>500 Internal Server Error</h1>";
        });

    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 5000;
    }
}

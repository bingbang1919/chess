package server;
import com.google.gson.Gson;
import dataaccess.DataAccessObjects;
import model.AuthData;
import model.GameData;
import model.UserData;
import requestResponseModels.*;
import service.*;
import service.Service;
import spark.*;

public class Server {

    private DataAccessObjects AuthDAO;
    private DataAccessObjects GameDAO;
    private DataAccessObjects UserDAO;

    public Server() {
        UserDAO = null;
        GameDAO = null;
        AuthDAO = null;
    }

    public void setAuthDAO(DataAccessObjects dao) {
        AuthDAO = dao;
    }
    public void setGameDAO(DataAccessObjects dao) {
        GameDAO = dao;
    }
    public void setUserDAO(DataAccessObjects dao) {
        UserDAO = dao;
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) {
        final Gson gson = new Gson();
        try {
            Service service = new Service();
            service.clear(AuthDAO, GameDAO, UserDAO);
            res.status(200);
            return "{}";
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(e.getMessage());
        }
    }
    private Object register(Request req, Response res) {
        final Gson gson = new Gson();
        var userRequest = new Gson().fromJson(req.body(), registerRequest.class);
        try {
            UserService service = new UserService();
//            AuthData data = service.register()
            res.status(200);
            return new Gson().toJson(userRequest);
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(e.getMessage());
        }
    }
    private Object login(Request req, Response res) {
        throw new RuntimeException("Not implemented");
    }
    private Object logout(Request req, Response res) {
        throw new RuntimeException("Not implemented");
    }
    private Object listGames(Request req, Response res) {
        throw new RuntimeException("Not implemented");
    }
    private Object createGame(Request req, Response res) {
        throw new RuntimeException("Not implemented");
    }
    private Object joinGame(Request req, Response res) {
        throw new RuntimeException("Not implemented");
    }

}

package server;
import com.google.gson.Gson;
import dataaccess.DataAccessObjects;
import model.*;
import service.*;
import service.Service;
import spark.*;

public class Server {

    private DataAccessObjects.AuthDAO AuthDAO;
    private DataAccessObjects.GameDAO GameDAO;
    private DataAccessObjects.UserDAO UserDAO;

    public Server() {
        UserDAO = null;
        GameDAO = null;
        AuthDAO = null;
    }

    public void setAuthDAO(DataAccessObjects.AuthDAO dao) {
        AuthDAO = dao;
    }
    public void setGameDAO(DataAccessObjects.GameDAO dao) {
        GameDAO = dao;
    }
    public void setUserDAO(DataAccessObjects.UserDAO dao) {
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
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
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
        var userRequest = new Gson().fromJson(req.body(), UserData.class);
        try {
            UserService service = new UserService();
            AuthData response = service.register(userRequest, AuthDAO, UserDAO);
            res.status(200);
            return new Gson().toJson(response);
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(e.getMessage());
        }
        // TODO: you need to write the error stuff for all of this. Not just 500.
    }
    private Object login(Request req, Response res) {
        final Gson gson = new Gson();
        var userRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            UserService service = new UserService();
            AuthData response = service.login(userRequest, AuthDAO, UserDAO);
            res.status(200);
            return new Gson().toJson(response);
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(e.getMessage());
        }
        // TODO: you need to write the error stuff for all of this. Not just 500.
    }
    private Object logout(Request req, Response res) {
        final Gson gson = new Gson();
        SingleAuthentication userRequest = new SingleAuthentication(req.headers("authorization"));
        try {
            UserService service = new UserService();
            service.logout(userRequest, AuthDAO, UserDAO);
            res.status(200);
            return "{}";
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(e.getMessage());
        }
        // TODO: you need to write the error stuff for all of this. Not just 500.
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

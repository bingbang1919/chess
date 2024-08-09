package server;
import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.*;
import service.*;
import service.Service;
import spark.*;
import java.util.Collection;

public class Server {

    private DataAccessObjects.AuthDAO authDao;
    private DataAccessObjects.GameDAO gameDao;
    private DataAccessObjects.UserDAO userDao;
    private int gameCounter = 1;
    private WebSocketHandler webSocketHandler;
    public Server() {
        try {
            userDao = SequelUserDAO.getInstance();
            gameDao = SequelGameDAO.getInstance();
            authDao = SequelAuthDAO.getInstance();
            DatabaseManager.configureDatabase();
            webSocketHandler = new WebSocketHandler(userDao, gameDao, authDao);
        } catch (Exception e) {
            System.out.println("Something went terribly wrong");
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.webSocket("/ws", webSocketHandler);
        Spark.staticFiles.location("web");
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

    public Object clear(Request req, Response res) {
        final Gson gson = new Gson();
        try {
            Service service = new Service();
            service.clear(authDao, gameDao, userDao);
            res.status(200);
            return "{}";
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
    private Object register(Request req, Response res) {
        final Gson gson = new Gson();
        var userRequest = new Gson().fromJson(req.body(), UserData.class);
        try {
            UserService service = new UserService();
            AuthData response = service.register(userRequest, authDao, userDao);
            res.status(200);
             return new Gson().toJson(response);
        } catch (DataAccessException e) {
            res.status(403);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (IllegalAccessException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object login(Request req, Response res) {
        final Gson gson = new Gson();
        var userRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try {
            UserService service = new UserService();
            AuthData response = service.login(userRequest, authDao, userDao);
            res.status(200);
            return new Gson().toJson(response);
        } catch (DataAccessException e) {
            res.status(401);
            ErrorResponse response = new ErrorResponse("Error: unauthorized");
            return gson.toJson(response);
        } catch (Exception e) {
            res.status(500);
            ErrorResponse response = new ErrorResponse(e.getMessage());
            return gson.toJson(response);
        }
    }

    private Object logout(Request req, Response res) {
        final Gson gson = new Gson();
        SingleAuthentication userRequest = new SingleAuthentication(req.headers("authorization"));
        try {
            UserService service = new UserService();
            service.logout(userRequest, authDao);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object listGames(Request req, Response res) {
        final Gson gson = new Gson();
        SingleAuthentication userRequest = new SingleAuthentication(req.headers("authorization"));
        try {
            GameService service = new GameService();
            Collection<GameData> gameList = service.listGames(userRequest, authDao, gameDao);
            res.status(200);
            return gson.toJson(new ListGamesResponse(gameList));
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object createGame(Request req, Response res) {
        final Gson gson = new Gson();
        String token = req.headers("authorization");
        var request = new Gson().fromJson(req.body(), CreateGameRequest.class);
        try {
            GameService service = new GameService();
            GameData data = service.createGame(gameCounter, token, request, authDao, gameDao);
            res.status(200);
            gameCounter += 1;
            return gson.toJson(new CreateGameResponse(data.gameID()));
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (IllegalArgumentException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }
    private Object joinGame(Request req, Response res) {
        final Gson gson = new Gson();
        String token = req.headers("authorization");
        var request = new Gson().fromJson(req.body(), JoinGameRequest.class);
        try {
            GameService service = new GameService();
            service.joinGame(request, token, authDao, gameDao);
            res.status(200);
            return "{}";
        } catch (DataAccessException e) {
            res.status(401);
            return gson.toJson(new ErrorResponse("Error: unauthorized"));
        } catch (IllegalArgumentException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        } catch (IllegalAccessException e) {
            res.status(403);
            return gson.toJson(new ErrorResponse("Error: already taken"));
        }
        catch (Exception e) {
            res.status(500);
            return gson.toJson(e.getMessage());
        }
    }
}

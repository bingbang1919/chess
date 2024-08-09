package ui;


import chess.ChessGame.TeamColor;

import model.*;
import java.util.*;

public class ChessClient {

    public ServerFacade getFacade() {
        return facade;
    }

    private final ServerFacade facade;
    public String authToken = null;
    public Map<Integer, Integer> registeredGames = null;
    private final WebSocketClient wbClient;
    public ChessClient(String url) throws Exception {
        facade = new ServerFacade(url, this);
        wbClient = new WebSocketClient(url);
    }

    public String eval(String input) {
        boolean isLoggedIn = PreloginREPL.isLoggedIn;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            String output;
            if (isLoggedIn) {
                output = switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> playGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logout();
                    case "quit" -> quit();
                    default -> help();
                };
            } else {
                output = switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> quit();
                    default -> help();
                };
            }
            return output;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String help() {
        String helpScreen;
        if (PreloginREPL.isLoggedIn) {
            helpScreen = """
                    
                    create <NAME>
                    list
                    join <ID> <WHITE|BLACK>
                    observe <ID>
                    logout
                    quit
                    help
                    
                    """;

        } else {
            helpScreen = """
                    
                    register <USERNAME> <PASSWORD> <EMAIL>
                    login <USERNAME> <PASSWORD>
                    quit
                    help
                    
                    """;
        }
        return helpScreen;
    }

    public String quit() {
        PreloginREPL.isLoggedIn = false;
        return "quit";
    }

    public String login(String ... params) throws Exception {
        AuthData authentication;
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            LoginRequest request = new LoginRequest(username, password);
            authentication = facade.login(request);
            authToken = authentication.authToken();
            wbClient.authtoken = authToken;
        } else {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
        return "LOGGEDIN";
    }

    public String register(String ... params) throws Exception {
        AuthData authentication;
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            UserData request = new UserData(username, password, email);
            authentication = facade.register(request);
            authToken = authentication.authToken();
            wbClient.authtoken = authToken;
        } else {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
        return "LOGGEDIN";
    }

    public String logout() throws Exception {
        facade.logout();
        authToken = null;
        wbClient.authtoken = null;
        PreloginREPL.isLoggedIn = false;
        return "Logged out";
    }

    public String createGame(String ... params) throws Exception {
        String name = params[0];
        if (params.length == 1) {
            facade.createGame(name);
            return "Created game.";
        }
        else {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
    }

    public String listGames() throws Exception {
        Map<Integer, Integer> gameMap = new HashMap<>();
        ArrayList<GameData> games = (ArrayList<GameData>) facade.listGames();
        String returnString = "";
        if (games.isEmpty()) {
            return "There are no games available.";
        }
        for (int i=0; i<games.size(); i++) {
            GameData game;
            game = games.get(i);
            Integer gamenum = i+1;
            gameMap.put(gamenum, game.gameID());
            String users = game.whiteUsername() + ", " + game.blackUsername();
            returnString = returnString + "Game Number " + gamenum + ": " + game.gameName() + '\n' + "Players (white, black): " + users + "\n\n" ;
        }
        registeredGames = gameMap;
        return returnString;
    }

    public String playGame(String ... params) throws Exception {
        if (params.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
        if (registeredGames == null) {
            throw new Exception("You must list games first.");
        }
        if (isInteger(params[0])){
            Integer gameID = Integer.valueOf(params[0]);
            TeamColor color = switch (params[1]) {
                case "black" -> TeamColor.BLACK;
                case "white" -> TeamColor.WHITE;
                default -> throw new IllegalStateException("Second argument must be a chess team color: <BLACK|WHITE>");
            };
            wbClient.gameID = gameID;
            facade.joinGame(registeredGames.get(gameID), color);
        }
        else {
            throw new IllegalArgumentException("Game ID must be an integer.");
        }

        new GameplayREPL(wbClient).run();
        return "Successfully joined game #" + params[0];
    }

    public String observeGame(String ... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }
        if (isInteger(params[0])) {
            int gameID = Integer.parseInt(params[0]);
            new GameplayREPL(wbClient).run();
            return "Observing game #" + gameID;
        }
        else {
            throw new IllegalArgumentException("Game ID must be an integer");
        }
    }

    boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e) {
            return false;
        }
    }
}

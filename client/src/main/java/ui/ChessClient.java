package ui;

import java.util.Arrays;
import static ui.EscapeSequences.*;

public class ChessClient {




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
                    
                    create NAME
                    list
                    join ID WHITE|BLACK
                    observe ID
                    logout
                    quit
                    help
                    
                    """;

        } else {
            helpScreen = """
                    
                    register USERNAME PASSWORD EMAIL
                    login USERNAME PASSWORD
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

    public String login(String ... params) {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];

        } else {
            throw new IllegalArgumentException("Hey you tried to log in, but there were a wrong number of arguments.");
        }

    }

    public String register(String ... params) {
        return null;

    }

    public String logout() {
        PreloginREPL.isLoggedIn = false;
        return null;
    }

    public String createGame(String ... params) {
        return null;

    }

    public String listGames() {
        return null;

    }

    public String playGame(String ... params) {
        return null;

    }

    public String observeGame(String ... params) {
        return null;
    }
}

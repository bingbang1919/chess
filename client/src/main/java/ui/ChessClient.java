package ui;

import java.util.Arrays;

public class ChessClient {




    public String eval(String input, boolean isLoggedIn) {
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
                    default -> help(isLoggedIn);
                };
            } else {
                output = switch (cmd) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> quit();
                    default -> help(isLoggedIn);
                };
            }
            return output;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String help(boolean isLoggedIn) {
        return null;
    }

    public String quit() {
        return null;
    }

    public String login(String ... params) {
        return null;

    }

    public String register(String ... params) {
        return null;

    }

    public String logout() {
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

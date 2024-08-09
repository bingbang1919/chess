package ui;

import java.util.Arrays;

public class WebSocketClient {

    private final WebSocketServerFacade facade;
    public String authtoken = null;
    public WebSocketClient(String url) {this.facade = new WebSocketServerFacade(url, this);}


    public String eval(String input) {
        boolean isLoggedIn = PreloginREPL.isLoggedIn;
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            String output = null;
            return output;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

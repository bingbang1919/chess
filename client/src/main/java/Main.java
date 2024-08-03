import chess.*;
import ui.PreloginREPL;

public class Main {
    public static String authToken;
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new PreloginREPL(serverUrl).run();
    }

}
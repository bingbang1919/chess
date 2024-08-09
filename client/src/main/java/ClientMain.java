import ui.ChessClient;
import ui.PreloginREPL;

public class ClientMain {
    public static void main(String[] args) throws Exception {
        var serverUrl = "http://localhost:7389";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new PreloginREPL(serverUrl, new ChessClient(serverUrl)).run();
    }

}
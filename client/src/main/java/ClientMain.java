import chess.ChessBoard;
import ui.ChessClient;
import ui.GameplayREPL;
import ui.NewerGameplayREPL;
import ui.PreloginREPL;

public class ClientMain {
    public static void main(String[] args) {
//        var serverUrl = "http://localhost:7389";
//        if (args.length == 1) {
//            serverUrl = args[0];
//        }
//        new PreloginREPL(serverUrl, new ChessClient(serverUrl)).run();
        new NewerGameplayREPL().run(new ChessBoard(), true);
        new NewerGameplayREPL().run(new ChessBoard(), false);
    }

}
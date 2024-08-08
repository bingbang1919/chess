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
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        new NewerGameplayREPL().run(board, true);
        new NewerGameplayREPL().run(board, false);
    }

}
import chess.*;
import dataaccess.DataAccessObjects;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import server.Server;

//public class Main {
//    public static void main(String[] args) {
//        Spark.get("/hello", (req, res) -> "Hello BYU!");
//    }
//}

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(8080);
    }
}
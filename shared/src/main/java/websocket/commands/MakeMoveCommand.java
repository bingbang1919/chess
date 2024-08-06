package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    ChessMove move;
    public MakeMoveCommand(CommandType type, String token, Integer id, ChessMove move) {
        super(type, token, id);
        this.move = move;
    }
}

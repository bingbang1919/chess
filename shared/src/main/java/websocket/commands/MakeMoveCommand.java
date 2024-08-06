package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public MakeMoveCommand(CommandType type, String token, Integer id, ChessMove move) {
        super(type, token, id);
    }
}

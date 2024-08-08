package server;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DataAccessObjects;
import websocket.commands.ConnectCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class WebSocketService {
    /**
     Root Client sends CONNECT:
         1.  Server sends a LOAD_GAME message back to the root client.
         2.  Server sends a Notification message to all other clients in that game informing them that the root client
            connected to the game, either as a player (in which case their color must be specified) or as an observer.
     */
    public LoadGameMessage connect(ConnectCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        // TODO: needs to grab the game from the database, and return the game.
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();
        try {
            authDao.getAuth(authToken);
        } catch (DataAccessException e) {
            throw new DataAccessException("Illegal access");
        }
        ChessGame game = gameDao.getGame(gameID).game();
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
    }

    /**
     Root Client sends MAKE_MOVE
         1. Server verifies the validity of the move.
         2. Game is updated to represent the move. Game is updated in the database.
         3. Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
         4. Server sends a Notification message to all other clients in that game informing them what move was made.
         5. If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.
     */
    public LoadGameMessage makeMove(ConnectCommand command) {
        // TODO: needs to get the game from the database, execute the makeMove function, then return the LoadGameMessage
        throw new RuntimeException("Not yet implemented");
    }

    /**
     Root Client sends LEAVE
         1. If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
         2. Server sends a Notification message to all other clients in that game,
            informing them that the root client left. This applies to both players and observers.
     */
    public NotificationMessage resign(ConnectCommand command) {
        // TODO: need to grab the game, remove the user from it's respective color, then update the game.
        throw new RuntimeException("Not yet implemented");
    }

    /**
     Root Client sends RESIGN
         1. Server marks the game as over (no more moves can be made). Game is updated in the database.
         2. Server sends a Notification message to all clients in that game informing them that the root client resigned.
            This applies to both players and observers.
     */
    public NotificationMessage leave(ConnectCommand command) {
        // TODO: Develop a way to mark the game as over? Update the game.
        throw new RuntimeException("Not yet implemented");
    }
}

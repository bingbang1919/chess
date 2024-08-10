package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.DataAccessObjects;
import model.AuthData;
import model.GameData;
import websocket.commands.*;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Objects;

public class WebSocketService {
    /**
     Root Client sends CONNECT:
         1.  Server sends a LOAD_GAME message back to the root client.
         2.  Server sends a Notification message to all other clients in that game informing them that the root client
            connected to the game, either as a player (in which case their color must be specified) or as an observer.
     */
    public LoadGameMessage connect(ConnectCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        // TODO: needs to grab the game from the database, and return the game.
        int gameID = command.getGameID();
        authenticate(command, authDao);
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
    public LoadGameMessage makeMove(MakeMoveCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) throws DataAccessException, InvalidMoveException {
        String username = authenticate(command, authDao);
        int gameID = command.getGameID();
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        String gamename = gameData.gameName();
        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();
        if (game.isFinished) {
            String message;
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                message = whiteUser + " (white) is in checkmate.";}
            else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                message = blackUser + " (black) is in checkmate.";}
            else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                message = "Stalemate";}
            else {
                message = "For some reason the game was marked as finished but not in stale/checkmate";}
            throw new InvalidMoveException("Error, This game is finished: " + message);}
        game.makeMove(command.getMove());
        gameDao.removeGame(gameID);
        gameDao.addGame(new GameData(gameID, whiteUser, blackUser, gamename, game));
        return new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
    }

    /**
     Root Client sends RESIGN
         1. Server marks the game as over (no more moves can be made). Game is updated in the database.
         2. Server sends a Notification message to all clients in that game informing them that the root client resigned.
         This applies to both players and observers.
     */
    public NotificationMessage resign(ResignCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        String username = authenticate(command, authDao);
        int gameID = command.getGameID();
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        game.isFinished = true;
        String gamename = gameData.gameName();
        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();
        gameData = new GameData(gameID, whiteUser, blackUser, gamename, game);
        gameDao.removeGame(gameID);
        gameDao.addGame(gameData);
        return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " resigned from " + gamename);
    }

    /**
     Root Client sends LEAVE
         1. If a player is leaving, then the game is updated to remove the root client. Game is updated in the database.
         2. Server sends a Notification message to all other clients in that game,
         informing them that the root client left. This applies to both players and observers.
     */
    public NotificationMessage leave(LeaveCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        String username = authenticate(command, authDao);
        int gameID = command.getGameID();
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        String gamename = gameData.gameName();
        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();
        if (gameData.blackUsername() == username) {blackUser = null;}
        else {whiteUser = null;}
        gameData = new GameData(gameID, whiteUser, blackUser, gamename, game);
        gameDao.removeGame(gameID);
        gameDao.addGame(gameData);
        return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, username + " left " + gamename);
    }

    private String authenticate(UserGameCommand command, DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        String authToken = command.getAuthToken();
        try {
            return authDao.getAuth(authToken).username();
        } catch (DataAccessException e) {
            throw new DataAccessException("Illegal access");
        }
    }
}

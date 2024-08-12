package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.DataAccessObjects;
import model.GameData;
import server.Pair;
import websocket.commands.*;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class WebSocketService {
    /**
     Root Client sends CONNECT:
         1.  Server sends a LOAD_GAME message back to the root client.
         2.  Server sends a Notification message to all other clients in that game informing them that the root client
            connected to the game, either as a player (in which case their color must be specified) or as an observer.
     */
    public Pair<LoadGameMessage, NotificationMessage> connect(ConnectCommand command, DataAccessObjects.GameDAO gameDao,
                                                              DataAccessObjects.AuthDAO authDao) throws DataAccessException {
        int gameID = command.getGameID();
        String username = authenticate(command, authDao);
        GameData gameData = gameDao.getGame(gameID);
        String whiteUser = gameData.whiteUsername();
        String blackUser = gameData.blackUsername();

        ChessGame game = gameData.game();
        LoadGameMessage loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        String note = null;
        if (!Objects.equals(username, blackUser) && !Objects.equals(username, whiteUser)) {
            note = username + " is observing the game.";
        }
        else if (Objects.equals(username, blackUser)) {
            note = username + " has joined the game as the black player";
        }
        else if (Objects.equals(username, whiteUser)) {
            note = username + " has joined the game as the white player";
        }
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, note);
        return new Pair<>(loadMessage, notificationMessage);
    }

    /**
     Root Client sends MAKE_MOVE
         1. Server verifies the validity of the move.
         2. Game is updated to represent the move. Game is updated in the database.
         3. Server sends a LOAD_GAME message to all clients in the game (including the root client) with an updated game.
         4. Server sends a Notification message to all other clients in that game informing them what move was made.
         5. If the move results in check, checkmate or stalemate the server sends a Notification message to all clients.
     */
    public Pair<LoadGameMessage, NotificationMessage> makeMove(MakeMoveCommand command, DataAccessObjects.GameDAO gameDao,
                                                               DataAccessObjects.AuthDAO authDao)
            throws DataAccessException, InvalidMoveException, IllegalAccessException {
        String username = authenticate(command, authDao);
        int gameID = command.getGameID();
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        String gamename = gameData.gameName();
        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();
        if (!username.equals(blackUser) && !username.equals(whiteUser)) {
            throw new IllegalAccessException("An observer cannot make a move.");
        }
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
            throw new InvalidMoveException("This game is finished: " + message);
        }
        if ((game.getTeamTurn() == ChessGame.TeamColor.WHITE && !username.equals(whiteUser)) ||
                (game.getTeamTurn() == ChessGame.TeamColor.BLACK && !username.equals(blackUser))) {
            throw new InvalidMoveException("Not your turn.");
        }
        if ((game.getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor() == ChessGame.TeamColor.BLACK &&
                !username.equals(blackUser)) || (game.getBoard().getPiece(command.getMove().getStartPosition()).getTeamColor() ==
                ChessGame.TeamColor.WHITE && !username.equals(whiteUser))) {
            throw new InvalidMoveException("That's not your piece");
        }
        game.makeMove(command.getMove());
        LoadGameMessage loadGameMessage =  new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        gameDao.removeGame(gameID);
        gameDao.addGame(new GameData(gameID, whiteUser, blackUser, gamename, game));
        if (game.isFinished) {
            String message;
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                message = whiteUser + " (white) is in checkmate.";}
            else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                message = blackUser + " (black) is in checkmate.";}
            else if (game.isInStalemate(ChessGame.TeamColor.WHITE) || game.isInStalemate(ChessGame.TeamColor.BLACK)) {
                message = "Stalemate";}
            else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                message = whiteUser + " put " + blackUser + " into check.";
            } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                message = blackUser + " put " + whiteUser + " into check.";
            } else {
                message = "For some reason the game was marked as finished but not in stale/checkmate";
            }
            return new Pair<>(loadGameMessage, new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
        }
        String piece = switch (game.getBoard().getPiece(command.getMove().getEndPosition()).getPieceType()) {
            case PAWN -> "pawn";
            case ROOK -> "rook";
            case KNIGHT -> "knight";
            case BISHOP -> "bishop";
            case QUEEN -> "queen";
            case KING -> "king";
        };
        String moveMessage = username + " moved their " + piece;
        NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, moveMessage);
        return new Pair<>(loadGameMessage, notificationMessage);
    }

    /**
     Root Client sends RESIGN
         1. Server marks the game as over (no more moves can be made). Game is updated in the database.
         2. Server sends a Notification message to all clients in that game informing them that the root client resigned.
         This applies to both players and observers.
     */
    public NotificationMessage resign(ResignCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao)
            throws DataAccessException, IllegalAccessException {
        String username = authenticate(command, authDao);
        int gameID = command.getGameID();
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        String gamename = gameData.gameName();
        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();
        if (!username.equals(blackUser) && !username.equals(whiteUser)) {
            throw new IllegalAccessException("An observer cannot resign.");
        }
        if (game.isFinished) {
            throw new IllegalAccessException("This game is already finished.");
        }
        game.isFinished = true;
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
    public NotificationMessage leave(LeaveCommand command, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao)
            throws DataAccessException {
        String username = authenticate(command, authDao);
        System.out.println(username);
        int gameID = command.getGameID();
        GameData gameData = gameDao.getGame(gameID);
        ChessGame game = gameData.game();
        String gamename = gameData.gameName();
        String blackUser = gameData.blackUsername();
        String whiteUser = gameData.whiteUsername();
        if (!Objects.equals(username, blackUser) && !Objects.equals(username, whiteUser)) {
            return new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                    username + " has stopped observing.");
        }
        if (blackUser != null && gameData.blackUsername().equals(username)) {blackUser = null;}
        else if (whiteUser != null && gameData.whiteUsername().equals(username)) {whiteUser = null;}
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

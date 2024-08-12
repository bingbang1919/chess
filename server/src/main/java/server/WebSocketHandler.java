package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessObjects;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.*;
import websocket.messages.ServerMessage;
import server.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;



@WebSocket
public class WebSocketHandler {
    private DataAccessObjects.UserDAO userDao;
    private DataAccessObjects.GameDAO gameDao;
    private DataAccessObjects.AuthDAO authDao;
    private final HashMap<Integer, HashSet<Session>> connections = new HashMap<>();

    public WebSocketHandler(DataAccessObjects.UserDAO userDao, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) {
        this.authDao = authDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        Gson gson = new Gson();
        switch (gson.fromJson(message, UserGameCommand.class).getCommandType()) {
            case CONNECT -> connect(session, gson.fromJson(message, ConnectCommand.class));
            case MAKE_MOVE -> makeMove(session, gson.fromJson(message, MakeMoveCommand.class));
            case LEAVE -> leave(session, gson.fromJson(message, LeaveCommand.class));
            case RESIGN -> resign(session, gson.fromJson(message, ResignCommand.class));
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
    }

    private void connect(Session session, ConnectCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            Pair<LoadGameMessage, NotificationMessage> pair = service.connect(command, gameDao, authDao);
            LoadGameMessage loadGameMessage = pair.getFirst();
            NotificationMessage notification = pair.getSecond();
            Integer gameID = command.getGameID();
            // Checks if the game already has a set to its game id, adds the session.
            if (!connections.containsKey(gameID)) {connections.put(gameID, new HashSet<>());}
            connections.get(gameID).add(session);
            session.getRemote().sendString(new Gson().toJson(loadGameMessage));
            notifyAll(gameID, notification, session);
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }
    private void makeMove(Session session, MakeMoveCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            Pair<LoadGameMessage, NotificationMessage> pair = service.makeMove(command, gameDao, authDao, connections, session);
            if (pair!=null) {
                NotificationMessage notificationMessage = pair.getSecond();
                LoadGameMessage message = pair.getFirst();
                if (notificationMessage != null) {
                    notifyAll(command.getGameID(), notificationMessage, null);
                }
                notifyAll(command.getGameID(), message, null);
            }
        } catch (RuntimeException e) {
            NotificationMessage notificationMessage = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, e.getMessage());
            notifyAll(command.getGameID(), notificationMessage, null);
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }
    private void leave(Session session, LeaveCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            NotificationMessage message = service.leave(command, gameDao, authDao);
            notifyAll(command.getGameID(), message, session);
            connections.get(command.getGameID()).remove(session);
        } catch (Exception e) {
            System.out.print(e.getMessage());
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }

    private void resign(Session session, ResignCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            NotificationMessage message = service.resign(command, gameDao, authDao);
            notifyAll(command.getGameID(), message, null);
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }

    private void notifyAll(Integer gameID, ServerMessage message, Session excludedSession) throws IOException {
        notifyAll(gameID, message, excludedSession, connections);
    }

    static void notifyAll(Integer gameID, ServerMessage message, Session excludedSession,
                          HashMap<Integer, HashSet<Session>> connections) throws IOException {
        HashSet<Session> sessions = connections.get(gameID);
        Session[] sessionArray = sessions.toArray(new Session[0]);
        for (Session session : sessionArray) {
            if (!session.equals(excludedSession)) {session.getRemote().sendString(new Gson().toJson(message));}
        }
    }
}

package server;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;



@WebSocket
public class WebSocketHandler {
    private DataAccessObjects.UserDAO userDao;
    private DataAccessObjects.GameDAO gameDao;
    private DataAccessObjects.AuthDAO authDao;
    private final HashMap<Integer, Set<Session>> connections = new HashMap<>();

    public WebSocketHandler(DataAccessObjects.UserDAO userDao, DataAccessObjects.GameDAO gameDao, DataAccessObjects.AuthDAO authDao) {
        this.authDao = authDao;
        this.userDao = userDao;
        this.gameDao = gameDao;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("received message: " + message);
        Gson gson = new Gson();
        switch (gson.fromJson(message, UserGameCommand.class).getCommandType()) {
            case CONNECT -> connect(session, gson.fromJson(message, ConnectCommand.class));
            case MAKE_MOVE -> makeMove(session, new Gson().fromJson(message, MakeMoveCommand.class));
            case LEAVE -> leave(session, gson.fromJson(message, LeaveCommand.class));
            case RESIGN -> resign(session, gson.fromJson(message, ResignCommand.class));
        }
    }

//    @OnWebSocketError
//    public void onError(Session session, Throwable e) {
//        System.out.println(e.getMessage());
//    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println("Status: " + status + ", Reason: " + reason);
    }

    private void connect(Session session, ConnectCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            LoadGameMessage message = service.connect(command, gameDao, authDao);
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }
    private void makeMove(Session session, MakeMoveCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            LoadGameMessage message = service.makeMove(command, gameDao, authDao);
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }
    private void leave(Session session, LeaveCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            NotificationMessage message = service.leave(command, gameDao, authDao);
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }

    private void resign(Session session, ResignCommand command) throws Exception {
        try {
            WebSocketService service = new WebSocketService();
            NotificationMessage message = service.resign(command, gameDao, authDao);
            session.getRemote().sendString(new Gson().toJson(message));
        } catch (Exception e) {
            ErrorMessage message = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage());
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }

}

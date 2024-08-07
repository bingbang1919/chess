package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer, Set<Session>> connections = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("received message: " + message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(session, command);
            case MAKE_MOVE -> makeMove(session, command);
            case LEAVE -> leave(session, command);
            case RESIGN -> resign(session, command);
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println("Status: " + status + ", Reason: " + reason);
    }

    private void connect(Session session, UserGameCommand command) throws Exception {
        session.getRemote().sendString(new Gson().toJson(command));
    }
    private void makeMove(Session session, UserGameCommand command) throws Exception {
        session.getRemote().sendString(new Gson().toJson(command));
    }
    private void leave(Session session, UserGameCommand command) throws Exception {
        session.getRemote().sendString(new Gson().toJson(command));
    }
    private void resign(Session session, UserGameCommand command) throws Exception {
        session.getRemote().sendString(new Gson().toJson(command));
    }

}

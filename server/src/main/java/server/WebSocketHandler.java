package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer, Set<Session>> connections = new HashMap<>();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("received message: " + message);
        session.getRemote().sendString(message);
//        session.getRemote().sendString(message);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable e) {
        e.printStackTrace();
    }

    @OnWebSocketClose
    public void onClose(Session session, int status, String reason) {
        System.out.println("Status: " + status + ", Reason: " + reason);
    }

}

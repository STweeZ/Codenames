package fr.univartois.ili.jai.websocket;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import jakarta.servlet.ServletRequestAttributeListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint("/refresh")
@WebListener
public class Refresh implements ServletRequestAttributeListener {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    public static void refresh(String gameId) {
        for (Session session : sessions) {
            try {
                synchronized (session) {
                    if (session.isOpen()) {
                        session.getBasicRemote().sendText(gameId);
                    }
                }
            } catch (IOException e) {
                sessions.remove(session);
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        sessions.add(session);
        session.setMaxIdleTimeout(0);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
}

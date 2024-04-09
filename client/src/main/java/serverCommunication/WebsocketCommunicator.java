package serverCommunication;

import org.glassfish.tyrus.core.wsadl.model.Endpoint;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
    private final URI connectURI;

    public WebsocketCommunicator(int serverPort, String urlStem) {
        try {
            this.connectURI = new URI("ws://" + urlStem + serverPort + "/connect");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Session getNewSession() throws IOException, DeploymentException {
        WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
        Session session = wsContainer.connectToServer(this, this.connectURI);
        session.addMessageHandler((MessageHandler.Whole<String>) System.out::print);
        return session;
    }

    public void send(Session session, String msg) throws IOException { session.getBasicRemote().sendText(msg); }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

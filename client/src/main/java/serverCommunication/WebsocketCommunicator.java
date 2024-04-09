package serverCommunication;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketCommunicator extends Endpoint {
    private final URI connectURI;
    private Session session;

    public WebsocketCommunicator(int serverPort, String urlStem) {
        try {
            this.connectURI = new URI("ws://" + urlStem + serverPort + "/connect");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public void ensureOpenSession() {
        if (this.session == null) { initalizeSession();
        } else if (!this.session.isOpen()) { initalizeSession(); }
    }

    private void initalizeSession() {
        try {
            this.session = getNewSession();
        } catch (IOException | DeploymentException e) {
            throw new RuntimeException(e);
        }
    }

    private Session getNewSession() throws IOException, DeploymentException {
        WebSocketContainer wsContainer = ContainerProvider.getWebSocketContainer();
        Session session = wsContainer.connectToServer(this, this.connectURI);
        session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
        return session;
    }

    public void closeSession() throws IOException { this.session.close(); }

    public void send(String msg) throws IOException { this.session.getBasicRemote().sendText(msg); }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

package serverCommunication;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

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
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                if (serverMessage.getServerMessageType() != ServerMessage.ServerMessageType.LOAD_GAME) {
                    System.out.println(serverMessage.getServerMessageType() + ": " + serverMessage.getServerMessageValue());
                } else { System.out.println(("LOAD_GAME received, I'll implement that later")); }
            }
        });
        return session;
    }

    public void closeSession() throws IOException { this.session.close(); }

    public void sendCommand(UserGameCommand command) throws IOException {
        String commandStr = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(commandStr);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

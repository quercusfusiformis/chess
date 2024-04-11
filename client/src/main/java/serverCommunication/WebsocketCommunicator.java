package serverCommunication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import model.GameData;
import webSocketMessages.userCommands.*;
import webSocketMessages.serverMessages.*;

public class WebsocketCommunicator extends Endpoint {
    private final URI connectURI;
    private Session session;
    private GameData gameData;

    public WebsocketCommunicator(int serverPort, String urlStem) {
        try {
            this.connectURI = new URI("ws://" + urlStem + serverPort + "/connect");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void setGameData(GameData gameData) { this.gameData = gameData; }

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
                ServerMessage serverMessage = createMessageSerializer().fromJson(message, ServerMessage.class);
                ServerMessage.ServerMessageType messageType = serverMessage.getServerMessageType();
                switch (messageType) {
                    case LOAD_GAME -> {
                        ServerLoadGameMessage msg = (ServerLoadGameMessage) serverMessage;
                        setGameData(msg.getGameData());
                    }
                    case ERROR -> {
                        ServerErrorMessage msg = (ServerErrorMessage) serverMessage;
                        System.out.println(msg.getServerMessageType() + ": " + msg.getErrorName() + ": " + msg.getErrorMessage());
                    }
                    case NOTIFICATION -> {
                        ServerNotification msg = (ServerNotification) serverMessage;
                        System.out.println(msg.getServerMessageType() + ": " + msg.getNotification());
                    }
                }
            }
        });
        return session;
    }

    private static Gson createMessageSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(ServerMessage.class, (JsonDeserializer<ServerMessage>) (el, type, ctx) -> {
            ServerMessage message = null;
            if (el.isJsonObject()) {
                String messageType = el.getAsJsonObject().get("serverMessageType").getAsString();
                switch (ServerMessage.ServerMessageType.valueOf(messageType)) {
                    case LOAD_GAME -> message = ctx.deserialize(el, ServerLoadGameMessage.class);
                    case ERROR -> message = ctx.deserialize(el, ServerErrorMessage.class);
                    case NOTIFICATION -> message = ctx.deserialize(el, ServerNotification.class);
                }
            }
            return message;
        });
        return gsonBuilder.create();
    }

    public void closeSession() throws IOException { this.session.close(); }

    public void sendCommand(UserGameCommand command) throws IOException {
        String commandStr = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(commandStr);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}

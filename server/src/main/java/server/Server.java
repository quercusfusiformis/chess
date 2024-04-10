package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import spark.Spark;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import httpHandlers.AuthorizationHandler;
import httpHandlers.DatabaseOperationsHandler;
import httpHandlers.GameHandler;
import webSocketMessages.serverMessages.ServerErrorMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerNotification;
import webSocketMessages.userCommands.*;

import java.util.ArrayList;

@WebSocket
public class Server {
    ArrayList<Session> openSessions = new ArrayList<>();

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.webSocket("/connect", Server.class);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> new AuthorizationHandler(req, res).register());
        Spark.post("/session", (req, res) -> new AuthorizationHandler(req, res).login());
        Spark.delete("/session", (req, res) -> new AuthorizationHandler(req, res).logout());
        Spark.get("/game", (req, res) -> new GameHandler(req, res).listGames());
        Spark.post("/game", (req, res) -> new GameHandler(req, res).createGame());
        Spark.put("/game", (req, res) -> new GameHandler(req, res).joinGame());
        Spark.delete("/db", (req, res) -> new DatabaseOperationsHandler(req, res).clear());

        Spark.awaitInitialization();
        return Spark.port();
    }

    @OnWebSocketConnect
    public void onOpen(Session session) throws Exception {
        this.openSessions.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) throws Exception {
        this.openSessions.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = createCommandSerializer().fromJson(message, UserGameCommand.class);
        System.out.println("UserGameCommand: " + command.getCommandType() + " via a command of type " + command.getClass());

        ServerNotification serverNotification = new ServerNotification(ServerMessage.ServerMessageType.NOTIFICATION, "Successful message transmission");
        sendServerMessageAllSessions(serverNotification);
    }

    private static Gson createCommandSerializer() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(UserGameCommand.class, (JsonDeserializer<UserGameCommand>) (el, type, ctx) -> {
            UserGameCommand command = null;
            if (el.isJsonObject()) {
                String commandType = el.getAsJsonObject().get("commandType").getAsString();
                switch (UserGameCommand.CommandType.valueOf(commandType)) {
                    case JOIN_PLAYER -> command = ctx.deserialize(el, JoinPlayerCommand.class);
                    case JOIN_OBSERVER -> command = ctx.deserialize(el, JoinObserverCommand.class);
                    case MAKE_MOVE -> command = ctx.deserialize(el, MakeMoveCommand.class);
                    case LEAVE -> command = ctx.deserialize(el, LeaveGameCommand.class);
                    case RESIGN -> command = ctx.deserialize(el, ResignGameCommand.class);
                }
            }
            return command;
        });
        return gsonBuilder.create();
    }

    private void sendServerMessageToSession(Session session, ServerMessage message) throws Exception {
        String serverMsgStr = new Gson().toJson(message);
        session.getRemote().sendString(serverMsgStr);
    }

    private void sendServerMessageAllSessions(ServerMessage message) throws Exception {
        String serverMsgStr = new Gson().toJson(message);
        for (Session session: this.openSessions) {
            session.getRemote().sendString(serverMsgStr);
        }
    }

    @OnWebSocketError
    public void onError(org.eclipse.jetty.websocket.api.Session session, Throwable error) throws Exception {
        ServerErrorMessage errorMessage = new ServerErrorMessage(ServerMessage.ServerMessageType.ERROR, error.getClass().getName(), error.getMessage());
        sendServerMessageAllSessions(errorMessage);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

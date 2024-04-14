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
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import websocketService.WebsocketService;
import logging.ServerLogger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;

@WebSocket
public class Server {
    private final WebsocketService wsService = new WebsocketService();
    private final HashMap<Integer, HashSet<Session>> openSessionMap = new HashMap<>();

    private void changeSessionGameID(Session session, int updatedGameID) {
        removeSession(session);
        addSessionWithGameID(session, updatedGameID);
    }

    private void addSessionWithGameID(Session session, int updatedGameID) {
        if (this.openSessionMap.containsKey(updatedGameID)) {
            this.openSessionMap.get(updatedGameID).add(session);
        } else {
            this.openSessionMap.put(updatedGameID, new HashSet<>());
            this.openSessionMap.get(updatedGameID).add(session);
        }
    }

    private void removeSession(Session session) {
        int currSessionGame = getSessionGameID(session);
        this.openSessionMap.get(currSessionGame).remove(session);
    }

    private int getSessionGameID(Session session) {
        Integer foundGameID = null;
        for (Map.Entry<Integer, HashSet<Session>> entry: this.openSessionMap.entrySet()) {
            if (entry.getValue().contains(session)) {
                foundGameID = entry.getKey();
            }
        }
        if (foundGameID != null) {
            return foundGameID;
        } else { throw new NoSuchElementException("The requested session (" + session.toString() + ") was not found."); }
    }

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
        addSessionWithGameID(session, -1);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) throws Exception {
        try {
            removeSession(session);
            System.out.println("Session: " + session.toString() + " may have ended suddenly.");
        } catch (NoSuchElementException e) {
            System.out.println("Session ended successfully.");
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = createCommandSerializer().fromJson(message, UserGameCommand.class);
        ServerLogger.logUserCommand(command);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayerCommand jpCommand = (JoinPlayerCommand) command;
                int joinGameID = jpCommand.getGameID();
                changeSessionGameID(session, joinGameID);
                ServerMessage joinMessage = wsService.joinGameAsPlayer(joinGameID, jpCommand.getPlayerColor(), jpCommand.getAuthString());
                if (joinMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                    sendServerMessageToSession(session, joinMessage);
                    ServerLogger.logServerMessage(joinMessage, "session");
                } else {
                    sendServerMessageToOtherPlayers(session, joinMessage);
                    ServerLogger.logServerMessage(joinMessage, "other game players");
                    ServerMessage loadGameMessage = wsService.getGame(joinGameID);
                    sendServerMessageToSession(session, loadGameMessage);
                    ServerLogger.logServerMessage(loadGameMessage, "game " + joinGameID);
                }
            }
            case JOIN_OBSERVER -> {
                JoinObserverCommand joCommand = (JoinObserverCommand) command;
                int observeGameID = joCommand.getGameID();
                changeSessionGameID(session, observeGameID);
                ServerMessage observeMessage = wsService.joinGameAsObserver(observeGameID, joCommand.getAuthString());
                if (observeMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                    sendServerMessageToSession(session, observeMessage);
                    ServerLogger.logServerMessage(observeMessage, "session");
                } else {
                    sendServerMessageToOtherPlayers(session, observeMessage);
                    ServerLogger.logServerMessage(observeMessage, "other game players");
                    ServerMessage loadGameMessage = wsService.getGame(observeGameID);
                    sendServerMessageToSession(session, loadGameMessage);
                    ServerLogger.logServerMessage(loadGameMessage, "session" + observeGameID);
                }
            }
            case LEAVE -> {
                LeaveGameCommand lgCommand = (LeaveGameCommand) command;
                int leaveGameID = getSessionGameID(session);
                ServerMessage leaveMessage = wsService.leaveGame(leaveGameID, lgCommand.getAuthString());
                if (leaveMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                    sendServerMessageToSession(session, leaveMessage);
                    ServerLogger.logServerMessage(leaveMessage, "session");
                } else {
                    sendServerMessageToOtherPlayers(session, leaveMessage);
                    ServerLogger.logServerMessage(leaveMessage, "other game players");
                    removeSession(session);
                }
            }
            case MAKE_MOVE -> {
                MakeMoveCommand mmCommand = (MakeMoveCommand) command;
                int moveGameID = getSessionGameID(session);
                ServerMessage moveMessage = wsService.makeMove(moveGameID, mmCommand.getMove(), mmCommand.getAuthString());
                if (moveMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                    sendServerMessageToSession(session, moveMessage);
                    ServerLogger.logServerMessage(moveMessage, "session");
                } else {
                    sendServerMessageToOtherPlayers(session, moveMessage);
                    ServerLogger.logServerMessage(moveMessage, "other game players");
                    ServerMessage loadGameMessage = wsService.getGame(moveGameID);
                    sendServerMessageToGame(moveGameID, loadGameMessage);
                    ServerLogger.logServerMessage(loadGameMessage, "game " + moveGameID);
                }
                // Add check and checkmate messaging
            }
            case RESIGN -> {
                ResignGameCommand rgCommand = (ResignGameCommand) command;
                int resignGameID = getSessionGameID(session);
                ServerMessage resignMessage = wsService.resign(resignGameID, rgCommand.getAuthString());
                if (resignMessage.getServerMessageType().equals(ServerMessage.ServerMessageType.ERROR)) {
                    sendServerMessageToSession(session, resignMessage);
                    ServerLogger.logServerMessage(resignMessage, "session");
                } else {
                    sendServerMessageToGame(resignGameID, resignMessage);
                    ServerLogger.logServerMessage(resignMessage, "game " + resignGameID);
                }
            }
        }
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

    private void sendServerMessageAllSessions(ServerMessage message) throws Exception {
        for (Integer currGameID: this.openSessionMap.keySet()) {
            sendServerMessageToGame(currGameID, message);
        }
    }

    private void sendServerMessageToGame(int targetGameID, ServerMessage message) throws Exception {
        for (Session session: this.openSessionMap.get(targetGameID)) {
            sendServerMessageToSession(session, message);
        }
    }

    private void sendServerMessageToSession(Session session, ServerMessage message) throws Exception {
        String serverMsgStr = new Gson().toJson(message);
        session.getRemote().sendString(serverMsgStr);
    }

    private void sendServerMessageToOtherPlayers(Session session, ServerMessage message) throws Exception {
        int sessionGameID = getSessionGameID(session);
        for (Session gameSession: this.openSessionMap.get(sessionGameID)) {
            if (gameSession != session) {
                sendServerMessageToSession(gameSession, message);
            }
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

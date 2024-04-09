package server;

import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import handlers.AuthorizationHandler;
import handlers.DatabaseOperationsHandler;
import handlers.GameHandler;

import java.io.IOException;

@WebSocket
public class Server {
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
    public void onConnect(org.eclipse.jetty.websocket.api.Session session) throws IOException {
        session.getRemote().sendString("Websocket connection established. Welcome.\n");
    }

    @OnWebSocketMessage
    public void onMessage(org.eclipse.jetty.websocket.api.Session session, String message) throws IOException {
        session.getRemote().sendString("Message received:" + message + '\n');
    }

    @OnWebSocketClose
    public void onClose(org.eclipse.jetty.websocket.api.Session session) throws IOException {
        session.getRemote().sendString("Websocket connection closed. Goodbye.\n");
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

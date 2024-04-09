package server;

import spark.Spark;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import handlers.AuthorizationHandler;
import handlers.DatabaseOperationsHandler;
import handlers.GameHandler;

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
        Spark.get("/echo/:msg", (req, res) -> "Server Websocket says: " + req.params(":msg") + " received.\n");

        Spark.awaitInitialization();
        return Spark.port();
    }

//    @OnWebSocketConnect
//    public void onOpen(org.eclipse.jetty.websocket.api.Session session) throws Exception {
//        session.getRemote().sendString("Websocket connection established. Welcome.\n");
//    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        session.getRemote().sendString("Message received:" + message + '\n');
    }

//    @OnWebSocketError
//    public void onError(org.eclipse.jetty.websocket.api.Session session, Throwable error) throws Exception {
//        session.getRemote().sendString(error.getMessage());
//    }

//    @OnWebSocketClose
//    public void onClose(org.eclipse.jetty.websocket.api.Session session, int statusCode, String reason) throws Exception {
//        session.getRemote().sendString("Websocket connection closed. Goodbye.\n");
//    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}

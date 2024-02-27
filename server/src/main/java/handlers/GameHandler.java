package handlers;

import com.google.gson.Gson;
import responseRecords.CreateGameResponse;
import spark.*;
import dataAccess.DataAccessException;
import responseRecords.ListGamesResponse;
import responseRecords.ServerResponse;
import service.GameService;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;

public class GameHandler extends ServiceHandler {
    private GameService service = new GameService();

    public GameHandler(Request request, Response response) { super(request, response); }

    public Object listGames() {
        String body;
        String authToken = this.request.headers().toString();
        try {
            ListGamesResponse lgResponse = this.service.listGames(authToken);
            body = new Gson().toJson(lgResponse);
            this.response.status(200);
        }
        catch (DataAccessException ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }

    public Object createGame() {
        String body;
        String authToken = this.request.headers().toString();
        CreateGameRequest cgRequest = getBody(this.request, CreateGameRequest.class);
        try {
            CreateGameResponse cgResponse = this.service.createGame(cgRequest, authToken);
            body = new Gson().toJson(cgResponse);
            this.response.status(200);
        }
        catch (DataAccessException ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }

    public Object joinGame() {
        String body;
        String authToken = this.request.headers().toString();
        JoinGameRequest jgRequest = getBody(this.request, JoinGameRequest.class);
        try {
            this.service.joinGame(jgRequest, authToken);
            body = new Gson().toJson(new ServerResponse(""));
            this.response.status(200);
        }
        catch (DataAccessException ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(401);
        }
        catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }
}

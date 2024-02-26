package server;

import com.google.gson.Gson;
import spark.*;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;

public class GameHandler extends ServiceHandler {
    public GameHandler(Request request, Response response) { super(request, response); }

    public Object listGames() {
        return new Gson().toJson(7);
    }

    public Object createGame() {
        CreateGameRequest cgRequest = getBody(this.request, CreateGameRequest.class);
        return new Gson().toJson(7);
    }

    public Object joinGame() {
        JoinGameRequest jgRequest = getBody(this.request, JoinGameRequest.class);
        return new Gson().toJson(7);
    }
}

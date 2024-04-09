package serverCommunication;

import com.google.gson.Gson;
import javax.websocket.*;

import java.net.HttpURLConnection;

import requestRecords.*;
import responseRecords.*;

public class ServerFacade {
    private final HttpCommunicator httpCommunicator;
    private final WebsocketCommunicator websocketCommunicator;

    public ServerFacade(int serverPort, String urlStem) {
        this.httpCommunicator = new HttpCommunicator(serverPort, urlStem);
        this.websocketCommunicator = new WebsocketCommunicator(serverPort, urlStem);
    }

    public RegisterResponse register(RegisterRequest request) throws CommunicationException {
        RegisterResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/user", "POST", body);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            else { response = HttpCommunicator.getHTTPResponseBody(connection, RegisterResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public LoginResponse login(LoginRequest request) throws CommunicationException {
        LoginResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/session", "POST", body);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            else { response = HttpCommunicator.getHTTPResponseBody(connection, LoginResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void logout(String authToken) throws CommunicationException {
        try {
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/session", "DELETE", "", authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public ListGamesResponse listGames(String authToken) throws CommunicationException {
        ListGamesResponse response;
        try {
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/game", "GET", "", authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            response = HttpCommunicator.getHTTPResponseBody(connection, ListGamesResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws CommunicationException {
        CreateGameResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/game", "POST", body, authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            else { response = HttpCommunicator.getHTTPResponseBody(connection, CreateGameResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void joinGame(JoinGameRequest request, String authToken) throws CommunicationException {
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/game", "PUT", body, authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
//            return this.websocketCommunicator.getNewSession();
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public String redrawBoard() { return ""; }

    public void leaveGame() {}

    public void makeMove() {}

    public void resign() {}

    public String highlightLegalMoves() { return ""; }

    public void clear() throws CommunicationException {
        try {
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/db", "DELETE", "");
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }
}

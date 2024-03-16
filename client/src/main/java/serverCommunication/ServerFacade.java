package serverCommunication;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import requestRecords.*;
import responseRecords.*;

public class ServerFacade {
    private final int serverPort;
    private final String urlStemLocal;

    public ServerFacade(int serverPort) {
        this.serverPort = serverPort;
        urlStemLocal = "http://localhost:";
    }

    public RegisterResponse register(RegisterRequest request) throws CommunicationException {
        RegisterResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/user", "POST", body);
            response = getHTTPResponse(connection, RegisterResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public LoginResponse login(LoginRequest request) throws CommunicationException {
        LoginResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/session", "POST", body);
            response = getHTTPResponse(connection, LoginResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void logout(String authToken) throws CommunicationException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/session", "DELETE", "", authToken);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public ListGamesResponse listGames(String authToken) throws CommunicationException {
        ListGamesResponse response = null;
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "GET", "", authToken);
            response = getHTTPResponse(connection, ListGamesResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws CommunicationException {
        CreateGameResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "POST", body, authToken);
            response = getHTTPResponse(connection, CreateGameResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void joinGame(JoinGameRequest request, String authToken) throws CommunicationException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "PUT", "", authToken);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public void clear() throws CommunicationException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/db", "DELETE", "");
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    private HttpURLConnection makeHTTPRequest(int port, String urlStem, String path, String method, String body, String authHeader) throws URISyntaxException, IOException {
        URI uri = new URI(urlStem + port + path);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        if (!(authHeader == null)) { http.setRequestProperty("authorization", authHeader); }
        writeHTTPRequestBody(http, body);
        http.connect();
        return http;
    }

    private HttpURLConnection makeHTTPRequest(int port, String urlStem, String path, String method, String body) throws URISyntaxException, IOException {
        return makeHTTPRequest(port, urlStem, path, method, body, null);
    }

    private static void writeHTTPRequestBody(HttpURLConnection http, String body) throws IOException {
        if (!body.isEmpty()) {
            http.setDoInput(true);
            try (OutputStream outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static <T> T getHTTPResponse(HttpURLConnection http, Class<T> clazz) throws IOException {
        int statusCode = http.getResponseCode();
        String statusMessage = http.getResponseMessage();
        return getHTTPResponseBody(http, clazz);
    }

    private static <T> T getHTTPResponseBody(HttpURLConnection http, Class<T> clazz) throws IOException {
        T responseBody = null;
        try (InputStream inputStream = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            responseBody = new Gson().fromJson(inputStreamReader, clazz);
        }
        return responseBody;
    }
}

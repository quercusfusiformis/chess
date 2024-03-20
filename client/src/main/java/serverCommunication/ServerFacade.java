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
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, RegisterResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public LoginResponse login(LoginRequest request) throws CommunicationException {
        LoginResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/session", "POST", body);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, LoginResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void logout(String authToken) throws CommunicationException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/session", "DELETE", "", authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public ListGamesResponse listGames(String authToken) throws CommunicationException {
        ListGamesResponse response;
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "GET", "", authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            response = getHTTPResponseBody(connection, ListGamesResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws CommunicationException {
        CreateGameResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "POST", body, authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, CreateGameResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void joinGame(JoinGameRequest request, String authToken) throws CommunicationException {
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/game", "PUT", body, authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public GetGameResponse getGame(GetGameRequest request, String authToken) throws CommunicationException {
        GetGameResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/", "GET", body, authToken);
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
            else { response = getHTTPResponseBody(connection, GetGameResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void clear() throws CommunicationException {
        try {
            HttpURLConnection connection = makeHTTPRequest(serverPort, urlStemLocal, "/db", "DELETE", "");
            if (!(hasGoodResponseCode(connection))) { throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    private HttpURLConnection makeHTTPRequest(int port, String urlStem, String path, String method, String body, String authHeader) throws URISyntaxException, IOException {
        String preURI = weldURLComponents(port, urlStem, path);
        URI uri = new URI(preURI);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        if (!(authHeader == null)) { http.setRequestProperty("authorization", authHeader); }
        writeHTTPRequestBody(http, body);
        http.connect();
        return http;
    }

    private String weldURLComponents(int port, String urlStem, String path) { return urlStem + port + path; }

    private HttpURLConnection makeHTTPRequest(int port, String urlStem, String path, String method, String body) throws URISyntaxException, IOException {
        return makeHTTPRequest(port, urlStem, path, method, body, null);
    }

    private static void writeHTTPRequestBody(HttpURLConnection http, String body) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (OutputStream outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static boolean hasGoodResponseCode(HttpURLConnection http) throws IOException {
        return http.getResponseCode() == 200;
    }

    private static void throwResponseError(HttpURLConnection http) throws IOException, CommunicationException {
        throw new CommunicationException("Server returned: " + http.getResponseCode() + " " + http.getResponseMessage());
    }

    private static <T> T getHTTPResponseBody(HttpURLConnection http, Class<T> clazz) throws IOException {
        T responseBody;
        try (InputStream inputStream = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            responseBody = new Gson().fromJson(inputStreamReader, clazz);
        }
        return responseBody;
    }
}

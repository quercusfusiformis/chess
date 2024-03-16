package serverCommunication;

import com.google.gson.Gson;
import spark.Request;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;
import requestRecords.*;
import responseRecords.*;

public class ServerFacade {
    private final int commPort;
    private final String urlStemLocal;

    public ServerFacade(int commPort) {
        this.commPort = commPort;
        urlStemLocal = "http://localhost:" + commPort + "/";
    }

    public RegisterResponse register(RegisterRequest request) {
        return new RegisterResponse(null, null);
    }

    public LoginResponse login(LoginRequest request) {
        return new LoginResponse(null, null);
    }

    public void logout(String authToken) {}

    public ListGamesResponse listGames(String authToken) {
        return new ListGamesResponse(null);
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) {
        return new CreateGameResponse(0);
    }

    public void joinGame(JoinGameRequest request, String authToken) {}

    public void clear() {}

    protected static <T> T getBody(Request request, Class<T> clazz) {
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    private HttpURLConnection makeHTTPRequest(int port, String url, String method, String body) throws URISyntaxException, IOException {
        URI uri = new URI(url);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        writeHTTPRequestBody(http, body);
        http.connect();
        return http;
    }

    private static void writeHTTPRequestBody(HttpURLConnection http, String body) throws IOException {
        if (!body.isEmpty()) {
            http.setDoInput(true);
            try (OutputStream outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static String getHTTPResponse(HttpURLConnection http) throws IOException {
        int statusCode = http.getResponseCode();
        String statusMessage = http.getResponseMessage();
        String body = "";
        try (InputStream inputStream = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            body = inputStreamReader.toString();
        }
        return body;
    }

    private static <T> T getHTTPResponse(HttpURLConnection http, Class<T> clazz) throws IOException {
        int statusCode = http.getResponseCode();
        String statusMessage = http.getResponseMessage();
        return getHTTPResponseBody(http, clazz);
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

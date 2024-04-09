package serverCommunication;

import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpCommunicator {
    private final int serverPort;
    private final String urlStem;

    public HttpCommunicator(int serverPort, String urlStem) {
        this.serverPort = serverPort;
        this.urlStem = "http://" + urlStem;
    }

    public HttpURLConnection makeHTTPRequest(String path, String method, String body, String authHeader) throws URISyntaxException, IOException {
        String preURI = getRequestedURL(path);
        URI uri = new URI(preURI);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        if (!(authHeader == null)) { http.setRequestProperty("authorization", authHeader); }
        writeHTTPRequestBody(http, body);
        http.connect();
        return http;
    }

    public HttpURLConnection makeHTTPRequest(String path, String method, String body) throws URISyntaxException, IOException {
        return makeHTTPRequest(path, method, body, null);
    }

    private String getRequestedURL(String path) { return this.urlStem + this.serverPort + path; }

    private static void writeHTTPRequestBody(HttpURLConnection http, String body) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (OutputStream outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    public static boolean hasGoodResponseCode(HttpURLConnection http) throws IOException {
        return http.getResponseCode() == 200;
    }

    public static void throwResponseError(HttpURLConnection http) throws IOException, CommunicationException {
        throw new CommunicationException("Server returned: " + http.getResponseCode() + " " + http.getResponseMessage());
    }

    public static <T> T getHTTPResponseBody(HttpURLConnection http, Class<T> clazz) throws IOException {
        T responseBody;
        try (InputStream inputStream = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            responseBody = new Gson().fromJson(inputStreamReader, clazz);
        }
        return responseBody;
    }
}

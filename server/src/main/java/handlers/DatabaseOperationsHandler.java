package handlers;

import com.google.gson.Gson;
import spark.*;
import service.DatabaseOperationsService;
import responseRecords.ServerResponse;

public class DatabaseOperationsHandler extends ServiceHandler {
    private final DatabaseOperationsService service = new DatabaseOperationsService();

    public DatabaseOperationsHandler(Request request, Response response) { super(request, response); }

    public Object clear() {
        String body;
        try {
            this.service.clear();
            body = new Gson().toJson(new ServerResponse(""));
            this.response.status(200);
        } catch (Exception ex) {
            body = new Gson().toJson(new ServerResponse(ex.getMessage()));
            this.response.status(500);
        }
        this.response.type("application/json");
        return body;
    }
}

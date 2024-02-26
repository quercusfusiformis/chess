package server;

import com.google.gson.Gson;
import spark.*;

public class DatabaseOperationsHandler extends ServiceHandler {
    public DatabaseOperationsHandler(Request request, Response response) { super(request, response); }

    public Object clear() {
        this.response.type("application/json");
        this.response.status(200);
        return new Gson().toJson(7);
    }
}

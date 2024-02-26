package server;

import com.google.gson.Gson;
import spark.*;
import requestRecords.RegisterRequest;
import requestRecords.LoginRequest;

public class AuthorizationHandler extends  ServiceHandler {
    public AuthorizationHandler(Request request, Response response) { super(request, response); }

    public Object register() {
        RegisterRequest rRequest = getBody(this.request, RegisterRequest.class);
        return new Gson().toJson(7);
    }

    public Object login() {
        LoginRequest lRequest = getBody(this.request, LoginRequest.class);
        return new Gson().toJson(7);
    }

    public Object logout() {
        return new Gson().toJson(7);
    }
}

package service;

import dataAccess.*;
import requestRecords.LoginRequest;
import requestRecords.RegisterRequest;
import responseRecords.LoginResponse;
import responseRecords.RegisterResponse;

public class AuthorizationService {
    private AuthDAO authDAO = new MemoryAuthDAO();

    private UserDAO userDAO = new MemoryUserDAO();

    public RegisterResponse register(RegisterRequest rRequest) throws DataAccessException {
        if (userDAO.userExists(rRequest.username())) {
            throw new DataAccessException("Error: already taken");
        }
        else {
            userDAO.createUser(rRequest.username(), rRequest.password(), rRequest.email());
            String newAuthToken = authDAO.createAuth(rRequest.username());
            return new RegisterResponse(rRequest.username(), newAuthToken);
        }
    }

    public LoginResponse login(LoginRequest lRequest) throws DataAccessException {
        if (userDAO.userExists(lRequest.username())) {
            if (userDAO.userPasswordMatches(lRequest.username(), lRequest.password())) {
                String newAuthToken = authDAO.createAuth(lRequest.username());
                return new LoginResponse(lRequest.username(), newAuthToken);
            }
            else {
                throw new DataAccessException("Error: unauthorized");
            }
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            authDAO.deleteAuth(authToken);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}

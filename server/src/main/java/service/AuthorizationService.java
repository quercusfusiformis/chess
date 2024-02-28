package service;

import requestRecords.RegisterRequest;
import requestRecords.LoginRequest;
import responseRecords.RegisterResponse;
import responseRecords.LoginResponse;
import dataAccess.*;

public class AuthorizationService {
    private final AuthDAO authDAO = new MemoryAuthDAO();

    private final UserDAO userDAO = new MemoryUserDAO();

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

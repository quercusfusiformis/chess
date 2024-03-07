package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear() throws DataAccessException;

    String createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken);

    String getUsername(String authToken) throws IllegalArgumentException;

    boolean authExists(String authToken);

    void deleteAuth(String authToken);
}

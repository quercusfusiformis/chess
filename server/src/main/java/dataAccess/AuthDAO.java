package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();

    String createAuth(String username);

    AuthData getAuth(String authToken);

    boolean authExists(String authToken);

    void deleteAuth(String authToken);
}

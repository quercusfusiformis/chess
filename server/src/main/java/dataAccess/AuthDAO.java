package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();

    String createAuth(String username);

    AuthData authenticate(String authToken);

    void deleteAuth(String authToken);
}

package dataAccess;

import model.AuthData;

public interface AuthDAO {
    boolean clear();

    int createAuth(String username);

    AuthData authenticate(int authToken);

    boolean deleteAuth(int authToken);
}

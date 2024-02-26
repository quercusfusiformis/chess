package dataAccess;

import java.util.UUID;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    private DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void clear() {
        dbManager.clearAuths();
    }

    @Override
    public String createAuth(String username) {
        String newAuthToken = UUID.randomUUID().toString();
        dbManager.addAuth(newAuthToken, username);
        return newAuthToken;
    }

    @Override
    public AuthData authenticate(String authToken) {
        return dbManager.getAuth("authToken", new AuthData(authToken, null));
    }

    @Override
    public void deleteAuth(String authToken) {
        dbManager.delAuth("authToken", new AuthData(authToken, null));
    }
}

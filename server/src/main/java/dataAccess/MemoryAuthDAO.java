package dataAccess;

import java.util.UUID;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    private final DatabaseManager dbManager = new DatabaseManager();

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
    public AuthData getAuth(String authToken) {
        return dbManager.getAuth("authToken", new AuthData(authToken, null));
    }

    @Override
    public String getUsername(String authToken) throws IllegalArgumentException {
        try {
            AuthData auth = getAuth(authToken);
            return auth.username();
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Error: unauthorized");
        }
    }

    @Override
    public boolean authExists(String authToken) {
        return getAuth(authToken) != null;
    }

    @Override
    public void deleteAuth(String authToken) {
        dbManager.delAuth("authToken", new AuthData(authToken, null));
    }
}

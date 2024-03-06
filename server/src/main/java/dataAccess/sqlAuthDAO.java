package dataAccess;

import model.AuthData;

public class sqlAuthDAO implements AuthDAO {
    @Override
    public void clear() {

    }

    @Override
    public String createAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public String getUsername(String authToken) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean authExists(String authToken) {
        return false;
    }

    @Override
    public void deleteAuth(String authToken) {

    }
}

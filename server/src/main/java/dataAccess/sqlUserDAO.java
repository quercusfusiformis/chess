package dataAccess;

import model.UserData;

public class sqlUserDAO implements UserDAO {
    @Override
    public void clear() {

    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

    @Override
    public boolean userPasswordMatches(String testUsername, String password) {
        return false;
    }
}

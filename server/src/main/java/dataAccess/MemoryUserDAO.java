package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private final DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void clear() {
        dbManager.clearUsers();
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        if (!(username == null || password == null || email == null)) {
            dbManager.addUser(username, password, email);
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public UserData getUser(String username) {
        return dbManager.getUser("username", new UserData(username, null, null));
    }

    @Override
    public boolean userExists(String username) {
        return getUser(username) != null;
    }

    @Override
    public boolean userPasswordMatches(String testUsername, String testPassword) {
        UserData knownUser = getUser(testUsername);
        if (knownUser == null) {
            return false;
        }
        return testPassword.equals(knownUser.password());
    }
}

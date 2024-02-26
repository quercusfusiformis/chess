package dataAccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO {
    private DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void clear() {
        dbManager.clearUsers();
    }

    @Override
    public void createUser(String username, String password, String email) {
        dbManager.addUser(username, password, email);
    }

    @Override
    public UserData getUser(String username) {
        return dbManager.getUser("username", new UserData(username, null, null));
    }
}

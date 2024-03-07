package dataAccess;

import model.UserData;

public interface UserDAO {
    void clear() throws DataAccessException;

    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    boolean userExists(String username) throws DataAccessException;

    boolean userPasswordMatches(String testUsername, String password) throws DataAccessException;
}

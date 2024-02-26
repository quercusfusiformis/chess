package dataAccess;

import model.UserData;

import java.util.Map;

public interface UserDAO {
    boolean clear();

    boolean createUser(UserData user);

    UserData getUser(String username);
}

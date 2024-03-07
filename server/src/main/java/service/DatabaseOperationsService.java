package service;

import dataAccess.*;

public class DatabaseOperationsService {
    private final AuthDAO authDAO = new SQLAuthDAO();

    private final GameDAO gameDAO = new SQLGameDAO();

    private final UserDAO userDAO = new SQLUserDAO();

    public void clear() throws DataAccessException {
        this.authDAO.clear();
        this.gameDAO.clear();
        this.userDAO.clear();
    }
}

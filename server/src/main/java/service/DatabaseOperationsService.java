package service;

import dataAccess.*;

public class DatabaseOperationsService {
    private final AuthDAO authDAO = new MemoryAuthDAO();

    private final GameDAO gameDAO = new MemoryGameDAO();

    private final UserDAO userDAO = new MemoryUserDAO();

    public void clear() throws DataAccessException {
        this.authDAO.clear();
        this.gameDAO.clear();
        this.userDAO.clear();
    }
}

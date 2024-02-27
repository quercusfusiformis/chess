package service;

import dataAccess.*;

public class DatabaseOperationsService {
    private AuthDAO authDAO = new MemoryAuthDAO();

    private GameDAO gameDAO = new MemoryGameDAO();

    private UserDAO userDAO = new MemoryUserDAO();

    public void clear() {
        this.authDAO.clear();
        this.gameDAO.clear();
        this.userDAO.clear();
    }
}

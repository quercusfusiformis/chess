package dataAccess;

import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public boolean gameExists(int gameID) {
        return false;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void updatePlayerInGame(int gameID, String username, String color) throws IllegalArgumentException, DataAccessException {

    }

    @Override
    public boolean colorFreeInGame(String color, int gameID) throws DataAccessException {
        return false;
    }

    @Override
    public void joinGameAsPlayer(int gameID, String username, String color) throws DataAccessException {

    }
}

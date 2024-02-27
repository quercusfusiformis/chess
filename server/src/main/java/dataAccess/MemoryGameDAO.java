package dataAccess;

import java.util.Collection;
import model.GameData;

public class MemoryGameDAO implements GameDAO {
    private DatabaseManager dbManager = new DatabaseManager();

    @Override
    public void clear() {
        dbManager.clearGames();
    }

    @Override
    public int createGame(String gameName) {
        int newGameID = dbManager.getNewGameID();
        dbManager.addGame(newGameID, null, null, gameName, null);
        return newGameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return dbManager.getGame("gameID", new GameData(gameID, null, null, null, null));
    }

    @Override
    public boolean gameExists(int gameID) {
        return getGame(gameID) != null;
    }

    @Override
    public Collection<GameData> listGames() {
        return dbManager.getAllGames();
    }

    @Override
    public void updateGame(int gameID, String game) throws DataAccessException {
        GameData toUpdate = dbManager.getGame("gameID", new GameData(gameID, null, null, null, null));
        if (toUpdate == null) { throw new DataAccessException("bad request"); }
        else {
            dbManager.delGame("all", toUpdate);
            dbManager.addGame(toUpdate.gameID(), toUpdate.whiteUsername(), toUpdate.blackUsername(), toUpdate.gameName(), game);
        }
    }

    @Override
    public void updatePlayerInGame(int gameID, String username, String color) throws IllegalArgumentException, DataAccessException {
        GameData toUpdate = dbManager.getGame("gameID", new GameData(gameID, null, null, null, null));
        if (toUpdate == null) { throw new DataAccessException("bad request"); }
        else {
            dbManager.delGame("all", toUpdate);
            if (color.equals("WHITE")) {
                dbManager.addGame(toUpdate.gameID(), username, toUpdate.blackUsername(), toUpdate.gameName(), toUpdate.game());
            } else if (color.equals("BLACK")) {
                dbManager.addGame(toUpdate.gameID(), toUpdate.whiteUsername(), username, toUpdate.gameName(), toUpdate.game());
            } else {
                throw new IllegalArgumentException("invalid color parameter");
            }
        }
    }

    @Override
    public boolean colorFreeInGame(String color, int gameID) throws DataAccessException {
        if (gameExists(gameID)) {
            GameData gameToCheck = getGame(gameID);
            return gameToCheck.isColorAvailable(color);
        }
        else {
            throw new DataAccessException("bad request");
        }
    }

    @Override
    public void joinGameAsPlayer(int gameID, String username, String color) throws DataAccessException {
        boolean colorFree;
        try {
            colorFree = colorFreeInGame(color, gameID);
        }
        catch (IllegalArgumentException ex) {
            throw new DataAccessException("bad request");
        }
        if (colorFree) {
            updatePlayerInGame(gameID, username, color);
        }
        else {
            throw new DataAccessException("already taken");
        }
    }
}

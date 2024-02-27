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
    public void updateGame(int gameID, String game) {
        GameData toUpdate = dbManager.getGame("gameID", new GameData(gameID, null, null, null, null));
        dbManager.delGame("all", toUpdate);
        dbManager.addGame(toUpdate.gameID(), toUpdate.whiteUsername(), toUpdate.blackUsername(), toUpdate.gameName(), game);
    }

    @Override
    public boolean colorFreeInGame(String color, int gameID) {
        GameData gameToCheck = getGame(gameID);
        return gameToCheck.isColorAvailable(color);
    }
}

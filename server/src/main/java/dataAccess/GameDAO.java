package dataAccess;

import java.util.Collection;
import model.GameData;

public interface GameDAO {
    void clear();

    int createGame(String gameName);

    GameData getGame(int gameID);

    boolean gameExists(int gameID);

    Collection<GameData> listGames();

    void updateGame(int gameID, String game) throws DataAccessException;

    void updatePlayerInGame(int gameID, String username, String color) throws IllegalArgumentException, DataAccessException;

    boolean colorFreeInGame(String color, int gameID) throws DataAccessException;

    void joinGameAsPlayer(int gameID, String username, String color) throws DataAccessException;
}

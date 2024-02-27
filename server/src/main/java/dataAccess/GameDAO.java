package dataAccess;

import java.util.Collection;
import model.GameData;

public interface GameDAO {
    void clear();

    int createGame(String gameName);

    GameData getGame(int gameID);

    boolean gameExists(int gameID);

    Collection<GameData> listGames();

    void updateGame(int gameID, String game);

    boolean colorFreeInGame(String color, int gameID);
}

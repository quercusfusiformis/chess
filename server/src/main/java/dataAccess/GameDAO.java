package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    boolean clear();

    int createGame(String game);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    boolean updateGame(int gameID, String game);
}

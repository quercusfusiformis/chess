package dataAccess;

import java.util.Collection;

import chess.ChessGame;
import model.GameData;

public interface GameDAO {
    void clear() throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    boolean gameExists(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updatePlayerInGame(int gameID, String username, ChessGame.TeamColor color) throws IllegalArgumentException, DataAccessException;

    boolean colorFreeInGame(ChessGame.TeamColor color, int gameID) throws DataAccessException;

    void joinGameAsPlayer(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException;
}

package dataAccess;

import java.util.Collection;
import java.util.HashSet;
import com.google.gson.Gson;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import chess.ChessGame;
import model.GameData;

public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement("TRUNCATE TABLE game")) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        int newGameID = -1;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "INSERT INTO game (gameName, game) VALUES(?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            String game = new Gson().toJson(new ChessGame());
            statement.setString(1, gameName);
            statement.setString(2, game);
            statement.executeUpdate();
            var key = statement.getGeneratedKeys();
            if (key.next()) { newGameID = key.getInt(1); }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return newGameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData returnGame = null;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, gameID);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 1) {
                returnGame = new GameData(result.getInt(1),
                                        result.getString(2),
                                        result.getString(3),
                                        result.getString(4),
                                        result.getString(5));
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching gameID in database");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return returnGame;
    }

    @Override
    public boolean gameExists(int gameID) throws DataAccessException {
        boolean goodGame = false;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT gameID FROM game WHERE gameID=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, gameID);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 1) {
                goodGame = true;
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching gameID in database");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return goodGame;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> games = new HashSet<>();
        try (var statement = DatabaseManager.getConnection().prepareStatement("SELECT * FROM game",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            var result = statement.executeQuery();
            while (result.next()) {
                games.add(new GameData(result.getInt(1),
                                    result.getString(2),
                                    result.getString(3),
                                    result.getString(4),
                                    result.getString(5)));
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return games;
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

package dataAccess;

import com.google.gson.Gson;
import java.util.Collection;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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
            ChessGame newGame = new ChessGame();
            newGame.initialize();
            String game = new Gson().toJson(newGame);
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

    public void addGame(int gameID, String whiteUsername, String blackUsername, String gameName, String game) throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "INSERT INTO game (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, gameID);
            statement.setString(2, whiteUsername);
            statement.setString(3, blackUsername);
            statement.setString(4, gameName);
            statement.setString(5, game);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private void updateGame(int gameID, String whiteUsername, String blackUsername, String gameName, String game) throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, whiteUsername);
            statement.setString(2, blackUsername);
            statement.setString(3, gameName);
            statement.setString(4, game);
            statement.setInt(5, gameID);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void delGame(int gameID) throws DataAccessException {
        if (!(gameExists(gameID))) { throw new DataAccessException("Error: no game with given gameID"); }
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "DELETE FROM game WHERE gameID=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setInt(1, gameID);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
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
            if (rowcount == 0) { throw new DataAccessException("Error: no game with given gameID"); }
            else if (rowcount == 1) {
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
        Collection<GameData> games = new ArrayList<>();
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
    public void updatePlayerInGame(int gameID, String username, ChessGame.TeamColor color) throws IllegalArgumentException, DataAccessException {
        GameData toUpdate = getGame(gameID);
        if (toUpdate == null) { throw new DataAccessException("Error: bad request"); }
        else {
            if (color.equals(ChessGame.TeamColor.WHITE)) {
                updateGame(toUpdate.gameID(), username, toUpdate.blackUsername(), toUpdate.gameName(), toUpdate.game());
            } else if (color.equals(ChessGame.TeamColor.BLACK)) {
                updateGame(toUpdate.gameID(), toUpdate.whiteUsername(), username, toUpdate.gameName(), toUpdate.game());
            } else {
                throw new IllegalArgumentException("Error: invalid color parameter");
            }
        }
    }

    @Override
    public boolean colorFreeInGame(ChessGame.TeamColor color, int gameID) throws DataAccessException {
        if (gameExists(gameID)) {
            GameData gameToCheck = getGame(gameID);
            return gameToCheck.isColorAvailable(color);
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void joinGameAsPlayer(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        boolean colorFree;
        try {
            colorFree = colorFreeInGame(color, gameID);
        }
        catch (IllegalArgumentException ex) {
            throw new DataAccessException("Error: bad request");
        }
        if (colorFree) {
            updatePlayerInGame(gameID, username, color);
        }
        else {
            throw new DataAccessException("Error: already taken");
        }
    }

    public boolean isPlayerColor(int gameID, String username, ChessGame.TeamColor color) throws DataAccessException {
        if (gameExists(gameID)) {
            GameData gameToCheck = getGame(gameID);
            if (color == null) { throw new DataAccessException("Error: bad request"); }
            if (color.equals(ChessGame.TeamColor.WHITE)) {
                String whiteUsername = gameToCheck.whiteUsername();
                if (whiteUsername != null) {
                    return whiteUsername.equals(username);
                } else { return false; }
            } else if (color.equals(ChessGame.TeamColor.BLACK)) {
                String blackUsername = gameToCheck.blackUsername();
                if (blackUsername != null) {
                    return blackUsername.equals(username);
                } else { return false; }
            } else { throw new DataAccessException("Error: bad request"); }
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }

    public ChessGame.TeamColor getPlayerColor(int gameID, String username) throws DataAccessException {
        if (isPlayerColor(gameID, username, ChessGame.TeamColor.WHITE)) {
            return ChessGame.TeamColor.WHITE;
        } else if (isPlayerColor(gameID, username, ChessGame.TeamColor.BLACK)) {
            return ChessGame.TeamColor.BLACK;
        } else { return null; }
    }

    public void makeMove(int gameID, ChessMove move) throws DataAccessException {
        GameData gameData = getGame(gameID);
        if (gameData == null) { throw new DataAccessException("Error: bad request");
        } else {
            ChessGame game = new Gson().fromJson(gameData.game(), ChessGame.class);
            try {
                game.makeMove(move);
            } catch (InvalidMoveException ex) {
                throw new DataAccessException(ex.getClass().getName() + ": " + ex.getMessage());
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE) || game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                game.setTeamTurn(null);
            }
            String serializedGame = new Gson().toJson(game);
            updateGame(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), serializedGame);
        }
    }

    public ChessGame.TeamColor getCurrTeamTurn(int gameID) throws DataAccessException {
        GameData gameData = getGame(gameID);
        if (gameData == null) { throw new DataAccessException("Error: bad request");
        } else {
            ChessGame game = new Gson().fromJson(gameData.game(), ChessGame.class);
            return game.getTeamTurn();
        }
    }

    public void setTeamTurnNull(int gameID) throws DataAccessException {
        GameData gameData = getGame(gameID);
        if (gameData == null) { throw new DataAccessException("Error: bad request");
        } else {
            ChessGame game = new Gson().fromJson(gameData.game(), ChessGame.class);
            game.setTeamTurn(null);
            String serializedGame = new Gson().toJson(game);
            updateGame(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), serializedGame);
        }
    }
}

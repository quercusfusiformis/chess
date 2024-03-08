package dataAccess;

import java.util.UUID;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement("TRUNCATE TABLE auth")) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String newAuthToken = UUID.randomUUID().toString();
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "INSERT INTO auth (authToken, username) VALUES(?,?)")) {
            statement.setString(1, newAuthToken);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return newAuthToken;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData returnAuth = null;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT authToken, username FROM auth WHERE authToken=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, authToken);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 0 ) { throw new DataAccessException("Error: unauthorized"); }
            else if (rowcount == 1) {
                returnAuth = new AuthData(result.getString(1), result.getString(2));
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching authToken in database");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return returnAuth;
    }

    @Override
    public String getUsername(String authToken) throws IllegalArgumentException {
        String username = null;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username FROM auth WHERE authToken=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, authToken);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 0) { throw new DataAccessException("Error: unauthorized"); }
            else if (rowcount == 1) {
                username = result.getString(1);
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching authToken in database");
            }
//            if (result.next()) { username = result.getString(1); }
        } catch (SQLException | DataAccessException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return username;
    }

    @Override
    public boolean authExists(String authToken) throws DataAccessException {
        boolean goodAuth = false;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username FROM auth WHERE authToken=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, authToken);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 1) {
                goodAuth = true;
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching authToken in database");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return goodAuth;
    }

        @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (!(authExists(authToken))) { throw new DataAccessException("Error: unauthorized"); }
        else {
            try (var statement = DatabaseManager.getConnection().prepareStatement(
                    "DELETE FROM auth WHERE authToken=?")) {
                statement.setString(1, authToken);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        }
    }
}

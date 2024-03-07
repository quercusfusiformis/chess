package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.util.UUID;

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
        String insertStatement = "INSERT INTO auth (authToken, username) VALUES ('" + newAuthToken + "', '" + username + "')";
        try (var statement = DatabaseManager.getConnection().prepareStatement(insertStatement)) {
            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return newAuthToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        AuthData returnAuth = new AuthData("", "");
        String selectStatement = "SELECT authToken, username FROM auth WHERE authToken='" + authToken + "'";
        try (var statement = DatabaseManager.getConnection().prepareStatement(selectStatement)) {
            statement.executeUpdate();
            var result = statement.getGeneratedKeys();
            if (result.next()) {
                returnAuth = new AuthData(result.getString(1), result.getString(2));
            }
        } catch (SQLException | DataAccessException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return returnAuth;
    }

    @Override
    public String getUsername(String authToken) throws IllegalArgumentException {
        return null;
    }

    @Override
    public boolean authExists(String authToken) {
        return false;
    }

    @Override
    public void deleteAuth(String authToken) {

    }
}

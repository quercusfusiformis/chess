package dataAccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;
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
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "INSERT INTO auth (authToken, username) VALUES(?,?)")) {
            statement.setString(1, newAuthToken);
            statement.setString(2, username);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return newAuthToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        AuthData returnAuth = new AuthData("", "");
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT authToken, username FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, authToken);
            statement.executeQuery();
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
        String username = "";
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, authToken);
            statement.executeQuery();
            var result = statement.getGeneratedKeys();
            if (result.next()) { username = result.getString(1); }
        } catch (SQLException | DataAccessException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
        return username;
    }

    @Override
    public boolean authExists(String authToken) throws DataAccessException {
        boolean goodAuth = false;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username FROM auth WHERE authToken=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, authToken);
            statement.executeQuery();
            String result = String.valueOf(statement.getGeneratedKeys());
            if (!(result.isEmpty())) { goodAuth = true; }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return goodAuth;
    }

        @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "DELETE FROM auth WHERE authToken=?")) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

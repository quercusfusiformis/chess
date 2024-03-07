package dataAccess;

import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement("TRUNCATE TABLE user")) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        if (!(username == null || password == null || email == null)) {
            try (var statement = DatabaseManager.getConnection().prepareStatement(
                    "INSERT INTO user (username, password, email) VALUES(?,?, ?)")) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, email);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new DataAccessException(ex.getMessage());
            }
        }
        else {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData returnUser = new UserData("", "", "");
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username, password, email FROM user WHERE username=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.executeQuery();
            var result = statement.getGeneratedKeys();
            if (result.next()) {
                returnUser = new UserData(result.getString(1), result.getString(2), result.getString(3));
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return returnUser;
    }

    @Override
    public boolean userExists(String username) throws DataAccessException {
        boolean goodUser = false;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username FROM user WHERE username=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.executeQuery();
            String result = String.valueOf(statement.getGeneratedKeys());
            if (!(result.isEmpty())) { goodUser = true; }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return goodUser;
    }

    @Override
    public boolean userPasswordMatches(String testUsername, String password) throws DataAccessException {
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username FROM user WHERE username=?", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, testUsername);
            statement.executeQuery();
            String result = String.valueOf(statement.getGeneratedKeys());
            if (!(result.isEmpty())) { return false; }
            else { return result.equals(password); }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}

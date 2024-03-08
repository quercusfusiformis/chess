package dataAccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
                String hashedPassword = new BCryptPasswordEncoder().encode(password);
                statement.setString(2, hashedPassword);
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
        UserData returnUser = null;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT username, password, email FROM user WHERE username=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 0) { throw new DataAccessException("Error: user not registered"); }
            else if (rowcount == 1) {
                returnUser = new UserData(result.getString(1),
                        result.getString(2), result.getString(3));
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching username in database");
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
                "SELECT username FROM user WHERE username=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, username);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 1) {
                goodUser = true;
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching username in database");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return goodUser;
    }

    @Override
    public boolean userPasswordMatches(String testUsername, String password) throws DataAccessException {
        String expectedPassword = null;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                "SELECT password FROM user WHERE username=?",
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            statement.setString(1, testUsername);
            var result = statement.executeQuery();
            int rowcount = 0;
            if (result.last()) { rowcount = result.getRow(); }
            if (rowcount == 1) {
                expectedPassword = result.getString(1);
            }
            else if (rowcount > 1) {
                throw new DataAccessException("Error: More than one row matching username in database");
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        if (!(expectedPassword == null)) { return new BCryptPasswordEncoder().matches(password, expectedPassword); }
        else { return false; }
    }
}

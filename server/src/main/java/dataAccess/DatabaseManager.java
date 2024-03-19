package dataAccess;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    // Creates database and handles errors (especially if database is not running)
    static {
        try {
            createDatabase();
        } catch (DataAccessException ex) {
            String exceptMessage = ex.getMessage();
            String notRunningMessage = """
                    Communications link failure

                    The last packet sent successfully to the server was 0 milliseconds ago. The driver has not received any packets from the server.""";
            if ((exceptMessage.equals(notRunningMessage))) {
                throw new RuntimeException("Communications link error. Database may not be running. " +
                        "Check services.msc for MySQL80 and manually start the database if it is not running.");
            }
            throw new RuntimeException(ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            conn.setCatalog(databaseName);

            String createAuthTable = """
                    CREATE TABLE IF NOT EXISTS auth (
                        authToken VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (authToken)
                    )""";
            try (var createStatement = conn.prepareStatement(createAuthTable)) {
                createStatement.executeUpdate();
            }

            String createGameTable = """
                    CREATE TABLE IF NOT EXISTS game (
                        gameID INT NOT NULL AUTO_INCREMENT,
                        whiteUsername VARCHAR(255) DEFAULT NULL,
                        blackUsername VARCHAR(255) DEFAULT NULL,
                        gameName VARCHAR(255) NOT NULL,
                        game JSON NOT NULL,
                        PRIMARY KEY (gameID)
                    )""";
            try (var createStatement = conn.prepareStatement(createGameTable)) {
                createStatement.executeUpdate();
            }

            String createUserTable = """
                    CREATE TABLE IF NOT EXISTS user (
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        PRIMARY KEY (username)
                    )""";
            try (var createStatement = conn.prepareStatement(createUserTable)) {
                createStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static int getNumRows(String dbName) throws DataAccessException {
        String sqlStatement;
        switch (dbName) {
            case "auth" -> sqlStatement = "SELECT COUNT(*) FROM auth";
            case "game" -> sqlStatement = "SELECT COUNT(*) FROM game";
            case "user" -> sqlStatement = "SELECT COUNT(*) FROM user";
            default -> throw new DataAccessException("Invalid database requested.");
        }
        int numRows = 0;
        try (var statement = DatabaseManager.getConnection().prepareStatement(
                sqlStatement, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            var result = statement.executeQuery();
            if (result.next()) { numRows = result.getInt(1); }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return numRows;
    }
}

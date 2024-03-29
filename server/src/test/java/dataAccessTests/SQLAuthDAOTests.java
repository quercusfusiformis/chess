package dataAccessTests;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import dataAccess.DatabaseManager;
import dataAccess.SQLAuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import chess.ChessGame;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLAuthDAOTests {
    private final SQLAuthDAO authDAO = new SQLAuthDAO();

    private String rossAuthToken;

    @BeforeEach
    void setUp() throws DataAccessException {
        authDAO.clear();
        rossAuthToken = authDAO.createAuth("ross");
        authDAO.createAuth("richard");
        authDAO.createAuth("raymond");
        authDAO.createAuth("charlie");
        authDAO.createAuth("conner");
        authDAO.createAuth("peter");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        authDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clearPositive() throws DataAccessException {
        authDAO.clear();
        assertEquals(0, DatabaseManager.getNumRows("auth"));
    }

    @Test
    @Order(2)
    @DisplayName("createAuth (+)")
    void createAuthPositive() throws DataAccessException {
        authDAO.createAuth("echo8358");
        authDAO.createAuth("davidisamanwithalargesandwich");
        authDAO.createAuth("lorinwithano");
        assertEquals(9, DatabaseManager.getNumRows("auth"));
    }

    @Test
    @Order(3)
    @DisplayName("createAuth (-)")
    void createAuthNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.createAuth(new Gson().toJson(new ChessGame())));
        assertEquals("Data truncation: Data too long for column 'username' at row 1", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("getAuth (+)")
    void getAuthPositive() throws DataAccessException {
        AuthData testData = authDAO.getAuth(rossAuthToken);
        AuthData actualData = new AuthData(rossAuthToken, "ross");
        assertEquals(actualData, testData);
    }

    @Test
    @Order(5)
    @DisplayName("getAuth (-)")
    void getAuthNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.getAuth(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("getUsername (+)")
    void getUsernamePositive() {
        assertEquals("ross", authDAO.getUsername(rossAuthToken));
    }

    @Test
    @Order(7)
    @DisplayName("getUsername (-)")
    void getUsernameNegative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authDAO.getUsername(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(8)
    @DisplayName("authExists (+)")
    void authExistsPositive() throws DataAccessException {
        assertTrue(authDAO.authExists(rossAuthToken));
    }

    @Test
    @Order(9)
    @DisplayName("authExists (-)")
    void authExistsNegative() {
//        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO));
    }

    @Test
    @Order(10)
    @DisplayName("deleteAuth (+)")
    void deleteAuthPositive() throws DataAccessException {
        authDAO.deleteAuth(rossAuthToken);
        assertFalse(authDAO.authExists(rossAuthToken));
    }

    @Test
    @Order(11)
    @DisplayName("deleteAuth (-)")
    void deleteAuthNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> authDAO.deleteAuth(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}

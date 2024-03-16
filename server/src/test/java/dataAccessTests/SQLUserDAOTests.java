package dataAccessTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.SQLUserDAO;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLUserDAOTests {
    private final SQLUserDAO userDAO = new SQLUserDAO();

    @BeforeEach
    void setUp() throws DataAccessException {
        userDAO.clear();
        userDAO.createUser("ross", "hypergorilla", "pedropascalwithhair@haken.com");
        userDAO.createUser("richard", "biggestmoth", "bassproshop@haken.com");
        userDAO.createUser("raymond", "architext", "cutthecameras@haken.com");
        userDAO.createUser("charlie", "stoneface", "handsomegollum@haken.com");
        userDAO.createUser("conner", "golowmode", "???@haken.com");
        userDAO.createUser("peter", "harvardharvard", "lasvegasbonk@haken.com");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        userDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clearPositive() throws DataAccessException {
        userDAO.clear();
        assertEquals(0, DatabaseManager.getNumRows("user"));
    }

    @Test
    @Order(2)
    @DisplayName("createUser (+)")
    void createUserPositive() throws DataAccessException {
        userDAO.createUser("michael", "annieISOKay", "heehee@singer.com");
        userDAO.createUser("percy", "aBethisHOt", "mightypen@demigod.com");
        assertEquals(8, DatabaseManager.getNumRows("user"));
    }
    @Test
    @Order(3)
    @DisplayName("createUser (-)")
    void createUserNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> userDAO.createUser("parletscimpernel", null, "no"));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(4)
    @DisplayName("getUser (+)")
    void getUserPositive() throws DataAccessException {
        assertEquals("richard", userDAO.getUser("richard").username());
    }
    @Test
    @Order(5)
    @DisplayName("getUser (-)")
    void getUserNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> userDAO.getUser("diego"));
        assertEquals("Error: user not registered", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("userExists (+)")
    void userExistsPositive() throws DataAccessException {
        assertTrue(userDAO.userExists("peter"));
    }
    @Test
    @Order(7)
    @DisplayName("userExists (-)")
    void userExistsNegative() throws DataAccessException {
        assertFalse(userDAO.userExists("vikram"));
    }

    @Test
    @Order(8)
    @DisplayName("userPasswordMatches (+)")
    void userPasswordMatchesPositive() throws DataAccessException {
        String connerPassword = "golowmode";
        assertTrue(userDAO.userPasswordMatches("conner", connerPassword));
    }
    @Test
    @Order(9)
    @DisplayName("userPasswordMatches (-)")
    void userPasswordMatchesNegative() throws DataAccessException {
        assertFalse(userDAO.userPasswordMatches("conner", "nomatch"));
    }
}

package dataAccessTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.SQLUserDAO;
import dataAccess.DataAccessException;
import model.UserData;

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
    void clear_positive() throws DataAccessException {
        userDAO.clear();
        assertEquals(0, DatabaseManager.getNumRows("user"));
    }

    @Test
    @Order(2)
    @DisplayName("createUser (+)")
    void createUser_positive() throws DataAccessException {
        userDAO.createUser("michael", "annieISOKay", "heehee@singer.com");
        userDAO.createUser("percy", "aBethisHOt", "mightypen@demigod.com");
        assertEquals(8, DatabaseManager.getNumRows("user"));
    }
    @Test
    @Order(3)
    @DisplayName("createUser (-)")
    void createUser_negative() {
    }

    @Test
    @Order(4)
    @DisplayName("getUser (+)")
    void getUser_positive() throws DataAccessException {
        UserData testRichard = new UserData("richard", "biggestmoth", "bassproshop@haken.com");
        assertEquals(testRichard, userDAO.getUser("richard"));
    }
    @Test
    @Order(5)
    @DisplayName("getUser (-)")
    void getUser_negative() {
    }

    @Test
    @Order(6)
    @DisplayName("userExists (+)")
    void userExists_positive() throws DataAccessException {
        assertTrue(userDAO.userExists("peter"));
    }
    @Test
    @Order(7)
    @DisplayName("userExists (-)")
    void userExists_negative() {
    }

    @Test
    @Order(8)
    @DisplayName("userPasswordMatches (+)")
    void userPasswordMatches_positive() throws DataAccessException {
        String connerPassword = "golowmode";
        assertTrue(userDAO.userPasswordMatches("conner", connerPassword));
    }
    @Test
    @Order(9)
    @DisplayName("userPasswordMatches (-)")
    void userPasswordMatches_negative() {
    }
}
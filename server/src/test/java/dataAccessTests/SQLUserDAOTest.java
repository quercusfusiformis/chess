package dataAccessTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.SQLUserDAO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLUserDAOTest {
    private final SQLUserDAO userDAO = new SQLUserDAO();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clear_positive() {
    }

    @Test
    @Order(2)
    @DisplayName("createUser (+)")
    void createUser_positive() {
    }
    @Test
    @Order(3)
    @DisplayName("createUser (-)")
    void createUser_negative() {
    }

    @Test
    @Order(4)
    @DisplayName("getUser (+)")
    void getUser_positive() {
    }
    @Test
    @Order(5)
    @DisplayName("getUser (-)")
    void getUser_negative() {
    }

    @Test
    @Order(6)
    @DisplayName("userExists (+)")
    void userExists_positive() {
    }
    @Test
    @Order(7)
    @DisplayName("userExists (-)")
    void userExists_negative() {
    }

    @Test
    @Order(8)
    @DisplayName("userPasswordMatches (+)")
    void userPasswordMatches_positive() {
    }
    @Test
    @Order(9)
    @DisplayName("userPasswordMatches (-)")
    void userPasswordMatches_negative() {
    }
}
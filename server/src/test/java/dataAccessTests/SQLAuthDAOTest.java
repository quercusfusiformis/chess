package dataAccessTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.SQLAuthDAO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLAuthDAOTest {
    private final SQLAuthDAO authDAO = new SQLAuthDAO();

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
    @DisplayName("createAuth (+)")
    void createAuth_positive() {
    }

    @Test
    @Order(3)
    @DisplayName("createAuth (-)")
    void createAuth_negative() {
    }

    @Test
    @Order(4)
    @DisplayName("getAuth (+)")
    void getAuth_positive() {
    }

    @Test
    @Order(5)
    @DisplayName("getAuth (-)")
    void getAuth_negative() {
    }

    @Test
    @Order(6)
    @DisplayName("getUsername (+)")
    void getUsername_positive() {
    }

    @Test
    @Order(7)
    @DisplayName("getUsername (-)")
    void getUsername_negative() {
    }

    @Test
    @Order(8)
    @DisplayName("authExists (+)")
    void authExists_positive() {
    }

    @Test
    @Order(9)
    @DisplayName("authExists (-)")
    void authExists_negative() {
    }

    @Test
    @Order(10)
    @DisplayName("deleteAuth (+)")
    void deleteAuth_positive() {
    }

    @Test
    @Order(11)
    @DisplayName("deleteAuth (-)")
    void deleteAuth_negative() {
    }
}
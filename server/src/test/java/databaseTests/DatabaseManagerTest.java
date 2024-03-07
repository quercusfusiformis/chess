package databaseTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseManagerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Order(1)
    @DisplayName("initialization")
    void initialize() throws DataAccessException {
        DatabaseManager.createDatabase();
    }
}
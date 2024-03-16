package serviceTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import service.AuthorizationService;
import service.DatabaseOperationsService;
import service.GameService;
import requestRecords.CreateGameRequest;
import requestRecords.RegisterRequest;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DatabaseOperationsServiceTests {
    private final AuthorizationService authService = new AuthorizationService();

    private final DatabaseOperationsService doService = new DatabaseOperationsService();

    private final GameService gameService = new GameService();

    private String ryanAuth;

    @BeforeEach
    void setUp() throws DataAccessException {
        this.doService.clear();
        String lorinAuth = this.authService.register(new RegisterRequest(
                "79fierykittens", "mytwentyfilthybunnies", "8@gmail.com")).authToken();
        String tammyAuth = this.authService.register(new RegisterRequest(
                "tammywammybammyyammy", "ymmayymmabymmawymmat", "3@gmail.com")).authToken();
        this.ryanAuth = this.authService.register(new RegisterRequest(
                "5john2", "thisisapen", "ryan@bop.com")).authToken();
        String rebeccaAuth = this.authService.register(new RegisterRequest(
                "becca23", "yoyoyogas", "bananabread@star.com")).authToken();
        this.authService.logout(rebeccaAuth);
        this.gameService.createGame(new CreateGameRequest("penpineappleapplepen"), this.ryanAuth);
        this.gameService.createGame(new CreateGameRequest("hiccup"), tammyAuth);
        this.gameService.createGame(new CreateGameRequest("tylerisaboss"), lorinAuth);
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        this.doService.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clearPositive() throws DataAccessException {
        this.doService.clear();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(this.ryanAuth));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}
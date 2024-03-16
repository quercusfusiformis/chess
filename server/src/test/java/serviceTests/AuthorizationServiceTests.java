package serviceTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import service.AuthorizationService;
import service.DatabaseOperationsService;
import service.GameService;
import requestRecords.RegisterRequest;
import requestRecords.LoginRequest;
import requestRecords.CreateGameRequest;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthorizationServiceTests {
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
    @DisplayName("register (+)")
    void registerPositive() throws DataAccessException {
        String tylerAuth = this.authService.register(new RegisterRequest(
                "tyleransom", "applepie1", "appletyler@pie.com")).authToken();
        this.authService.logout(tylerAuth);
    }

    @Test
    @Order(2)
    @DisplayName("register (-)")
    void registerNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.register(new RegisterRequest("tyleransom", null, "appletyler@pie.com")));
        assertEquals("Error: bad request", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("login (+)")
    void loginPositive() throws DataAccessException {
        String newAuth = this.authService.login(new LoginRequest("becca23", "yoyoyogas")).authToken();
        this.authService.logout(newAuth);
    }

    @Test
    @Order(4)
    @DisplayName("login (-)")
    void loginNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.login(new LoginRequest("becca", "yoyoyogas")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("logout (+)")
    void logoutPositive() throws DataAccessException {
        this.authService.logout(this.ryanAuth);
    }

    @Test
    @Order(6)
    @DisplayName("logout (-)")
    void logoutNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.authService.logout(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }
}
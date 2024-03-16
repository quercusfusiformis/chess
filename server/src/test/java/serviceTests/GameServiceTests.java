package serviceTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import service.AuthorizationService;
import service.DatabaseOperationsService;
import service.GameService;
import requestRecords.RegisterRequest;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import dataAccess.DataAccessException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GameServiceTests {
    private final AuthorizationService authService = new AuthorizationService();

    private final DatabaseOperationsService doService = new DatabaseOperationsService();

    private final GameService gameService = new GameService();

    private String ryanAuth;

    @BeforeEach
    void setUp() throws DataAccessException {
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
    @DisplayName("listGames (+)")
    void listGamesPositive() throws DataAccessException {
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(3, length);
    }

    @Test
    @Order(2)
    @DisplayName("listGames (-)")
    void listGamesNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> this.gameService.listGames(UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("createGame (+)")
    void createGamePositive() throws DataAccessException {
        this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth);
        int length = this.gameService.listGames(this.ryanAuth).games().size();
        assertEquals(4, length);
    }

    @Test
    @Order(4)
    @DisplayName("createGame (-)")
    void createGameNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.gameService.createGame(new CreateGameRequest("chest.org"), UUID.randomUUID().toString()));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("joinGame (+)")
    void joinGamePositive() throws DataAccessException {
        int newGameID = this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth).gameID();
        this.gameService.joinGame(new JoinGameRequest("BLACK", newGameID), ryanAuth);
    }

    @Test
    @Order(6)
    @DisplayName("joinGame (-)")
    void joinGameNegative() throws DataAccessException {
        int newGameID = this.gameService.createGame(new CreateGameRequest("chest.org"), ryanAuth).gameID();
        this.gameService.joinGame(new JoinGameRequest("BLACK", newGameID), ryanAuth);
        DataAccessException exception = assertThrows(DataAccessException.class, () ->
                this.gameService.joinGame(new JoinGameRequest("BLACK", newGameID), ryanAuth));
        assertEquals("Error: already taken", exception.getMessage());
    }
}
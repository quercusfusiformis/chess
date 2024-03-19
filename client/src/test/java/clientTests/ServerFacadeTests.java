package clientTests;

import org.junit.jupiter.api.*;
import requestRecords.CreateGameRequest;
import requestRecords.LoginRequest;
import requestRecords.RegisterRequest;
import server.Server;
import serverCommunication.CommunicationException;
import serverCommunication.ServerFacade;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;

    private static final RegisterRequest fozzieRRequest =
            new RegisterRequest("fozzie", "wackawackaheyhey", "fozzie@bear.com");
    private static final LoginRequest fozzieLInRequest = new LoginRequest("fozzie", "wackawackaheyhey");
    private static final CreateGameRequest fozzieCGRequest = new CreateGameRequest("The Funny Game");

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() { server.stop(); }

    @BeforeEach
    public void setUp() throws CommunicationException { facade.clear(); }

    @AfterEach
    public void tearDown() throws CommunicationException { facade.clear(); }

    @Test
    @Order(1)
    @DisplayName("register (+)")
    public void registerPositive() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        String kermitAuth = facade.register(new RegisterRequest("kermit", "beinggreenisprettycoolngl",
                "kermit@muppets.com")).authToken();
        String gonzoAuth = facade.register(new RegisterRequest("gonzo", "chickencannOn",
                "gonzo@thegreat.com")).authToken();
        facade.logout(fozzieAuth);
        facade.logout(kermitAuth);
        facade.logout(gonzoAuth);
    }

    @Test
    @Order(2)
    @DisplayName("register (-)")
    public void registerNegative() {
        CommunicationException exception = assertThrows(CommunicationException.class, () ->
                facade.register(new RegisterRequest("", null, "")));
        assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

    @Test
    @Order(3)
    @DisplayName("login (+)")
    public void loginPositive() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        facade.logout(fozzieAuth);
        assertInstanceOf(String.class, facade.login(fozzieLInRequest).authToken());
    }

    @Test
    @Order(4)
    @DisplayName("login (-)")
    public void loginNegative() {
        CommunicationException exception = assertThrows(CommunicationException.class, () ->  facade.login(fozzieLInRequest));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(5)
    @DisplayName("logout (+)")
    public void logoutPositive() {}

    @Test
    @Order(6)
    @DisplayName("logout (-)")
    public void logoutNegative() {}

    @Test
    @Order(7)
    @DisplayName("listGames (+)")
    public void listGamesPositive() {}

    @Test
    @Order(8)
    @DisplayName("(listGames (-)")
    public void listGamesNegative() {}

    @Test
    @Order(9)
    @DisplayName("createGame (+)")
    public void createGamePositive() {}

    @Test
    @Order(10)
    @DisplayName("createGame (-)")
    public void createGameNegative() {}

    @Test
    @Order(11)
    @DisplayName("joinGame (+)")
    public void joinGamePositive() {}

    @Test
    @Order(12)
    @DisplayName("joinGame (-)")
    public void joinGameNegative() {}

    @Test
    @Order(13)
    @DisplayName("clear (+)")
    public void clearPositive() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        facade.createGame(new CreateGameRequest("The Imitation Game"), fozzieAuth);
        facade.createGame(new CreateGameRequest("A Hat that says Gamer"), fozzieAuth);
        facade.createGame(new CreateGameRequest("Animal Well"), fozzieAuth);
        facade.clear();
        String kermitAuth = facade.register(new RegisterRequest("kermit", "beinggreenisprettycoolngl",
                "kermit@muppets.com")).authToken();
        assertEquals(0, facade.listGames(kermitAuth).games().size());
    }

    @Test
    @Order(14)
    @DisplayName("clear (-)")
    public void clearNegative() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        facade.createGame(fozzieCGRequest, fozzieAuth);
        facade.clear();
        CommunicationException exception = assertThrows(CommunicationException.class, () -> facade.logout(fozzieAuth));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }
}

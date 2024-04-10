package clientTests;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import requestRecords.LoginRequest;
import requestRecords.RegisterRequest;
import responseRecords.ListGameInfo;
import server.Server;
import serverCommunication.ServerFacade;
import serverCommunication.CommunicationException;
import webSocketMessages.userCommands.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {
    private static Server server;
    private static ServerFacade facade;

    private static final RegisterRequest fozzieRRequest =
            new RegisterRequest("fozzie", "wackawackaheyhey", "fozzie@bear.com");
    private static final LoginRequest fozzieLInRequest = new LoginRequest("fozzie", "wackawackaheyhey");

    @BeforeAll
    public static void init() {
        server = new Server();
        int port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port, "localhost:");
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
    public void logoutPositive() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        facade.logout(fozzieAuth);
        CommunicationException exception = assertThrows(CommunicationException.class, () -> facade.logout(fozzieAuth));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(6)
    @DisplayName("logout (-)")
    public void logoutNegative() {
        CommunicationException exception = assertThrows(CommunicationException.class, () -> facade.logout("hehe"));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(7)
    @DisplayName("listGames (+)")
    public void listGamesPositive() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        facade.createGame(new CreateGameRequest("The Imitation Game"), fozzieAuth);
        facade.createGame(new CreateGameRequest("A Hat that says Gamer"), fozzieAuth);
        facade.createGame(new CreateGameRequest("Animal Well"), fozzieAuth);
        ArrayList<ListGameInfo> games = facade.listGames(fozzieAuth).games();
        assertEquals(new ListGameInfo(2, null, null, "A Hat that says Gamer"),
                games.get(1));
    }

    @Test
    @Order(8)
    @DisplayName("(listGames (-)")
    public void listGamesNegative() {
        CommunicationException exception = assertThrows(CommunicationException.class, () -> facade.listGames("hehe"));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(9)
    @DisplayName("createGame (+)")
    public void createGamePositive() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        facade.createGame(new CreateGameRequest("The Imitation Game"), fozzieAuth);
        int gamerGameID = facade.createGame(new CreateGameRequest("A Hat that says Gamer"), fozzieAuth).gameID();
        facade.createGame(new CreateGameRequest("Animal Well"), fozzieAuth);
        ArrayList<ListGameInfo> games = facade.listGames(fozzieAuth).games();
        assertEquals(gamerGameID, games.get(1).gameID());
    }

    @Test
    @Order(10)
    @DisplayName("createGame (-)")
    public void createGameNegative() {
        CommunicationException exception = assertThrows(CommunicationException.class, () ->
                facade.createGame(new CreateGameRequest("New Game"), "hehe"));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(11)
    @DisplayName("joinGame (+)")
    public void joinGamePositive() throws CommunicationException {
        String kermitAuth = facade.register(new RegisterRequest("kermit", "beinggreenisprettycoolngl",
                "kermit@muppets.com")).authToken();
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        int gameID = facade.createGame(new CreateGameRequest("Muppet Showdown"), fozzieAuth).gameID();
        facade.joinGame(new JoinGameRequest("WHITE", gameID), kermitAuth);
        facade.joinGame(new JoinGameRequest("BLACK", gameID), fozzieAuth);
        ListGameInfo muppetShowdownInfo = facade.listGames(kermitAuth).games().getFirst();
        assertEquals(new ListGameInfo(1, "kermit", "fozzie", "Muppet Showdown"), muppetShowdownInfo);
    }

    @Test
    @Order(12)
    @DisplayName("joinGame (-)")
    public void joinGameNegative() throws CommunicationException {
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        int gameID = facade.createGame(new CreateGameRequest("Geri's Game"), fozzieAuth).gameID();
        CommunicationException exception = assertThrows(CommunicationException.class, () ->
                facade.joinGame(new JoinGameRequest("GREEN", gameID), fozzieAuth));
        assertEquals("Server returned: 400 Bad Request", exception.getMessage());
    }

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
        facade.clear();
        CommunicationException exception = assertThrows(CommunicationException.class, () -> facade.logout(fozzieAuth));
        assertEquals("Server returned: 401 Unauthorized", exception.getMessage());
    }

    @Test
    @Order(15)
    @DisplayName("join_withwebsocket (+)")
    public void joinWSPositive() throws CommunicationException {
        String kermitAuth = facade.register(new RegisterRequest("kermit", "beinggreenisprettycoolngl",
                "kermit@muppets.com")).authToken();
        String fozzieAuth = facade.register(fozzieRRequest).authToken();
        int gameID = facade.createGame(new CreateGameRequest("Muppet Showdown"), fozzieAuth).gameID();
        facade.joinGame(new JoinGameRequest("WHITE", gameID), kermitAuth);
        facade.leaveGame();
    }

    @Test
    @Order(16)
    @DisplayName("notification test")
    public void notification() throws CommunicationException {
        String kermitAuth = facade.register(new RegisterRequest("kermit", "beinggreenisprettycoolngl",
                "kermit@muppets.com")).authToken();
        facade.createGame(new CreateGameRequest("newgame"), kermitAuth);
        facade.listGames(kermitAuth);
        facade.sendCommand(new JoinObserverCommand(1, kermitAuth));
    }
}

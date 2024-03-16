package clientTests;

import org.junit.jupiter.api.*;
import server.Server;
import serverCommunication.ServerFacade;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    @DisplayName("register (+)")
    public void registerPositive() {}

    @Test
    @Order(2)
    @DisplayName("register (-)")
    public void registerNegative() {}

    @Test
    @Order(3)
    @DisplayName("login (+)")
    public void loginPositive() {}

    @Test
    @Order(4)
    @DisplayName("login (-)")
    public void loginNegative() {}

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
    public void clearPositive() {}

    @Test
    @Order(14)
    @DisplayName("clear (-)")
    public void clearNegative() {}
}

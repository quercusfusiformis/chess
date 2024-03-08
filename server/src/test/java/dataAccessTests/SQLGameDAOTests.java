package dataAccessTests;

import chess.ChessGame;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.SQLGameDAO;
import dataAccess.DataAccessException;
import model.GameData;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLGameDAOTests {
    private final SQLGameDAO gameDAO = new SQLGameDAO();

    private int puzzleChessGameID;

    @BeforeEach
    void setUp() throws DataAccessException {
        gameDAO.clear();
        gameDAO.createGame("Ender's Game");
        puzzleChessGameID = gameDAO.createGame("Puzzle Chess");
        gameDAO.createGame("FPS Chess Test");
    }

    @AfterEach
    void tearDown() throws DataAccessException {
        gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("clear (+)")
    void clear_positive() throws DataAccessException {
        gameDAO.clear();
        assertEquals(0, DatabaseManager.getNumRows("game"));
    }

    @Test
    @Order(2)
    @DisplayName("createGame (+)")
    void createGame_positive() throws DataAccessException {
        gameDAO.createGame("The Game of Love");
        gameDAO.createGame("GameGrumps");
        assertEquals(5, DatabaseManager.getNumRows("game"));
    }

    @Test
    @Order(3)
    @DisplayName("createGame (-)")
    void createGame_negative() {
    }

    @Test
    @Order(4)
    @DisplayName("getGame (+)")
    void getGame_positive() throws DataAccessException {
        String newGameJson = new Gson().toJson(new ChessGame());
        GameData expectedGame = new GameData(puzzleChessGameID, null, null, "Puzzle Chess", newGameJson);
        assertEquals(expectedGame, gameDAO.getGame(puzzleChessGameID));
    }

    @Test
    @Order(5)
    @DisplayName("getGame (-)")
    void getGame_negative() {
    }

    @Test
    @Order(6)
    @DisplayName("gameExists (+)")
    void gameExists_positive() {
    }

    @Test
    @Order(7)
    @DisplayName("gameExists (-)")
    void gameExists_negative() {
    }

    @Test
    @Order(8)
    @DisplayName("listGames (+)")
    void listGames_positive() {
    }

    @Test
    @Order(9)
    @DisplayName("listGames (-)")
    void listGames_negative() {
    }

    @Test
    @Order(10)
    @DisplayName("updatePlayerInGame (+)")
    void updatePlayerInGame_positive() {
    }

    @Test
    @Order(11)
    @DisplayName("updatePlayerInGame (-)")
    void updatePlayerInGame_negative() {
    }

    @Test
    @Order(12)
    @DisplayName("colorFreeInGame (+)")
    void colorFreeInGame_positive() {
    }

    @Test
    @Order(13)
    @DisplayName("colorFreeInGame (-)")
    void colorFreeInGame_negative() {
    }

    @Test
    @Order(14)
    @DisplayName("joinGameAsPlayer (+)")
    void joinGameAsPlayer_positive() {
    }

    @Test
    @Order(15)
    @DisplayName("joinGameAsPlayer (-)")
    void joinGameAsPlayer_negative() {
    }
}
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

    private final String defaultGame = new Gson().toJson(new ChessGame());

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
    @DisplayName("addGame (+)")
    void addGame_positive() throws DataAccessException {
        gameDAO.addGame(76, "Conner", "Jack", "The Big Game", "{\"field\":\"hehe\"}");
        assertEquals(4, DatabaseManager.getNumRows("game"));
    }

    @Test
    @Order(6)
    @DisplayName("addGame (-)")
    void addGame_negative() throws DataAccessException {
    }

    @Test
    @Order(8)
    @DisplayName("delGame (+)")
    void delGame_positive() throws DataAccessException {
        gameDAO.delGame(puzzleChessGameID);
        assertEquals(2, DatabaseManager.getNumRows("game"));
    }

    @Test
    @Order(8)
    @DisplayName("delGame (-)")
    void delGame_negative() throws DataAccessException {
    }

    @Test
    @Order(6)
    @DisplayName("gameExists (+)")
    void gameExists_positive() throws DataAccessException {
        assertTrue(gameDAO.gameExists(puzzleChessGameID));
    }

    @Test
    @Order(7)
    @DisplayName("gameExists (-)")
    void gameExists_negative() {
    }

    @Test
    @Order(8)
    @DisplayName("listGames (+)")
    void listGames_positive() throws DataAccessException {
        assertEquals(3, gameDAO.listGames().size());
    }

    @Test
    @Order(9)
    @DisplayName("listGames (-)")
    void listGames_negative() {
    }

    @Test
    @Order(10)
    @DisplayName("updatePlayerInGame (+)")
    void updatePlayerInGame_positive() throws DataAccessException {
        GameData expectedGame = new GameData(puzzleChessGameID, "baloony", null, "Puzzle Chess", defaultGame);
        gameDAO.updatePlayerInGame(puzzleChessGameID, "baloony", "WHITE");
        assertEquals(expectedGame, gameDAO.getGame(puzzleChessGameID));
    }

    @Test
    @Order(11)
    @DisplayName("updatePlayerInGame (-)")
    void updatePlayerInGame_negative() {
    }

    @Test
    @Order(12)
    @DisplayName("colorFreeInGame (+)")
    void colorFreeInGame_positive() throws DataAccessException {
        assertTrue(gameDAO.colorFreeInGame("WHITE", puzzleChessGameID));
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
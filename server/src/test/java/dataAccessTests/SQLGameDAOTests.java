package dataAccessTests;

import com.google.gson.Gson;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dataAccess.DatabaseManager;
import dataAccess.SQLGameDAO;
import dataAccess.DataAccessException;
import model.GameData;
import chess.ChessGame;

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
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.createGame(new Gson().toJson(new ChessGame())));
        assertEquals("Data truncation: Data too long for column 'gameName' at row 1", exception.getMessage());
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
    void addGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.addGame(-1, null, null, null, null));
        assertEquals("Column 'gameName' cannot be null", exception.getMessage());
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
    void delGame_negative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.delGame(90));
        assertEquals("Error: no game with given gameID", exception.getMessage());
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
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.getGame(89));
        assertEquals("Error: no game with given gameID", exception.getMessage());
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
    void gameExists_negative() throws DataAccessException {
        assertFalse(gameDAO.gameExists(745));
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
    void listGames_negative() throws DataAccessException {
        assertNotEquals(7, gameDAO.listGames().size());
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
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gameDAO.updatePlayerInGame(puzzleChessGameID, "dally", "BLUE"));
        assertEquals("Error: invalid color parameter", exception.getMessage());
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
    void colorFreeInGame_negative() throws DataAccessException {
        gameDAO.updatePlayerInGame(puzzleChessGameID, "bobby", "WHITE");
        assertFalse(gameDAO.colorFreeInGame("WHITE", puzzleChessGameID));
    }

    @Test
    @Order(14)
    @DisplayName("joinGameAsPlayer (+)")
    void joinGameAsPlayer_positive() throws DataAccessException {
        gameDAO.updatePlayerInGame(puzzleChessGameID, "bobby", "WHITE");
        gameDAO.updatePlayerInGame(puzzleChessGameID, "hobby", "BLACK");
        GameData expectedGame = new GameData(puzzleChessGameID, "bobby", "hobby", "Puzzle Chess", defaultGame);
        assertEquals(expectedGame, gameDAO.getGame(puzzleChessGameID));
    }

    @Test
    @Order(15)
    @DisplayName("joinGameAsPlayer (-)")
    void joinGameAsPlayer_negative() throws DataAccessException {
        gameDAO.updatePlayerInGame(puzzleChessGameID, "bobby", "WHITE");
        gameDAO.updatePlayerInGame(puzzleChessGameID, "hobby", "BLACK");
        DataAccessException exception = assertThrows(DataAccessException.class, () -> gameDAO.joinGameAsPlayer(puzzleChessGameID, "gobby", "WHITE"));
        assertEquals("Error: already taken", exception.getMessage());
    }
}

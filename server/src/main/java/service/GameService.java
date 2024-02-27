package service;

import java.util.Collection;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;

public class GameService {
    private GameDAO gameDAO = new MemoryGameDAO();

    public Collection<GameData> listGames() {
        return gameDAO.listGames();
    }

    public int createGame(CreateGameRequest cgRequest) {
        return gameDAO.createGame(cgRequest.gameName());
    }

    public void joinGame(JoinGameRequest jgRequest) {
        // Not gonna implement this yet: seems pretty complicated
    }
}

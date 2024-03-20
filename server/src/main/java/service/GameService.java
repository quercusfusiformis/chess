package service;

import java.util.ArrayList;
import model.GameData;
import requestRecords.CreateGameRequest;
import requestRecords.GetGameRequest;
import requestRecords.JoinGameRequest;
import responseRecords.GetGameResponse;
import responseRecords.ListGamesResponse;
import responseRecords.ListGameInfo;
import responseRecords.CreateGameResponse;
import dataAccess.*;

public class GameService {
    private final AuthDAO authDAO = new SQLAuthDAO();

    private final GameDAO gameDAO = new SQLGameDAO();

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            ArrayList<ListGameInfo> formattedGames = new ArrayList<>();
            ArrayList<GameData> gameList = (ArrayList<GameData>) gameDAO.listGames();
            for (GameData game : gameList) {
                formattedGames.add(new ListGameInfo(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }
            return new ListGamesResponse(formattedGames);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public CreateGameResponse createGame(CreateGameRequest cgRequest, String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            return new CreateGameResponse(gameDAO.createGame(cgRequest.gameName()));
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public void joinGame(JoinGameRequest jgRequest, String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            String username = authDAO.getUsername(authToken);
            if (gameDAO.gameExists(jgRequest.gameID())) {
                if (!(jgRequest.playerColor() == null)) {
                    try {
                        gameDAO.joinGameAsPlayer(jgRequest.gameID(), username, jgRequest.playerColor());
                    }
                    catch (IllegalArgumentException ex) {
                        throw new DataAccessException("Error: bad request");
                    }
                }
                // If playerColor is null, you do nothing for phase 3? Specs don't give a place to store observers
            }
            else {
                throw new DataAccessException("Error: bad request");
            }
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    public GetGameResponse getGame(GetGameRequest ggRequest, String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            if (gameDAO.gameExists(ggRequest.gameID())) {
                GameData game;
                try {
                    game = gameDAO.getGame(ggRequest.gameID());
                } catch (DataAccessException ex) { throw new DataAccessException("Error: bad request"); }
                return new GetGameResponse(game.gameID(),
                        game.whiteUsername(),
                        game.blackUsername(),
                        game.gameName(),
                        game.game());
            }
            else {
                throw new DataAccessException("Error: bad request");
            }
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}

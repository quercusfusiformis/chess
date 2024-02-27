package service;

import java.util.ArrayList;

import dataAccess.*;
import model.GameData;
import requestRecords.CreateGameRequest;
import requestRecords.JoinGameRequest;
import responseRecords.CreateGameResponse;
import responseRecords.ListGameInfo;
import responseRecords.ListGamesResponse;

public class GameService {
    private AuthDAO authDAO = new MemoryAuthDAO();

    private GameDAO gameDAO = new MemoryGameDAO();

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
            throw new DataAccessException("unauthorized");
        }
    }

    public CreateGameResponse createGame(CreateGameRequest cgRequest, String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            return new CreateGameResponse(gameDAO.createGame(cgRequest.gameName()));
        }
        else {
            throw new DataAccessException("unauthorized");
        }
    }

    public void joinGame(JoinGameRequest jgRequest, String authToken) throws DataAccessException {
        if (authDAO.authExists(authToken)) {
            if (gameDAO.gameExists(jgRequest.gameID())) {
                if (!(jgRequest.playerColor() == null)) {
                    try {

                    }
                }
            }
            else {
                throw new DataAccessException("bad request");
            }
        }
        else {
            throw new DataAccessException("unauthorized");
        }
    }
}

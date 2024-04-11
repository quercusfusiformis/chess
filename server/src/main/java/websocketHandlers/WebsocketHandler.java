package websocketHandlers;

import chess.ChessMove;
import dataAccess.*;
import model.GameData;

public class WebsocketHandler {
    private final AuthDAO authDAO = new SQLAuthDAO();
    private final GameDAO gameDAO = new SQLGameDAO();

//    public GameData leaveGame(int gameID, String color, String authToken) throws DataAccessException {
//        if (authDAO.authExists(authToken)) {
//            String username = authDAO.getUsername(authToken);
//            if (gameDAO.gameExists(jgRequest.gameID())) {
//                if (!(jgRequest.playerColor() == null)) {
//                    try {
//                        gameDAO.joinGameAsPlayer(jgRequest.gameID(), username, jgRequest.playerColor());
//                    }
//                    catch (IllegalArgumentException ex) {
//                        throw new DataAccessException("Error: bad request");
//                    }
//                }
//                // If playerColor is null, you do nothing for phase 3? Specs don't give a place to store observers
//            }
//            else {
//                throw new DataAccessException("Error: bad request");
//            }
//        }
//        else {
//            throw new DataAccessException("Error: unauthorized");
//        }
//    }
//        return new GameData(-1, null, null, null, null);
//    }

    public GameData makeMove(int gameID, ChessMove move, String authToken) {
        return new GameData(-1, null, null, null, null);
    }

    public GameData resign(int gameID, String color, String authToken) {
        return new GameData(-1, null, null, null, null);
    }
}

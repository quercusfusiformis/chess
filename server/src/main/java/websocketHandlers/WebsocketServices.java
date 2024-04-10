package websocketHandlers;

import chess.ChessMove;
import model.GameData;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;

public class WebsocketServices {
    private final AuthDAO authDAO = new SQLAuthDAO();
    private final GameDAO gameDAO = new SQLGameDAO();

    public GameData leaveGame(int gameID, String color, String authToken) {
        return new GameData(-1, null, null, null, null);
    }

    public GameData makeMove(int gameID, ChessMove move, String authToken) {
        return new GameData(-1, null, null, null, null);
    }

    public GameData resign(int gameID, String color, String authToken) {
        return new GameData(-1, null, null, null, null);
    }
}

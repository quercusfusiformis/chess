package websocketService;

import chess.ChessMove;
import dataAccess.*;
import model.GameData;
import webSocketMessages.serverMessages.*;

public class WebsocketService {
    private final SQLAuthDAO authDAO = new SQLAuthDAO();
    private final SQLGameDAO gameDAO = new SQLGameDAO();

    public ServerMessage leaveGameAsPlayer(int gameID, String authToken) {
        ServerMessage leaveMessage = new ServerErrorMessage("LEAVEERROR", "Leaving must not have worked as expected.");
        try {
            if (authDAO.authExists(authToken)) {
                String username = authDAO.getUsername(authToken);
                if (gameDAO.gameExists(gameID)) {
                    String color = gameDAO.getPlayerColor(gameID, username);
                    if (!(color == null)) {
                        try {
                            if (gameDAO.isPlayerColor(gameID, username, color)) {
                                gameDAO.updatePlayerInGame(gameID, null, color);
                                leaveMessage = new ServerNotification("Player \"" + username + "\" has left the game.");
                            } else {
                                throw new DataAccessException("Bad request");
                            }
                        } catch (IllegalArgumentException ex) {
                            throw new DataAccessException("Bad request");
                        }
                    }
                } else {
                    throw new DataAccessException("Bad request");
                }
            } else {
                throw new DataAccessException("Unauthorized");
            }
        } catch (DataAccessException ex) {
            leaveMessage =  new ServerErrorMessage(ex);
        }
        return leaveMessage;
    }

    public ServerMessage makeMove(int gameID, ChessMove move, String authToken) {
        return new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
    }

    public ServerMessage resign(int gameID, String color, String authToken) {
        return new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
    }

    public ServerMessage getGame(int gameID) {
        try {
            return new ServerLoadGameMessage(gameDAO.getGame(gameID));
        } catch (DataAccessException ex) {
            return new ServerErrorMessage(ex);
        }
    }
}

package websocketService;

import com.google.gson.Gson;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import dataAccess.*;
import webSocketMessages.serverMessages.*;

public class WebsocketService {
    private final SQLAuthDAO authDAO = new SQLAuthDAO();
    private final SQLGameDAO gameDAO = new SQLGameDAO();

    public ServerMessage joinGameAsPlayer(int gameID, ChessGame.TeamColor requestedColor, String authToken) {
        ServerMessage joinMessage;
        try {
            if (authDAO.authExists(authToken)) {
                String username = authDAO.getUsername(authToken);
                if (gameDAO.gameExists(gameID)) {
                    ChessGame.TeamColor color = gameDAO.getPlayerColor(gameID, username);
                    if (requestedColor.equals(color)) {
                        joinMessage = new ServerNotification("User \"" + username + "\" has joined the game as the " + color + " player.");
                    } else {
                        throw new DataAccessException("Bad join as player request");
                    }
                } else {
                    throw new DataAccessException("Bad join as player request");
                }
            } else {
                throw new DataAccessException("Unauthorized");
            }
        } catch (DataAccessException ex) {
            joinMessage =  new ServerErrorMessage(ex);
        }
        return joinMessage;
    }

    public ServerMessage joinGameAsObserver(int gameID, String authToken) {
        ServerMessage joinMessage;
        try {
            if (authDAO.authExists(authToken)) {
                String username = authDAO.getUsername(authToken);
                if (gameDAO.gameExists(gameID)) {
                    joinMessage = new ServerNotification("User \"" + username + "\" has joined the game as an observer.");
                } else {
                    throw new DataAccessException("Bad join as observer request");
                }
            } else {
                throw new DataAccessException("Unauthorized");
            }
        } catch (DataAccessException ex) {
            joinMessage =  new ServerErrorMessage(ex);
        }
        return joinMessage;
    }

    public ServerMessage leaveGame(int gameID, String authToken) {
        ServerMessage leaveMessage;
        try {
            if (authDAO.authExists(authToken)) {
                String username = authDAO.getUsername(authToken);
                if (gameDAO.gameExists(gameID)) {
                    ChessGame.TeamColor color = gameDAO.getPlayerColor(gameID, username);
                    if (color != null) {
                        try {
                            if (gameDAO.isPlayerColor(gameID, username, color)) {
                                gameDAO.updatePlayerInGame(gameID, null, color);
                                leaveMessage = new ServerNotification("The " + color + " player \"" + username + "\" has left the game.");
                            } else {
                                throw new DataAccessException("Bad leave request");
                            }
                        } catch (IllegalArgumentException ex) {
                            throw new DataAccessException("Bad leave request");
                        }
                    } else {
                        leaveMessage = new ServerNotification("Observer \"" + username + "\" has left the game.");
                    }
                } else {
                    throw new DataAccessException("Bad leave request");
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
        ServerMessage moveMessage;
        try {
            if (authDAO.authExists(authToken)) {
                String username = authDAO.getUsername(authToken);
                if (gameDAO.gameExists(gameID)) {
                    ChessGame.TeamColor color = gameDAO.getPlayerColor(gameID, username);
                    if (color != null) {
                        try {
                            if (gameDAO.isPlayerColor(gameID, username, color)) {
                                ChessGame.TeamColor currTeamTurn = gameDAO.getCurrTeamTurn(gameID);
                                if (currTeamTurn == null) {
                                    throw new DataAccessException("Error: game is over");
                                } else if (currTeamTurn != color) {
                                    throw new DataAccessException("Error: cannot move on another player's turn");
                                } else {
                                    gameDAO.makeMove(gameID, move);
                                    ChessPiece movedPiece = getMovedPiece(gameID, move);
                                    moveMessage = new ServerNotification("The " + color + " player \"" +
                                            username + "\" moved a " + movedPiece.getPieceType() + " from " +
                                            positionToCoord(move.getStartPosition()) + " to " +
                                            positionToCoord(move.getEndPosition()) + ".");
                                }
                            } else {
                                throw new DataAccessException("Bad move request");
                            }
                        } catch (IllegalArgumentException ex) {
                            throw new DataAccessException("Bad move request");
                        }
                    } else {
                        throw new DataAccessException("Bad move request");
                    }
                } else {
                    throw new DataAccessException("Bad move request");
                }
            } else {
                throw new DataAccessException("Unauthorized");
            }
        } catch (DataAccessException ex) {
            moveMessage =  new ServerErrorMessage(ex);
        }
        return moveMessage;
    }

    public ServerMessage resign(int gameID, String authToken) {
        ServerMessage resignMessage;
        try {
            if (authDAO.authExists(authToken)) {
                String username = authDAO.getUsername(authToken);
                if (gameDAO.gameExists(gameID)) {
                    ChessGame.TeamColor color = gameDAO.getPlayerColor(gameID, username);
                    if (color != null) {
                        try {
                            if (gameDAO.isPlayerColor(gameID, username, color)) {
                                ChessGame.TeamColor currTeamTurn = gameDAO.getCurrTeamTurn(gameID);
                                if (currTeamTurn == null) {
                                    throw new DataAccessException("Error: game is over");
                                } else {
                                    gameDAO.setTeamTurnNull(gameID);
                                    resignMessage = new ServerNotification("The " + color + " player \"" +
                                        username + "\" resigned. ");
                                }
                            } else {
                                throw new DataAccessException("Bad resign request");
                            }
                        } catch (IllegalArgumentException ex) {
                            throw new DataAccessException("Bad resign request");
                        }
                    } else {
                        throw new DataAccessException("Bad resign request");
                    }
                } else {
                    throw new DataAccessException("Bad resign request");
                }
            } else {
                throw new DataAccessException("Unauthorized");
            }
        } catch (DataAccessException ex) {
            resignMessage =  new ServerErrorMessage(ex);
        }
        return resignMessage;
    }

    public ServerMessage getLoadGameMessage(int gameID) {
        try {
            return new ServerLoadGameMessage(gameDAO.getGame(gameID));
        } catch (DataAccessException ex) {
            return new ServerErrorMessage(ex);
        }
    }

    public ServerMessage checkAdjustGameState(int gameID) throws DataAccessException {
        ServerMessage gameStateMessage = null;
        GameData gameData = gameDAO.getGame(gameID);
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        ChessGame game = new Gson().fromJson(gameData.game(), ChessGame.class);
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            gameStateMessage = new ServerNotification("The WHITE player \"" + whiteUsername +
                    "\" is in checkmate. The BLACK player \"" + blackUsername + "\" wins.");
            gameDAO.setTeamTurnNull(gameID);
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            gameStateMessage = new ServerNotification("The BLACK player \"" + blackUsername +
                    "\" is in checkmate. The WHITE player \"" + whiteUsername + "\" wins.");
            gameDAO.setTeamTurnNull(gameID);
        } else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            gameStateMessage = new ServerNotification("The WHITE player \"" + whiteUsername +
                    "\" is in stalemate (has no valid moves). The BLACK player \"" + blackUsername + "\" wins.");
            gameDAO.setTeamTurnNull(gameID);
        } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            gameStateMessage = new ServerNotification("The BLACK player \"" + blackUsername +
                    "\" is in stalemate (has no valid moves). The WHITE player \"" + whiteUsername + "\" wins.");
            gameDAO.setTeamTurnNull(gameID);
        } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            gameStateMessage = new ServerNotification("The WHITE player \"" + whiteUsername + "\" is in check.");
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            gameStateMessage = new ServerNotification("The BLACK player \"" + blackUsername + "\" is in check.");
        }
        return gameStateMessage;
    }

    private ChessPiece getMovedPiece(int gameID, ChessMove move) throws DataAccessException {
        return new Gson().fromJson(gameDAO.getGame(gameID).game(), ChessGame.class).getBoard().getPiece(move.getEndPosition());
    }

    private static String positionToCoord(ChessPosition position) {
        int toLetter = position.getColumn();
        String letter = "x";
        switch (toLetter) {
            case 1 -> letter = "a";
            case 2 -> letter = "b";
            case 3 -> letter = "c";
            case 4 -> letter = "d";
            case 5 -> letter = "e";
            case 6 -> letter = "f";
            case 7 -> letter = "g";
            case 8 -> letter = "h";
        }
        int number = position.getRow();
        return letter + number;
    }
}

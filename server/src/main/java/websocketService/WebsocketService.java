package websocketService;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataAccess.*;
import logging.ServerLogger;
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
                                leaveMessage = new ServerNotification("Player \"" + username + "\" has left the game.");
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
                                ServerLogger.logger.info("Before making move, currTeamTurn: " + currTeamTurn);
                                if (currTeamTurn == null) {
                                    throw new DataAccessException("Error: game is over");
                                } else if (currTeamTurn != color) {
                                    throw new DataAccessException("Error: cannot move on another player's turn");
                                } else {
                                    gameDAO.makeMove(gameID, move);
                                    moveMessage = new ServerNotification("The " + color + " player \"" +
                                            username + "\" moved a piece from position " +
                                            positionToCoord(move.getStartPosition()) + " to position " +
                                            positionToCoord(move.getEndPosition()) + ".");
                                    ServerLogger.logger.info("After making move, currTeamTurn: " + gameDAO.getCurrTeamTurn(gameID));
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
                                ServerLogger.logger.info("Before resigning, currTeamTurn: " + currTeamTurn);
                                if (currTeamTurn == null) {
                                    throw new DataAccessException("Error: game is over");
                                } else {
                                    gameDAO.setTeamTurnNull(gameID);
                                    resignMessage = new ServerNotification("The " + color + " player \"" +
                                        username + "\" resigned. ");
                                    ServerLogger.logger.info("After resigning, currTeamTurn: " + gameDAO.getCurrTeamTurn(gameID));
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

    public ServerMessage getGame(int gameID) {
        try {
            return new ServerLoadGameMessage(gameDAO.getGame(gameID));
        } catch (DataAccessException ex) {
            return new ServerErrorMessage(ex);
        }
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

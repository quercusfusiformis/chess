package serverCommunication;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collection;

import model.GameData;
import requestRecords.*;
import responseRecords.*;
import ui.BoardToStringUtil;
import webSocketMessages.userCommands.*;

public class ServerFacade {
    private final HttpCommunicator httpCommunicator;
    private final WebsocketCommunicator websocketCommunicator;

    public ServerFacade(int serverPort, String urlStem) {
        this.httpCommunicator = new HttpCommunicator(serverPort, urlStem);
        this.websocketCommunicator = new WebsocketCommunicator(serverPort, urlStem);
    }

    public RegisterResponse register(RegisterRequest request) throws CommunicationException {
        RegisterResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/user", "POST", body);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            else { response = HttpCommunicator.getHTTPResponseBody(connection, RegisterResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public LoginResponse login(LoginRequest request) throws CommunicationException {
        LoginResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/session", "POST", body);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            else { response = HttpCommunicator.getHTTPResponseBody(connection, LoginResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void logout(String authToken) throws CommunicationException {
        try {
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/session", "DELETE", "", authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public ListGamesResponse listGames(String authToken) throws CommunicationException {
        ListGamesResponse response;
        try {
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/game", "GET", "", authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            response = HttpCommunicator.getHTTPResponseBody(connection, ListGamesResponse.class);
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws CommunicationException {
        CreateGameResponse response = null;
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/game", "POST", body, authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            else { response = HttpCommunicator.getHTTPResponseBody(connection, CreateGameResponse.class); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
        return response;
    }

    public void joinGame(JoinGameRequest request, String authToken) throws CommunicationException {
        try {
            String body = new Gson().toJson(request);
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/game", "PUT", body, authToken);
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
            this.websocketCommunicator.ensureOpenSession();
            if (request.playerColor() != null) {
                if (request.playerColor().equals(ChessGame.TeamColor.WHITE)) {
                    this.websocketCommunicator.sendCommand(new JoinPlayerCommand(request.gameID(), ChessGame.TeamColor.WHITE, authToken));
                    this.websocketCommunicator.setPlayerGameColor(ChessGame.TeamColor.WHITE);
                } else if (request.playerColor().equals(ChessGame.TeamColor.BLACK)) {
                    this.websocketCommunicator.sendCommand(new JoinPlayerCommand(request.gameID(), ChessGame.TeamColor.BLACK, authToken));
                    this.websocketCommunicator.setPlayerGameColor(ChessGame.TeamColor.BLACK);
                }
            } else {
                this.websocketCommunicator.sendCommand(new JoinObserverCommand(request.gameID(), authToken));
                this.websocketCommunicator.setPlayerGameColor(null);
            }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    public String redrawBoard() throws CommunicationException {
        ChessBoard board = new Gson().fromJson(getWSSessionGame().game(), ChessGame.class).getBoard();
        ChessGame.TeamColor color = getWSSessionGamePlayerColor();
        return BoardToStringUtil.getBoard(board, color);
    }

    public void leaveGame(String authToken) throws CommunicationException {
        try {
            int gameID = getWSSessionGame().gameID();
            this.websocketCommunicator.ensureOpenSession();
            this.websocketCommunicator.sendCommand(new LeaveGameCommand(gameID, authToken));
            this.websocketCommunicator.setGameData(null);
            this.websocketCommunicator.setPlayerGameColor(null);
            this.websocketCommunicator.closeSession();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMove() {}

    public void resign() {}

    public String highlightLegalMoves(ChessPosition position) throws CommunicationException {
        ChessBoard board = new Gson().fromJson(getWSSessionGame().game(), ChessGame.class).getBoard();
        ChessGame.TeamColor color = getWSSessionGamePlayerColor();
        if (color == null) { color = ChessGame.TeamColor.WHITE; }
        ChessPiece posPiece = board.getPiece(position);
        if (posPiece != null) {
            Collection<ChessPosition> validMoves = BoardToStringUtil.getMoveResults(posPiece.pieceMoves(board, position));
            return BoardToStringUtil.getHighlightedBoard(board, color, position, validMoves);
        } else {
            throw new CommunicationException("No piece in that position of the board.");
        }
    }

    public void clear() throws CommunicationException {
        try {
            HttpURLConnection connection = this.httpCommunicator.makeHTTPRequest("/db", "DELETE", "");
            if (!(HttpCommunicator.hasGoodResponseCode(connection))) { HttpCommunicator.throwResponseError(connection); }
        } catch (Exception ex) { throw new CommunicationException(ex.getMessage()); }
    }

    private void sendCommand(UserGameCommand command) {
        this.websocketCommunicator.ensureOpenSession();
        try {
            this.websocketCommunicator.sendCommand(command);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GameData getWSSessionGame() throws CommunicationException {
        GameData gameData = websocketCommunicator.getGameData();
        if (gameData != null) {
            return gameData;
        } else {
            throw new CommunicationException("Websocket game is null");
        }
    }

    private ChessGame.TeamColor getWSSessionGamePlayerColor() {
        return websocketCommunicator.getPlayerGameColor();
    }
}

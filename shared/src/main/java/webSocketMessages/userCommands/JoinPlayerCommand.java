package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

public class JoinPlayerCommand extends UserGameCommand {
    private final ChessGame.TeamColor playerColor;
    private final int gameID;

    public JoinPlayerCommand(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        super(CommandType.JOIN_PLAYER, authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor() { return playerColor; }

    public int getGameID() { return gameID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinPlayerCommand that = (JoinPlayerCommand) o;
        return gameID == that.gameID && Objects.equals(playerColor, that.playerColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), playerColor, gameID);
    }

    @Override
    public String toString() {
        return "JoinPlayerCommand{" +
                "commandType=" + commandType +
                ", gameID=" + gameID +
                ", playerColor='" + playerColor + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}

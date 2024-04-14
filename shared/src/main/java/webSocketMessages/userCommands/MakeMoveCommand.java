package webSocketMessages.userCommands;

import java.util.Objects;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameID;
    private final ChessMove move;

    public MakeMoveCommand(int gameID, ChessMove move, String authToken) {
        super(CommandType.MAKE_MOVE, authToken);
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() { return gameID; }

    public ChessMove getMove() { return move; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MakeMoveCommand that = (MakeMoveCommand) o;
        return gameID == that.gameID && Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID, move);
    }

    @Override
    public String toString() {
        return "MakeMoveCommand{" +
                "commandType=" + commandType +
                ", gameID=" + gameID +
                ", move=" + move +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}

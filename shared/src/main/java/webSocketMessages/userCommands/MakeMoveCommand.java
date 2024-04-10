package webSocketMessages.userCommands;

import chess.ChessPosition;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand {
    ChessPosition startPos;
    ChessPosition endPos;

    public MakeMoveCommand(ChessPosition startPos, ChessPosition endPos, String authToken) {
        super(CommandType.MAKE_MOVE, authToken);
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public ChessPosition getStartPos() { return startPos; }

    public ChessPosition getEndPos() {return endPos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MakeMoveCommand that = (MakeMoveCommand) o;
        return Objects.equals(startPos, that.startPos) && Objects.equals(endPos, that.endPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), startPos, endPos);
    }
}

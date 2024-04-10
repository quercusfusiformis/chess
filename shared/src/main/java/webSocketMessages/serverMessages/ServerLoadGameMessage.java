package webSocketMessages.serverMessages;

import chess.ChessGame;

import java.util.Objects;

public class ServerLoadGameMessage extends ServerMessage {
    private final ChessGame game;

    public ServerLoadGameMessage(ServerMessageType type, ChessGame game) {
        super(type);
        this.game = game;
    }

    public ChessGame getBoard() { return game; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerLoadGameMessage that = (ServerLoadGameMessage) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game);
    }
}

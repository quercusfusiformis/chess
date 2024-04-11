package webSocketMessages.serverMessages;

import model.GameData;

import java.util.Objects;

public class ServerLoadGameMessage extends ServerMessage {
    private final GameData game;

    public ServerLoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getBoard() { return game; }

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

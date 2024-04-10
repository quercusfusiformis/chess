package webSocketMessages.userCommands;

import java.util.Objects;

public class JoinPlayerCommand extends UserGameCommand {
    private final String requestedColor;
    private final int requestedGameID;

    public JoinPlayerCommand(String requestedColor, int requestedGameID, String authToken) {
        super(CommandType.JOIN_PLAYER, authToken);
        this.requestedColor = requestedColor;
        this.requestedGameID = requestedGameID;
    }

    public String getRequestedColor() { return requestedColor; }

    public int getRequestedGameID() { return requestedGameID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinPlayerCommand that = (JoinPlayerCommand) o;
        return requestedGameID == that.requestedGameID && Objects.equals(requestedColor, that.requestedColor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requestedColor, requestedGameID);
    }
}

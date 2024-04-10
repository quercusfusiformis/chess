package webSocketMessages.userCommands;

import java.util.Objects;

public class JoinObserverCommand extends UserGameCommand {
    private final int requestedGameID;

    public JoinObserverCommand(int requestedGameID, String authToken) {
        super(CommandType.JOIN_OBSERVER, authToken);
        this.requestedGameID = requestedGameID;
    }

    public int getRequestedGameID() { return requestedGameID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinObserverCommand that = (JoinObserverCommand) o;
        return requestedGameID == that.requestedGameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), requestedGameID);
    }
}

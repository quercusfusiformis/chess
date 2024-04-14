package webSocketMessages.userCommands;

import java.util.Objects;

public class LeaveGameCommand extends UserGameCommand {
    private final int gameID;

    public LeaveGameCommand(int gameID, String authToken) {
        super(CommandType.LEAVE, authToken);
        this.gameID = gameID;
    }

    public int getGameID() { return gameID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LeaveGameCommand that = (LeaveGameCommand) o;
        return gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID);
    }

    @Override
    public String toString() {
        return "LeaveGameCommand{" +
                "commandType=" + commandType +
                ", gameID=" + gameID +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}

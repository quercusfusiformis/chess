package webSocketMessages.userCommands;

import java.util.Objects;

public class ResignGameCommand extends UserGameCommand {
    private final int gameID;

    public ResignGameCommand(int gameID, String authToken) {
        super(CommandType.RESIGN, authToken);
        this.gameID = gameID;
    }

    public int getGameID() { return gameID; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ResignGameCommand that = (ResignGameCommand) o;
        return gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID);
    }

    @Override
    public String toString() {
        return "ResignGameCommand{" +
                "commandType=" + commandType +
                ", gameID=" + gameID +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}

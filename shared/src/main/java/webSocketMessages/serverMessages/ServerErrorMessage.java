package webSocketMessages.serverMessages;

import java.util.Objects;

public class ServerErrorMessage extends ServerMessage {
    private final String errorName;
    private final String errorMessage;

    public ServerErrorMessage(ServerMessageType type, String errorName, String errorMessage) {
        super(type);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    public String getErrorName() { return errorName; }

    public String getErrorMessage() { return errorMessage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerErrorMessage that = (ServerErrorMessage) o;
        return Objects.equals(errorName, that.errorName) && Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errorName, errorMessage);
    }
}

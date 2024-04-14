package webSocketMessages.serverMessages;

import java.util.Objects;

public class ServerErrorMessage extends ServerMessage {
    private final String errorName;
    private final String errorMessage;

    public ServerErrorMessage(String errorName, String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorName = errorName;
        this.errorMessage = errorMessage;
    }

    public ServerErrorMessage(Throwable ex) {
        super(ServerMessageType.ERROR);
        this.errorName = ex.getClass().getName();
        this.errorMessage = ex.getMessage();
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

    @Override
    public String toString() {
        return "ServerErrorMessage{" +
                "serverMessageType=" + serverMessageType +
                ", errorName='" + errorName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}

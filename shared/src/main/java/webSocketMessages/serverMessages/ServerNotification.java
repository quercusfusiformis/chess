package webSocketMessages.serverMessages;

import java.util.Objects;

public class ServerNotification extends ServerMessage {
    private final String notification;

    public ServerNotification(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.notification = notification;
    }

    public String getNotification() { return notification; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServerNotification that = (ServerNotification) o;
        return Objects.equals(notification, that.notification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), notification);
    }
}

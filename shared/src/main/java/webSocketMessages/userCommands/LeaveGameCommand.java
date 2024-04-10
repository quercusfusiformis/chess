package webSocketMessages.userCommands;

public class LeaveGameCommand extends UserGameCommand {

    public LeaveGameCommand(String authToken) {
        super(CommandType.LEAVE, authToken);
    }
}

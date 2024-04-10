package webSocketMessages.userCommands;

public class ResignGameCommand extends UserGameCommand {

    public ResignGameCommand(String authToken) {
        super(CommandType.RESIGN, authToken);
    }
}

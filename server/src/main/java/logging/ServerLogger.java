package logging;

import webSocketMessages.serverMessages.ServerErrorMessage;
import webSocketMessages.serverMessages.ServerLoadGameMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.serverMessages.ServerNotification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class ServerLogger {
    public static Logger logger = Logger.getLogger("serverLogger");
    static {
        try {
            FileHandler fh = new FileHandler("C:\\Users\\srick\\IntellijProjects\\chess\\server\\src\\main\\java\\logging\\server.log", false);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logUserCommand(UserGameCommand command) {
        Level logLevel = Level.INFO;
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> {
                JoinPlayerCommand jpCommand = (JoinPlayerCommand) command;
                logger.log(logLevel, "UserCommand received: " + jpCommand);
            }
            case JOIN_OBSERVER -> {
                JoinObserverCommand joCommand = (JoinObserverCommand) command;
                logger.log(logLevel, "UserCommand received: " + joCommand);
            }
            case LEAVE -> {
                LeaveGameCommand lgCommand = (LeaveGameCommand) command;
                logger.log(logLevel, "UserCommand received: " + lgCommand);
            }
            case MAKE_MOVE -> {
                MakeMoveCommand mmCommand = (MakeMoveCommand) command;
                logger.log(logLevel, "UserCommand received: " + mmCommand);
            }
            case RESIGN -> {
                ResignGameCommand rgCommand = (ResignGameCommand) command;
                logger.log(logLevel, "UserCommand received: " + rgCommand);
            }
        }
    }

    public static void logServerMessage(ServerMessage message, String recipient) {
        Level logLevel = Level.INFO;
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> {
                ServerLoadGameMessage lgMessage = (ServerLoadGameMessage) message;
                logger.log(logLevel, "ServerMessage sent to " + recipient + ": " + lgMessage);
            }
            case ERROR -> {
                ServerErrorMessage eMessage = (ServerErrorMessage) message;
                logger.log(logLevel, "ServerMessage sent to " + recipient + ": " + eMessage);
            }
            case NOTIFICATION -> {
                ServerNotification nMessage = (ServerNotification) message;
                logger.log(logLevel, "ServerMessage sent to " + recipient + ": " + nMessage);
            }
        }
    }
}

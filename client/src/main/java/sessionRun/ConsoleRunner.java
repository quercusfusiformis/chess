package sessionRun;

import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import requestRecords.*;
import responseRecords.*;
import serverCommunication.ServerFacade;
import serverCommunication.CommunicationException;

public class ConsoleRunner {
    private final ServerFacade server = new ServerFacade(3676, "localhost:");
    private boolean running = true;
    private boolean userAuthorized = false;
    private String userAuthToken;
    private boolean runningWSSession = false;
    private boolean printLoggedOutMenu = true;
    private boolean printLoggedInMenu = false;
    private boolean printWSSessionMenu = false;
    private final HashMap<Integer, Integer> gameListIDMap = new HashMap<>();

    public void run() {
        while (this.running) {
            if (this.printLoggedOutMenu) {
                printLoggedOutMenu();
                this.printLoggedOutMenu = false;
            } else if (this.printWSSessionMenu) {
                printWSSessionMenu();
                this.printWSSessionMenu = false;
            } else if (this.printLoggedInMenu) {
                printLoggedInMenu();
                this.printLoggedInMenu = false;
            }

            ArrayList<String> userInput = new ArrayList<>();
            try {
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try { userInput = (ArrayList<String>) promptUserForInput();
            } catch (IOException ex) { System.out.print("An error occurred. Please try again"); }

            if (!this.runningWSSession) {
                parseCommands(userInput);
            } else {
                parseWSCommands(userInput);
            }
        }
    }

    private void printLoggedOutMenu() {
        String printString = String.format("""
                
                %s OPTIONS:
                    register <username> <password> <email> - creates an account and logs user in
                    login <username> <password> - logs user in
                    quit - quits program
                    help - lists available commands
                
                """, getUserAuthStatusAsString(this.userAuthorized));
        System.out.print(printString);
    }

    private void printLoggedInMenu() {
        String printString = String.format("""
            
            %s OPTIONS:
                list - lists available games
                create <name> - creates a game
                join <ID> <WHITE|BLACK> - joins a specified game as the chosen color
                observe <ID> - joins a specified game as an observer
                logout - logs you out
                quit - quits program
                help - lists available commands
            
            """, getUserAuthStatusAsString(this.userAuthorized));
        System.out.print(printString);
    }

    private void printWSSessionMenu() {
        String printString = String.format("""
                
                %s OPTIONS:
                    showboard - show board
                    showmoves <COORD> - show available moves for a piece at a position on the board
                    move <COORD> <COORD> - move a piece
                    leave - leave game
                    resign - forfeit game
                    help - lists available commands
                
                """, getUserAuthStatusAsString(this.userAuthorized));
        System.out.print(printString);
    }

    private Collection<String> promptUserForInput() throws IOException {
        System.out.print(getPrompt());
        return getUserInput();
    }

    private String getPrompt() { return String.format("[%s] >> ", getUserAuthStatusAsString(this.userAuthorized)); }

    private String getUserAuthStatusAsString(boolean userAuthorized) {
        if (!userAuthorized) {
            return "LOGGED_OUT";
        } else if (this.runningWSSession) {
            return "LOGGED_IN--GAME";
        } else {
            return "LOGGED_IN";
        }
    }

    private static Collection<String> getUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return new ArrayList<>(List.of(reader.readLine().split(" ")));
    }

    private void parseCommands(ArrayList<String> userInput) {
        String unrecognizedCommandString = "Unrecognized command. Type help to list available commands.\n";
        if (!userInput.isEmpty()) {
            ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("register", "login", "list", "create",
                    "join", "observe", "logout", "quit", "help"));
            String firstCommand = userInput.getFirst().toLowerCase();
            if (firstCommand.isEmpty()) { return; }
            else if (!validCommands.contains(firstCommand)) {
                System.out.print(unrecognizedCommandString);
                return;
            }
            ArrayList<String> userArgs;
            userArgs = userInput;
            userArgs.removeFirst();
            ArrayList<String> validate;
            // Unauthorized and authorized options
            try {
                boolean invalidInput = false;
                if (!userAuthorized) {
                    switch (firstCommand) {
                        case "register" -> {
                            validate = new ArrayList<>(Arrays.asList("str", "str", "str"));
                            if (isValidInput(userArgs, validate)) { register(userArgs);
                            } else { invalidInput = true; }
                        }
                        case "login" -> {
                            validate = new ArrayList<>(Arrays.asList("str", "str"));
                            if (isValidInput(userArgs, validate)) { login(userArgs);
                            } else { invalidInput = true; }
                        }
                    }
                } else {
                    switch (firstCommand) {
                        case "list" -> {
                            if (userArgs.isEmpty()) { list();
                            } else { invalidInput = true; }
                        }
                        case "create" -> {
                            validate = new ArrayList<>(List.of("str"));
                            if (isValidInput(userArgs, validate)) { create(userArgs);
                            } else { invalidInput = true; }
                        }
                        case "join" -> {
                            validate = new ArrayList<>(Arrays.asList("int", "str"));
                            if (isValidInput(userArgs, validate)) { join(userArgs);
                            } else { invalidInput = true; }
                        }
                        case "observe" -> {
                            validate = new ArrayList<>(List.of("int"));
                            if (isValidInput(userArgs, validate)) { observe(userArgs);
                            } else { invalidInput = true; }
                        }
                        case "logout" -> {
                            if (userArgs.isEmpty()) { logout();
                            } else { invalidInput = true; }
                        }
                    }
                }
                // Always-available options
                switch (firstCommand) {
                    case "quit" -> {
                        if (userArgs.isEmpty()) { quit();
                        } else { invalidInput = true; }
                    }
                    case "help" -> {
                        if (userArgs.isEmpty()) { help();
                        } else { invalidInput = true; }
                    }
                }
                if (invalidInput) {
                    System.out.print("Invalid command input. Type help and format your command according to the menu.\n");
                }
            } catch (CommunicationException | InterruptedException ex) {
                System.out.print("An error occurred while communicating with the server: " + ex.getMessage() + "\n");
            }
        } else { System.out.print(unrecognizedCommandString); }
    }

    private void parseWSCommands(ArrayList<String> userInput) {
        String unrecognizedCommandString = "Unrecognized command. Type help to list available commands.\n";
        if (!userInput.isEmpty()) {
            ArrayList<String> validCommands = new ArrayList<>(Arrays.asList("showboard", "showmoves", "move", "leave",
                    "resign", "help"));
            String firstCommand = userInput.getFirst().toLowerCase();
            if (firstCommand.isEmpty()) { return; }
            else if (!validCommands.contains(firstCommand)) {
                System.out.print(unrecognizedCommandString);
                return;
            }
            ArrayList<String> userArgs;
            userArgs = userInput;
            userArgs.removeFirst();
            ArrayList<String> validate;
            try {
                boolean invalidInput = false;
                switch (firstCommand) {
                    case "showboard" -> {
                        if (userArgs.isEmpty()) {
                            redrawBoard();
                        } else {
                            invalidInput = true;
                        }
                    }
                    case "showmoves" -> {
                        validate = new ArrayList<>(List.of("str"));
                        if (isValidInput(userArgs, validate)) {
                            highlightLegalMoves(userArgs);
                        } else {
                            invalidInput = true;
                        }
                    }
                    case "move" -> {
                        validate = new ArrayList<>(Arrays.asList("str", "str"));
                        if (isValidInput(userArgs, validate)) {
                            makeMove(userArgs);
                        } else {
                            invalidInput = true;
                        }
                    }
                    case "resign" -> {
                        if (userArgs.isEmpty()) {
                            resign();
                        } else {
                            invalidInput = true;
                        }
                    }
                    case "leave" -> {
                        if (userArgs.isEmpty()) {
                            leaveGame();
                        } else {
                            invalidInput = true;
                        }
                    }
                    case "help" -> {
                        if (userArgs.isEmpty()) {
                            help();
                        } else {
                            invalidInput = true;
                        }
                    }
                }
                if (invalidInput) {
                    System.out.print("Invalid command input. Type help and format your command according to the menu.\n");
                }
            } catch (CommunicationException ex) {
                System.out.print("An error occurred while processing your request: " + ex.getMessage() + "\n");
            }
        } else { System.out.print(unrecognizedCommandString); }
    }

    private boolean isValidInput(ArrayList<String> userInput, ArrayList<String> validTypes) {
        if (userInput.size() != validTypes.size()) { return false; }
        boolean isValidInput = true;
        for (int i = 0; i < validTypes.size(); i++) {
            String input = userInput.get(i);
            String type = validTypes.get(i);
            switch(type) {
                case "str" -> isValidInput = isValidInput && !isNumeric(input) && !input.isEmpty();
                case "int" -> isValidInput = isValidInput && isNumeric(input);
            }
        }
        return isValidInput;
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.valueOf(str);
            return true;
        } catch (NumberFormatException ex) { return false; }
    }

    private void register(ArrayList<String> userArgs) throws CommunicationException {
        RegisterResponse rResponse = server.register(
                new RegisterRequest(userArgs.get(0), userArgs.get(1), userArgs.get(2)));
        System.out.print("Registered user " + rResponse.username() + ".\n");
        login(new ArrayList<>(Arrays.asList(userArgs.get(0), userArgs.get(1))));
    }

    private void login(ArrayList<String> userArgs) throws CommunicationException {
        LoginResponse lResponse = server.login(
                new LoginRequest(userArgs.get(0), userArgs.get(1)));
        setAuthorization(lResponse.authToken());
        System.out.print("Logged in user " + lResponse.username() + ".\n");
    }

    private void list() throws CommunicationException {
        ListGamesResponse response = server.listGames(this.userAuthToken);
        this.gameListIDMap.clear();
        System.out.print("\nCURRENT GAMES:\n");
        System.out.print("    Game No. | Game Name | White Player Username | Black Player Username\n");
        for (int i = 0; i < response.games().size(); i++) {
            this.gameListIDMap.put((i+1), response.games().get(i).gameID());
            System.out.print("    " + (i+1) + " | " +
                    response.games().get(i).gameName() + " | " +
                    response.games().get(i).whiteUsername() + " | " +
                    response.games().get(i).blackUsername() + "\n");
        }
        System.out.print("\n");
    }

    private void create(ArrayList<String> userArgs) throws CommunicationException {
        server.createGame(new CreateGameRequest(userArgs.getFirst()), this.userAuthToken);
        System.out.print("New game \"" + userArgs.getFirst() + "\"\n");
    }

    private void join(ArrayList<String> userArgs) throws CommunicationException, InterruptedException {
        ChessGame.TeamColor color;
        if (userArgs.get(1) != null) { color = stringToTeamColor(userArgs.get(1));
        } else { color = null; }
        Integer requestedID = Integer.parseInt(userArgs.get(0));
        if (!gameListIDMap.containsKey(requestedID)) { throw new CommunicationException("Invalid game ID requested. List games and try again.\n"); }
        int gameID = this.gameListIDMap.get(requestedID);
        server.joinGame(new JoinGameRequest(color, gameID), this.userAuthToken);
        TimeUnit.MILLISECONDS.sleep(250);
        redrawBoard();
        this.runningWSSession = true;
        this.printWSSessionMenu = true;
    }

    private ChessGame.TeamColor stringToTeamColor(String toColor) throws CommunicationException {
        if (toColor.equalsIgnoreCase("WHITE")) {
            return ChessGame.TeamColor.WHITE;
        } else if (toColor.equalsIgnoreCase("BLACK")) {
            return ChessGame.TeamColor.BLACK;
        } else {
            throw new CommunicationException("Invalid color");
        }
    }

    private void observe(ArrayList<String> userArgs) throws CommunicationException, InterruptedException {
        join(new ArrayList<>(Arrays.asList(userArgs.getFirst(), null)));
    }

    private void logout() throws CommunicationException {
        server.logout(this.userAuthToken);
        setAuthorization(null);
        System.out.print("Logged out.\n");
    }

    private void quit() throws CommunicationException {
        if (this.userAuthorized) { logout(); }
        System.out.print("Quitting...\n\n");
        this.running = false;
    }

    private void help() {
        if (!this.userAuthorized) {
            printLoggedOutMenu();
        } else if (this.runningWSSession) {
            printWSSessionMenu();
        } else { printLoggedInMenu(); }
    }

    private void redrawBoard() throws CommunicationException {
        System.out.println(server.redrawBoard());
    }

    private void leaveGame() throws CommunicationException {
        server.leaveGame(this.userAuthToken);
        this.runningWSSession = false;
        this.printLoggedInMenu = true;
        System.out.println("Left game.\n");
    }

    private void makeMove(ArrayList<String> userArgs) throws CommunicationException {
        String movePosStr = userArgs.getFirst();
        String endPosStr = userArgs.getLast();
        if (isValidCoord(movePosStr) && isValidCoord(endPosStr)) {
            ChessPosition movePos = coordToPosition(movePosStr);
            ChessPosition endPos = coordToPosition(endPosStr);
            ChessMove move = new ChessMove(movePos, endPos, null);
            server.makeMove(move, this.userAuthToken);
        }
    }

    private boolean isValidCoord(String coord) {
        boolean isValid = false;
        if (coord.length() == 2) {
            String letter = String.valueOf(coord.charAt(0)).toLowerCase();
            String number = String.valueOf(coord.charAt(1));
            ArrayList<String> validate = new ArrayList<>(List.of("str", "int"));
            if (isValidInput(new ArrayList<>(List.of(letter, number)), validate)) {
                ArrayList<String> letterArray = new ArrayList<>(List.of("a","b","c","d","e","f","g","h"));
                ArrayList<String> numberArray = new ArrayList<>(List.of("1","2","3","4","5","6","7","8"));
                isValid = letterArray.contains(letter) && numberArray.contains((number));
            }
        }
        return isValid;
    }

    private ChessPosition coordToPosition(String coord) throws CommunicationException {
        if (isValidCoord(coord)) {
            String letter = String.valueOf(coord.charAt(0)).toLowerCase();
            String number = String.valueOf(coord.charAt(1));
            int letterVal = getLetterVal(letter);
            int numberVal = Integer.parseInt(number);
            return new ChessPosition(numberVal, letterVal);
        } else {
            throw new CommunicationException("Incorrect coordinate formatting (a5, e6, etc)");
        }
    }

    private static int getLetterVal(String letter) throws CommunicationException {
        int letterVal;
        switch (letter) {
            case "a" -> letterVal = 1;
            case "b" -> letterVal = 2;
            case "c" -> letterVal = 3;
            case "d" -> letterVal = 4;
            case "e" -> letterVal = 5;
            case "f" -> letterVal = 6;
            case "g" -> letterVal = 7;
            case "h" -> letterVal = 8;
            default -> throw new CommunicationException("Incorrect coordinate formatting (a5, e6, etc)");
        }
        return letterVal;
    }

    private void resign() throws CommunicationException {
        server.resign(this.userAuthToken);
    }

    private void highlightLegalMoves(ArrayList<String> userArgs) throws CommunicationException {
        String posString = userArgs.getFirst();
        if (isValidCoord(posString)) {
            ChessPosition pos = coordToPosition(posString);
            System.out.println(server.highlightLegalMoves(pos));
        } else {
            throw new CommunicationException("Incorrect coordinate formatting (a5, e6, etc)");
        }
    }

    private void setAuthorization(String authToken) {
        this.userAuthToken = authToken;
        this.userAuthorized = (authToken != null);
        if (this.userAuthorized) { this.printLoggedInMenu = true;
        } else { this.printLoggedOutMenu = true; }
    }
}

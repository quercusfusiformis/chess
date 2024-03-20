package sessionRun;

import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import requestRecords.*;
import responseRecords.*;
import serverCommunication.ServerFacade;
import serverCommunication.CommunicationException;

public class SessionRunner {
    private final ServerFacade server = new ServerFacade(3676);
    private boolean userAuthorized = false;
    private String userAuthToken;
    private boolean running = true;
    private boolean printLoggedOutMenu = true;
    private boolean printLoggedInMenu = false;

    public void run() {
        while (this.running) {
            if (this.printLoggedOutMenu) {
                printLoggedOutMenu();
                this.printLoggedOutMenu = false;
            } else if (this.printLoggedInMenu) {
                printLoggedInMenu();
                this.printLoggedInMenu = false;
            }

            ArrayList<String> userInput = new ArrayList<>();
            try { userInput = (ArrayList<String>) promptUserForInput();
            } catch (IOException ex) { System.out.print("An error occurred. Please try again"); }

            parseCommands(userInput);
        }
    }

    private void printLoggedOutMenu() {
        String printString = String.format("""
                
                %s OPTIONS:
                    register <username> <password> <email> - creates an account and logs you in
                    login <username> <password> - logs you in
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
                join <ID> <WHITE|BLACK> - joins a specificed game as the chosen color
                observe <ID> - joins a specified game as an observer
                logout - logs you out
                quit - quits program
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
        if (userAuthorized) { return "LOGGED_IN"; }
        else { return "LOGGED_OUT"; }
    }

    private static Collection<String> getUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return new ArrayList<>(List.of(reader.readLine().split(" ")));
    }

    private void parseCommands(ArrayList<String> userInput) {
        String unrecognizedCommandString = "Unrecognized command. Type help to list available commands.\n";
        if (!userInput.isEmpty()) {
            String firstCommand = userInput.getFirst().toLowerCase();
            if (firstCommand.isEmpty()) { return; }
            ArrayList<String> userArgs;
            userArgs = userInput;
            userArgs.removeFirst();
            ArrayList<String> validate;
            // Unauthorized and authorized options
            try {
                String bull = null;
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
                        case "join" -> bull = null;
                        case "observe" -> bull = null;
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
            } catch (CommunicationException ex) {
                System.out.print("An error occurred while communicating with the server: " + ex.getMessage() + "\n");
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
        System.out.print("\nCURRENT GAMES:\n");
        System.out.print("    Game Name | Game ID | White Player Username | Black Player Username\n");
        for(ListGameInfo gameInfo: response.games()) {
            System.out.print("    " + gameInfo.gameName() + " | " +
                    gameInfo.gameID() + " | " +
                    gameInfo.whiteUsername() + " | " +
                    gameInfo.blackUsername() + "\n");
        }
        System.out.print("\n");
    }

    private void create(ArrayList<String> userArgs) throws CommunicationException {
        CreateGameResponse response = server.createGame(new CreateGameRequest(userArgs.getFirst()), this.userAuthToken);
        System.out.print("New game \"" + userArgs.getFirst() + "\" created with ID:" + response.gameID() + "\n");
    }

    private void join(ArrayList<String> userArgs) throws CommunicationException {}

    private void observe(ArrayList<String> userArgs) throws CommunicationException {}

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
        if (!this.userAuthorized) { printLoggedOutMenu();
        } else { printLoggedInMenu(); }
    }

    private void setAuthorization(String authToken) {
        this.userAuthToken = authToken;
        this.userAuthorized = (authToken != null);
        if (this.userAuthorized) { this.printLoggedInMenu = true;
        } else { this.printLoggedOutMenu = true; }
    }
}

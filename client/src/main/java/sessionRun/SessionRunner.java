package sessionRun;

import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class SessionRunner {
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
            } catch (IOException ex) { System.out.print("An error occured. Please try again"); }

            parseCommands(userInput);
        }
    }

    private static void printLoggedOutMenu() {
        String printString = """
                \nOPTIONS:
                    register <username> <password> <email> - creates an account and logs you in
                    login <username> <password> - logs you in
                    quit - quits program
                    help - lists available commands""";
        System.out.print(printString);
    }

    private static void printLoggedInMenu() {
        String printString = """
                \nOPTIONS:
                    list - lists available games
                    create <name> - creates a game
                    join <ID> <WHITE|BLACK> - joins a specificed game as the chosen color
                    observe <ID> - joins a specified game as an observer
                    logout - logs you out
                    quit - quits program
                    help - lists available commands""";
        System.out.print(printString);
    }

    private Collection<String> promptUserForInput() throws IOException {
        System.out.print("\n\n" + getPrompt());
        return getUserInput();
    }

    private String getPrompt() { return String.format("[%s] >> ", getUserAuthStatusAsString(this.userAuthorized)); }

    private String getUserAuthStatusAsString(boolean userAuthorized) {
        if (userAuthorized) { return "LOGGED_IN"; }
        else { return "LOGGED_OUT"; }
    }

    private static Collection<String> getUserInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return new ArrayList<>(List.of(reader.readLine().split(" "))) {
        };
    }

    private void parseCommands(ArrayList<String> userInput) {
        if (!userInput.isEmpty()) {
            String firstCommand = userInput.getFirst().toLowerCase();
            // Unauthorized and authorized options
            if (!userAuthorized) {

            } else {

            }
            // Always-available options
            switch (firstCommand) {

            }
        } else { System.out.print("\nUnrecognized command. Type help to view available commands."); }
    }
}

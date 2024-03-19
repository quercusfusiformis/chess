package sessionRun;

import java.util.*;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class SessionRunner {
    private boolean userAuthorized = false;

    public void run() {
        boolean running = true;
        while (running) {
            ArrayList<String> userInput = new ArrayList<>();
            try { userInput = (ArrayList<String>) promptUserForInput();
            } catch (IOException ex) { System.out.print("An error occured. Please try again"); }

            if (userInput.size() == 1 && userInput.getFirst().equalsIgnoreCase("quit")) { running = false; }
            else {
                System.out.print("You entered words, huh:" + userInput);
            }
        }
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
}

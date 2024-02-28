package dataAccess;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import model.*;

public class DatabaseManager {
    private static int nextGameID = 1000;
    private static final HashSet<AuthData> authData = new HashSet<>();

    private static final HashSet<GameData> gameData = new HashSet<>();

    private static final HashSet<UserData> userData = new HashSet<>();

    public int getNewGameID() {
        nextGameID = nextGameID + 1;
        return nextGameID - 1;
    }

    public Collection<GameData> getAllGames() {
        return new ArrayList<>(gameData);
    }

    public void clearAuths() {
        authData.clear();
    }

    public void clearGames() {
        gameData.clear();
        nextGameID = 1000;
    }

    public void clearUsers() {
        userData.clear();
    }

    public void addAuth(String authToken, String username) {
        authData.add(new AuthData(authToken, username));
    }

    public void addGame(int gameID, String whiteUsername, String blackUsername, String gameName, String game) {
        gameData.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
    }

    public void addUser(String username, String password, String email) {
        userData.add(new UserData(username, password, email));
    }

    public AuthData getAuth(String field, AuthData search) {
        switch (field) {
            case "authToken" -> {
                for (AuthData curr : authData) {
                    if (search.authToken().equals(curr.authToken())) {
                        return curr;
                    }
                }
            }
            case "username" -> {
                for (AuthData curr : authData) {
                    if (search.username().equals(curr.username())) {
                        return curr;
                    }
                }
            }
            case "all" -> {
                for (AuthData curr : authData) {
                    if (search == curr) {
                        return curr;
                    }
                }
            }
            default -> throw new IllegalArgumentException("invalid search field");
        }
        return null;
    }

    public GameData getGame(String field, GameData search) {
        switch (field) {
            case "gameID" -> {
                for (GameData curr : gameData) {
                    if (search.gameID() == curr.gameID()) {
                        return curr;
                    }
                }
            }
            case "whiteUsername" -> {
                for (GameData curr : gameData) {
                    if (search.whiteUsername().equals(curr.whiteUsername())) {
                        return curr;
                    }
                }
            }
            case "blackUsername" -> {
                for (GameData curr : gameData) {
                    if (search.blackUsername().equals(curr.blackUsername())) {
                        return curr;
                    }
                }
            }
            case "gameName" -> {
                for (GameData curr : gameData) {
                    if (search.gameName().equals(curr.gameName())) {
                        return curr;
                    }
                }
            }
            case "game" -> {
                for (GameData curr : gameData) {
                    if (search.game().equals(curr.game())) {
                        return curr;
                    }
                }
            }
            case "all" -> {
                for (GameData curr : gameData) {
                    if (search == curr) {
                        return curr;
                    }
                }
            }
            default -> throw new IllegalArgumentException("invalid search field");
        }
        return null;
    }

    public UserData getUser(String field, UserData search) {
        switch (field) {
            case "username" -> {
                for (UserData curr : userData) {
                    if (search.username().equals(curr.username())) {
                        return curr;
                    }
                }
            }
            case "password" -> {
                for (UserData curr : userData) {
                    if (search.password().equals(curr.password())) {
                        return curr;
                    }
                }
            }
            case "email" -> {
                for (UserData curr : userData) {
                    if (search.email().equals(curr.email())) {
                        return curr;
                    }
                }
            }
            case "all" -> {
                for (UserData curr : userData) {
                    if (search == curr) {
                        return curr;
                    }
                }
            }
            default -> throw new IllegalArgumentException("invalid search field");
        }
        return null;
    }

    public void delAuth(String field, AuthData search) {
        AuthData toRemove = getAuth(field, search);
        while(!(toRemove == null)) {
            authData.remove(toRemove);
            toRemove = getAuth(field, search);
        }
    }

    public void delGame(String field, GameData search) {
        GameData toRemove = getGame(field, search);
        while(!(toRemove == null)) {
            gameData.remove(toRemove);
            toRemove = getGame(field, search);
        }
    }

    public void delUser(String field, UserData search) {
        UserData toRemove = getUser(field, search);
        while(!(toRemove == null)) {
            userData.remove(toRemove);
            toRemove = getUser(field, search);
        }
    }
}

package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, String game) {
    public boolean isColorAvailable(String requestedColor) {
        if (requestedColor.equals("WHITE")) {
            return (this.whiteUsername == null);
        }
        else if (requestedColor.equals("BLACK")) {
            return (this.blackUsername == null);
        }
        else { throw new IllegalArgumentException("Error: invalid color parameter"); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        ChessGame game1 = new Gson().fromJson(game, ChessGame.class);
        ChessGame game2 = new Gson().fromJson(gameData.game, ChessGame.class);
        boolean gameEquals = game1.equals(game2);
        boolean otherVarsEqual = (gameID == gameData.gameID && Objects.equals(whiteUsername, gameData.whiteUsername)
                && Objects.equals(blackUsername, gameData.blackUsername) && Objects.equals(gameName, gameData.gameName));
        return (gameEquals && otherVarsEqual);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }
}

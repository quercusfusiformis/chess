package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, String game) {
    boolean isColorAvailable(ChessGame.TeamColor requestedColor) {
        return false;
    }
}

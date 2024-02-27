package model;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, String game) {
    public boolean isColorAvailable(String requestedColor) {
        if (requestedColor.equals("WHITE")) {
            return (this.whiteUsername == null);
        }
        else if (requestedColor.equals("BLACK")) {
            return (this.blackUsername == null);
        }
        else { throw new IllegalArgumentException("invalid color parameter"); }
    }
}

package responseRecords;

public record GetGameResponse(int gameID, String whiteUsername, String blackUsername, String gameName, String game) {
}

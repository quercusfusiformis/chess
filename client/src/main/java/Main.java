import chess.*;

import sessionRun.SessionRunner;

public class Main {
    private static final SessionRunner runner = new SessionRunner();

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        runner.run();
    }
}
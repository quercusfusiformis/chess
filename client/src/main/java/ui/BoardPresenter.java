package ui;

import static ui.EscapeSequences.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import chess.ChessGame;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPresenter {
    public static final ChessBoard defaultBoard = new ChessBoard();

    public static void printBoard(ChessBoard board, ChessGame.TeamColor callerColor) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        for(int i = 8; i >= 1; i--) {
            if (i % 2 == 0) {
                setSpaceWhite(out);
            } else { setSpaceBlack(out); }
            for (int j = 1; j <= 8; j++) {
                ChessPiece currPiece = board.getPiece(new ChessPosition(i, j));

                if (currPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    printWhitePiece(out, currPiece.getPieceType());
                } else { printBlackPiece(out, currPiece.getPieceType()); }
            }
        }
    }

    private static void setSpaceWhite(PrintStream out) { out.print(SET_BG_COLOR_WHITE); }

    private static void setSpaceBlack(PrintStream out) { out.print(SET_BG_COLOR_BLACK); }

    private static void setSpaceBrown(PrintStream out) { out.print(SET_BG_COLOR_DARK_GREY); }

    private static void printWhitePiece(PrintStream out, ChessPiece.PieceType piece) {
        out.print(SET_TEXT_COLOR_WHITE);
        printPiece(out, piece);
    }

    private static void printBlackPiece(PrintStream out, ChessPiece.PieceType piece) {
        out.print(SET_TEXT_COLOR_BLACK);
        printPiece(out, piece);
    }

    private static void printPiece(PrintStream out, ChessPiece.PieceType piece) {
        String pieceString;
        switch (piece) {
            case KING -> pieceString = BLACK_KING;
            case QUEEN -> pieceString = BLACK_QUEEN;
            case BISHOP -> pieceString = BLACK_BISHOP;
            case ROOK -> pieceString = BLACK_ROOK;
            case KNIGHT -> pieceString = BLACK_KNIGHT;
            case PAWN -> pieceString = BLACK_PAWN;
            default -> pieceString = EMPTY;
        };
        out.print(pieceString);
    }
}

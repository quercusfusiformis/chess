package ui;

import static ui.EscapeSequences.*;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import chess.ChessGame;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPresenter {
    public static ChessBoard defaultBoard = new ChessBoard();
    static {
        defaultBoard.resetBoard();
    }

    public static void printBoard(ChessBoard board, ChessGame.TeamColor callerColor) {
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        int [] rowOrder;
        int [] colOrder;
        if (callerColor == ChessGame.TeamColor.WHITE) {
            rowOrder = new int [] {8,7,6,5,4,3,2,1};
            colOrder = new int [] {1,2,3,4,5,6,7,8};
        } else {
            rowOrder = new int [] {1,2,3,4,5,6,7,8};
            colOrder = new int [] {8,7,6,5,4,3,2,1};
        }

        printHeader(out, callerColor);
        ChessGame.TeamColor currSpaceColor;
        for(int i: rowOrder) {
            out.print(SET_BG_COLOR_BROWN);
            out.print(SET_TEXT_COLOR_BLACK);
            printCharWithSpacing(out, String.valueOf(i));
            if (i % 2 == 1) { currSpaceColor = ChessGame.TeamColor.BLACK; }
            else { currSpaceColor = ChessGame.TeamColor.WHITE; }
            for (int j: colOrder) {
                if (currSpaceColor == ChessGame.TeamColor.WHITE) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    currSpaceColor = ChessGame.TeamColor.BLACK;
                } else {
                    out.print(SET_BG_COLOR_DARK_GREY);
                    currSpaceColor = ChessGame.TeamColor.WHITE;
                }
                ChessPiece currPiece = board.getPiece(new ChessPosition(i, j));
                if (!(currPiece == null)) { printPiece(out, currPiece.getPieceType(), currPiece.getTeamColor()); }
                else { out.print(CHESSSPACE); }
            }
            out.print(SET_BG_COLOR_BROWN);
            out.print(CHESSSPACE);
            out.print(RESET_BG_COLOR);
            out.print(System.lineSeparator());
        }
        out.print(SET_BG_COLOR_BROWN);
        for (int i = 0; i <= 9; i++) { out.print(CHESSSPACE); }
        out.print(RESET_BG_COLOR);
        out.print(System.lineSeparator());
    }

    private static void printHeader(PrintStream out, ChessGame.TeamColor callerColor) {
        String [] headerOrder;
        out.print(SET_BG_COLOR_BROWN);
        out.print(CHESSSPACE);
        out.print(SET_TEXT_COLOR_BLACK);
        if (callerColor == ChessGame.TeamColor.WHITE) {
            headerOrder = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        } else { headerOrder = new String [] {"h", "g", "f", "e", "d", "c", "b", "a"}; }
        for (String headerLetter: headerOrder) {
            printCharWithSpacing(out, headerLetter);
        }
        out.print(CHESSSPACE);
        out.print(RESET_BG_COLOR);
        out.print(System.lineSeparator());
    }

    private static void printCharWithSpacing(PrintStream out, String aChar) {
        out.print(EMSIXTH_SPACE);
        out.print(aChar);
        out.print(EMSIXTH_SPACE);
    }

    private static void printPiece(PrintStream out, ChessPiece.PieceType piece, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) { out.print(SET_TEXT_COLOR_WHITE); }
        else { out.print(SET_TEXT_COLOR_BLACK); }
        printPieceIcon(out, piece);
    }

    private static void printPieceIcon(PrintStream out, ChessPiece.PieceType piece) {
        String pieceString;
        switch (piece) {
            case KING -> pieceString = BLACK_KING;
            case QUEEN -> pieceString = BLACK_QUEEN;
            case BISHOP -> pieceString = BLACK_BISHOP;
            case ROOK -> pieceString = BLACK_ROOK;
            case KNIGHT -> pieceString = BLACK_KNIGHT;
            case PAWN -> pieceString = BLACK_PAWN;
            default -> pieceString = CHESSSPACE;
        }
        out.print(pieceString);
    }
}

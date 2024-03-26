package ui;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public final class BoardToStringUtil {
    private BoardToStringUtil() {}

    public static ChessBoard getADefaultBoard() {
        ChessBoard defaultBoard = new ChessBoard();
        defaultBoard.resetBoard();
        return defaultBoard;
    }

    private static final String originalTextColor = SET_TEXT_COLOR_WHITE;
    private static final String originalBackgroundColor = SET_BG_COLOR_BLACK;
    private static final String whitePieceColor = SET_TEXT_COLOR_WHITE;
    private static final String blackPieceColor = SET_TEXT_COLOR_BLACK;
    private static final String indexColor = SET_TEXT_COLOR_BLACK;
    private static final String boardEdgeColor = SET_BG_COLOR_BROWN;
    private static final String boardSpaceWhiteColor = SET_BG_COLOR_LIGHT_GREY;
    private static final String boardSpaceBlackColor = SET_BG_COLOR_DARK_GREY;
    private static final String boardSeparatorColor = RESET_BG_COLOR;

    public static String getBoardAll(ChessBoard board) {
        return getBoard(board, ChessGame.TeamColor.BLACK) +
                getBlankFullRow(boardSeparatorColor) +
                getBoard(board, ChessGame.TeamColor.WHITE);
    }

    public static String getBoard(ChessBoard board, ChessGame.TeamColor callerColor) {
        return getHeader(callerColor) +
                getBoardRows(board, callerColor) +
                getFooter() +
                resetTerminalColors();
    }

    private static String getHeader(ChessGame.TeamColor callerColor) {
        StringBuilder returnStringBuilder = new StringBuilder();
        String [] headerOrder;
        returnStringBuilder.append(boardEdgeColor);
        returnStringBuilder.append(CHESSSPACE);
        returnStringBuilder.append(indexColor);
        if (callerColor == ChessGame.TeamColor.WHITE) {
            headerOrder = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        } else { headerOrder = new String [] {"h", "g", "f", "e", "d", "c", "b", "a"}; }
        for (String headerLetter: headerOrder) {
            returnStringBuilder.append(getCharWithSpacing(headerLetter));
        }
        returnStringBuilder.append(CHESSSPACE);
        returnStringBuilder.append(originalTextColor);
        returnStringBuilder.append(originalBackgroundColor);
        returnStringBuilder.append(System.lineSeparator());
        return returnStringBuilder.toString();
    }

    private static String getBoardRows(ChessBoard board, ChessGame.TeamColor callerColor) {
        StringBuilder returnStringBuilder = new StringBuilder();

        int spaceColorDesignator;
        int [] rowOrder;
        int [] colOrder;
        if (callerColor == ChessGame.TeamColor.WHITE) {
            spaceColorDesignator = 0;
            rowOrder = new int [] {8,7,6,5,4,3,2,1};
            colOrder = new int [] {1,2,3,4,5,6,7,8};
        } else {
            spaceColorDesignator = 1;
            rowOrder = new int [] {1,2,3,4,5,6,7,8};
            colOrder = new int [] {8,7,6,5,4,3,2,1};
        }

        for(int i: rowOrder) {
            returnStringBuilder.append(getBoardLeftSide(i));
            ChessGame.TeamColor nextSpaceColor;
            if (i % 2 == spaceColorDesignator) { nextSpaceColor = ChessGame.TeamColor.WHITE; }
            else { nextSpaceColor = ChessGame.TeamColor.BLACK; }
            for (int j: colOrder) {
                returnStringBuilder.append(getANSIForColor(nextSpaceColor));
                nextSpaceColor = getOppositeTeamColor(nextSpaceColor);
                ChessPiece currPiece = board.getPiece(new ChessPosition(i, j));
                if (!(currPiece == null)) {
                    returnStringBuilder.append(getPiece(currPiece.getPieceType(), currPiece.getTeamColor()));
                } else { returnStringBuilder.append(CHESSSPACE); }
            }
            returnStringBuilder.append(getBoardRightSide());
            returnStringBuilder.append(System.lineSeparator());
        }
        return returnStringBuilder.toString();
    }

    private static String getBoardLeftSide(int index) {
        return boardEdgeColor +
                indexColor +
                getCharWithSpacing(String.valueOf(index));
    }

    private static String getBoardRightSide() {
        return boardEdgeColor +
                CHESSSPACE +
                originalBackgroundColor;
    }

    private static String getANSIForColor(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) { return boardSpaceWhiteColor; }
        else { return boardSpaceBlackColor; }
    }

    private static ChessGame.TeamColor getOppositeTeamColor(ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) { return ChessGame.TeamColor.BLACK; }
        else { return ChessGame.TeamColor.WHITE; }
    }

    private static String getFooter() { return getBlankFullRow(boardEdgeColor); }

    private static String getBlankFullRow(String color) {
        return color +
                CHESSSPACE.repeat(10) +
                originalBackgroundColor +
                System.lineSeparator();
    }

    private static String getCharWithSpacing(String aChar) {
        return EMSIXTH_SPACE +
                aChar +
                EMSIXTH_SPACE;
    }

    private static String getPiece(ChessPiece.PieceType piece, ChessGame.TeamColor color) {
        StringBuilder returnStringBuilder = new StringBuilder();
        if (color == ChessGame.TeamColor.WHITE) { returnStringBuilder.append(whitePieceColor); }
        else { returnStringBuilder.append(blackPieceColor); }
        returnStringBuilder.append(getPieceIcon(piece));
        return returnStringBuilder.toString();
    }

    private static String getPieceIcon(ChessPiece.PieceType piece) {
        StringBuilder returnStringBuilder = new StringBuilder();
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
        returnStringBuilder.append(pieceString);
        return returnStringBuilder.toString();
    }

    private static String resetTerminalColors() { return originalTextColor + originalBackgroundColor; }
}

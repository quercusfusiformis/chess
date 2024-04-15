package boardPrinterTests;

import java.util.Collection;
import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BoardToStringUtil;
import chess.ChessGame;

class BoardToStringUtilTests {
    ChessBoard defaultBoard = BoardToStringUtil.getADefaultBoard();

    @BeforeEach
    void setUp() {System.out.print(System.lineSeparator() + System.lineSeparator()); }

    @Test
    void printBoardDefault() {
        System.out.print(BoardToStringUtil.getBoard(defaultBoard, ChessGame.TeamColor.BLACK));
    }

    @Test
    void printBoardAllDefault() {
        System.out.print(BoardToStringUtil.getBoardBothSides(defaultBoard));
    }

    @Test
    void printBoardHighlighted() {
        ChessPosition testPos = new ChessPosition(2, 2);
        ChessPiece testPiece = defaultBoard.getPiece(testPos);
        Collection<ChessPosition> testMoves = BoardToStringUtil.getMoveResults(testPiece.pieceMoves(defaultBoard, testPos));
        System.out.print(BoardToStringUtil.getHighlightedBoard(defaultBoard, ChessGame.TeamColor.BLACK, testPos, testMoves));
    }
}
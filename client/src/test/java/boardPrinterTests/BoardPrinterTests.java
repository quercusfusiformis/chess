package boardPrinterTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BoardPrinter;
import chess.ChessGame;

class BoardPrinterTests {
    @BeforeEach
    void setUp() {}

    @Test
    void printBoardDefault() {
        BoardPrinter.printBoard(BoardPrinter.defaultBoard, ChessGame.TeamColor.BLACK);
    }

    @Test
    void printBoardAllDefault() {
        BoardPrinter.printBoardAll(BoardPrinter.defaultBoard);
    }
}
package boardPrinterTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BoardToStringUtil;
import chess.ChessGame;

class BoardToStringUtilTests {
    @BeforeEach
    void setUp() {System.out.print(System.lineSeparator() + System.lineSeparator()); }

    @Test
    void printBoardDefault() {
        System.out.print(BoardToStringUtil.getBoard(BoardToStringUtil.getADefaultBoard(), ChessGame.TeamColor.BLACK));
    }

    @Test
    void printBoardAllDefault() {
        System.out.print(BoardToStringUtil.getBoardAll(BoardToStringUtil.getADefaultBoard()));
    }
}
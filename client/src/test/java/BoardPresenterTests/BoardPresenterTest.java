package BoardPresenterTests;

import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.BoardPresenter;

import static org.junit.jupiter.api.Assertions.*;

class BoardPresenterTest {
    @BeforeEach
    void setUp() {}

    @Test
    void printBoardDefault() {
        BoardPresenter.printBoard(BoardPresenter.defaultBoard, ChessGame.TeamColor.WHITE);
    }
}
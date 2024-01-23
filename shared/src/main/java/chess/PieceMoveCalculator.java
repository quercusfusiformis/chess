package chess;

import java.util.Collection;

public abstract class PieceMoveCalculator {
    protected ChessBoard board;
    protected ChessPosition position;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public abstract Collection<ChessMove> calculateMoves();
}

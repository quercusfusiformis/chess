package chess;

import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator {
    KnightMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return null;
    }

}

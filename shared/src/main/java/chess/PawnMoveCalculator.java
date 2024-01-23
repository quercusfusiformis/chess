package chess;

import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator {
    PawnMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return null;
    }
}

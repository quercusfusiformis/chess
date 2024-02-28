package chess.move_calculators;

import chess.*;

import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator {
    public BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return bishopCalculator();
    }
}

package chess.move_calculators;

import chess.*;

import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {
    public RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return rookCalculator();
    }
}

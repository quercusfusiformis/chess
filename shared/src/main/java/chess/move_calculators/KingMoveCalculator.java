package chess.move_calculators;

import chess.*;

import java.util.Collection;

public class KingMoveCalculator extends PieceMoveCalculator {
    public KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        int [][] combos = { {1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1} };
        return kingKnightCalculator(combos);
    }
}

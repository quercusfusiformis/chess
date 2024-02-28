package chess.move_calculators;

import chess.*;

import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator {
    public KnightMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        int [][] combos = { {1,2}, {1, -2}, {-1, 2}, {-1, -2}, {2, 1}, {2, -1}, {-2, 1}, {-2, -1} };
        return kingKnightCalculator(combos);
    }

}

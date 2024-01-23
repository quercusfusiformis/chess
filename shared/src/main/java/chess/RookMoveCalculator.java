package chess;

import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator{
    RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return null;
    }

}

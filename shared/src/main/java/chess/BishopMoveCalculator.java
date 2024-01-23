package chess;

import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator{
    BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return null;
    }

}

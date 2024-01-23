package chess;

import java.util.Collection;

public class KingMoveCalculator extends  PieceMoveCalculator{
    KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return null;
    }

}

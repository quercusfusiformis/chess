package chess;

import java.util.Collection;

public class QueenMoveCalculator extends PieceMoveCalculator{
    QueenMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        return null;
    }

}

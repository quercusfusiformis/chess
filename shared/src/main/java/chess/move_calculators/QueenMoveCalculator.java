package chess.move_calculators;

import chess.*;

import java.util.Collection;

public class QueenMoveCalculator extends PieceMoveCalculator {
    public QueenMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> queenMoves = bishopCalculator();
        queenMoves.addAll(rookCalculator());
        return queenMoves;
    }

}

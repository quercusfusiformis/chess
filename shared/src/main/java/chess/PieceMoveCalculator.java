package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMoveCalculator {
    protected ChessBoard board;
    protected ChessPosition position;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public abstract Collection<ChessMove> calculateMoves();

    protected boolean hasClearStraightPath(ChessPosition first, ChessPosition second) {
        // ChessPositions passed in must be straight across from each other. Undefined behavior if not
        ArrayList<Integer[]> intermediates = first.findStraightPositionsBetween(second);
        boolean isClear = true;
        for(Integer [] intermediate : intermediates) {
            ChessPosition testPos = new ChessPosition(intermediate[0], intermediate[1]);
            boolean occupied = (!(this.board.getPiece(testPos) == null));
            if (occupied) {
                isClear = false;
                break;
            }
        }
        return isClear;
    }

    protected boolean hasClearDiagonalPath(ChessPosition first, ChessPosition second) {
        // ChessPositions passed in must be exactly diagonal from each other. Undefined behavior if not
        ArrayList<Integer[]> intermediates = first.findDiagonalPositionsBetween(second);
        boolean isClear = true;
        for(Integer [] intermediate : intermediates) {
            ChessPosition testPos = new ChessPosition(intermediate[0], intermediate[1]);
            boolean occupied = (!(this.board.getPiece(testPos) == null));
            if (occupied) {
                isClear = false;
                break;
            }
        }
        return isClear;
    }
}

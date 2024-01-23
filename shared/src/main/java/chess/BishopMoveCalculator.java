package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator{
    BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        ChessGame.TeamColor movePieceColor = this.board.getPiece(this.position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        // Adds characteristic bishop moves
        int row = this.position.getRow();
        int col = this.position.getColumn();
        int [][] combos = { {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}, {1, -1}, {2, -2}, {3, -3}, {4, -4},
                {5, -5}, {6, -6}, {7, -7}, {-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}, {-1, -1},
                {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7} };
        for (int[] combo : combos) {
            int newRow = row + combo[0];
            int newCol = col + combo[1];
            ChessPosition tmpPos = new ChessPosition(newRow, newCol);
            if (!tmpPos.outOfBounds()) {
                if(!(super.hasClearDiagonalPath(this.position, tmpPos))) { continue; }
                ChessPiece jumpedPiece = this.board.getPiece((tmpPos));
                if (jumpedPiece == null) { moves.add(new ChessMove(this.position, tmpPos, null)); }
                else if (!(jumpedPiece.getTeamColor() == movePieceColor)) {
                    moves.add(new ChessMove(this.position, tmpPos, null));
                }
            }
        }
        return moves;
    }
}
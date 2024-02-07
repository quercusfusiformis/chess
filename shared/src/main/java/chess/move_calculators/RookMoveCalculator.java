package chess.move_calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {
    public RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        ChessGame.TeamColor movePieceColor = this.board.getPiece(this.position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        // Adds characteristic rook moves
        int row = this.position.getRow();
        int col = this.position.getColumn();
        int [][] combos = { {1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}, {-1, 0}, {-2, 0}, {-3, 0}, {-4, 0},
                {-5, 0}, {-6, 0}, {-7, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}, {0, -1}, {0, -2},
                {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7} };
        for (int[] combo : combos) {
            int newRow = row + combo[0];
            int newCol = col + combo[1];
            ChessPosition tmpPos = new ChessPosition(newRow, newCol);
            if (!tmpPos.outOfBounds()) {
                if(!(super.hasClearStraightPath(this.position, tmpPos))) { continue; }
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

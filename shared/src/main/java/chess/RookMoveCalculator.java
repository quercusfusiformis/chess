package chess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator{
    RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        ChessGame.TeamColor movePieceColor = this.board.getPiece(this.position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        // Add characteristic rook moves
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
                if(!(hasClearStraightPath(this.position, tmpPos))) { continue; }
                ChessPiece jumpedPiece = this.board.getPiece((tmpPos));
                if (jumpedPiece == null) { moves.add(new ChessMove(this.position, tmpPos, null)); }
                else if (!(jumpedPiece.getTeamColor() == movePieceColor)) {
                    moves.add(new ChessMove(this.position, tmpPos, null));
                }
            }
        }
//        ChessPosition test1 = new ChessPosition(4, 1);
//        ChessPosition test2 = new ChessPosition(6, 1);
//        System.out.print(test1.findStraightPositionsBetween(test2));
//        System.out.print("\nhasClearStraightPath: ");
//        System.out.print(hasClearStraightPath(test1, test2));

        return moves;
    }

    private boolean hasClearStraightPath(ChessPosition first, ChessPosition second) {
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

}

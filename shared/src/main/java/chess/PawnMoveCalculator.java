package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator {
    PawnMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        // Define default position (depends on color)
        //     If pawn is in default position and nobody's in front of it (1 or 2 steps), it can move forward 2
        // Pawn can move forward 1 if nobody's in front of it
        // If opposite color pieces are on the forward diagonals from it, it can move to those diagonals to capture
        // If move takes pawn to edge of board, promotion move: move needs to be listed 4 times with all possible pieces
        //     Queen, Rook, Bishop, Knight

        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = this.position.getRow();
        int col = this.position.getColumn();

        // Defines default position for initial movement and opposite side of board for promotion
        ChessPosition defaultPos;
        int oppositeSide;
        ChessGame.TeamColor movePieceColor = this.board.getPiece(this.position).getTeamColor();
        if (movePieceColor == ChessGame.TeamColor.WHITE) {
            defaultPos = new ChessPosition(2, this.position.getColumn());
            oppositeSide = 8;
        }
        else if (movePieceColor == ChessGame.TeamColor.BLACK) {
            defaultPos = new ChessPosition(7, this.position.getColumn());
            oppositeSide = 1;
        }

        // Checks left and right sides for opponents to enable diagonal movement
        ChessPosition forwardleft;
        ChessPosition forwardright;
        if (movePieceColor == ChessGame.TeamColor.WHITE) {
            forwardleft = new ChessPosition(row + 1, col - 1);
            forwardright = new ChessPosition(row + 1, col + 1);
        }
        else if (movePieceColor == ChessGame.TeamColor.BLACK) {
            forwardleft = new ChessPosition(row - 1, col - 1);
            forwardright = new ChessPosition(row - 1, col + 1);

        }
        // Handle all move specs at once, and do a sweep on each one to see if it ends on the opposite side of the board
        return null;
    }
}

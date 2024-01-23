package chess;

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

        // Defines default position
//        ChessPosition defaultPos;
//        ChessGame.TeamColor movePieceColor = this.board.getPiece(this.position).getTeamColor();
//        if (movePieceColor == ChessGame.TeamColor.WHITE) {
//            defaultPos = new ChessPosition(2, this.position.getColumn());
//        }
//        else if (movePieceColor == ChessGame.TeamColor.BLACK) {
//            defaultPos = new ChessPosition(7, this.position.getColumn());
//        }
        return null;
    }
}

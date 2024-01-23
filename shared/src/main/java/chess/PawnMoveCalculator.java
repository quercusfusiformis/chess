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
        else {
            defaultPos = new ChessPosition(7, this.position.getColumn());
            oppositeSide = 1;
        }

        // Check for occupied spaces in front of pawn
        ChessPosition frontPos;
        ChessPosition twoInFrontPos;
        boolean frontOccupied = true;
        boolean twoInFrontAvailable = false;
        if (movePieceColor == ChessGame.TeamColor.WHITE) {
            frontPos = new ChessPosition(row + 1, col);
            twoInFrontPos = new ChessPosition(row + 2, col);
        }
        else {
            frontPos = new ChessPosition(row - 1, col);
            twoInFrontPos = new ChessPosition(row - 2, col);
        }
        ChessPiece frontPiece = this.board.getPiece(frontPos);
        ChessPiece twoInFrontPiece = this.board.getPiece(twoInFrontPos);
        if ((frontPiece == null)) {frontOccupied = false; }
        if ((frontPiece == null) && (twoInFrontPiece == null) && (this.position == defaultPos)) {
            twoInFrontAvailable = true;
        }

        // Checks left and right sides for opponents to enable diagonal movement
        ChessPosition forwardleftPos;
        ChessPosition forwardrightPos;
        boolean forwardleftOccupied = true;
        boolean forwardrightOccupied = true;
        if (movePieceColor == ChessGame.TeamColor.WHITE) {
            forwardleftPos = new ChessPosition(row + 1, col - 1);
            forwardrightPos = new ChessPosition(row + 1, col + 1);
        }
        else {
            forwardleftPos = new ChessPosition(row - 1, col - 1);
            forwardrightPos = new ChessPosition(row - 1, col + 1);
        }
        ChessPiece flPiece = this.board.getPiece(forwardleftPos);
        ChessPiece frPiece = this.board.getPiece(forwardrightPos);
        if (!(flPiece == null) && !(flPiece.getTeamColor() == movePieceColor)) {forwardleftOccupied = false; }
        if (!(frPiece == null) && !(frPiece.getTeamColor() == movePieceColor)) {forwardrightOccupied = false; }

        // Handle all move specs at once, and do a sweep on each one to see if it ends on the opposite side of the board
        ArrayList<ChessPosition> proposedMoves = new ArrayList<>();
        if (!(frontOccupied)) { proposedMoves.add(frontPos); }
        if (twoInFrontAvailable) { proposedMoves.add(twoInFrontPos); }
        if (!(forwardleftOccupied)) { proposedMoves.add(forwardleftPos); }
        if (!(forwardrightOccupied)) { proposedMoves.add(forwardrightPos); }
        for(ChessPosition pMove : proposedMoves) {
            int pRow = pMove.getRow();
            int pCol = pMove.getColumn();
            if (movePieceColor == ChessGame.TeamColor.WHITE && pRow == 8) {
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.ROOK));
            }
            else if (movePieceColor == ChessGame.TeamColor.BLACK && pRow == 1) {
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.BISHOP));
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(this.position, pMove, ChessPiece.PieceType.ROOK));
            }
            else {
                moves.add(new ChessMove(this.position, pMove, null));
            }
        }
        return moves;
    }
}

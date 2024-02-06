package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor opposingTeamColor;
        if (teamColor == TeamColor.WHITE) { opposingTeamColor = TeamColor.BLACK; }
        else { opposingTeamColor = TeamColor.WHITE; }

        boolean inCheck = false;

        ChessPosition kingAtRiskPos = getKingPosition(teamColor);
        ArrayList<ChessMove> possibleOpposingMoves = (ArrayList<ChessMove>) getPossibleTeamMoves(opposingTeamColor);
        for (ChessMove move : possibleOpposingMoves) {
            ChessPosition threatenedPos = move.getEndPosition();
            if (threatenedPos.getRow() == kingAtRiskPos.getRow() &&
                    threatenedPos.getColumn() == kingAtRiskPos.getColumn()) {
                inCheck = true;
                break;
            }
        }

        return inCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private ChessPosition getKingPosition(TeamColor team) {
        ChessPosition kingPosition = null;
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i,j);
                ChessPiece currPiece = this.board.getPiece(currPos);
                if (currPiece.getTeamColor() == team && currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = currPos;
                }
            }
        }
        return kingPosition;
    }

    private Collection<ChessPosition> getTeamPiecePositions(TeamColor team) {
        ArrayList<ChessPosition> teamPiecePositions = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition currPos = new ChessPosition(i,j);
                ChessPiece currPiece = this.board.getPiece(currPos);
                if (currPiece.getTeamColor() == team) { teamPiecePositions.add(currPos); }
            }
        }
        return teamPiecePositions;
    }

    private Collection<ChessMove> getPossiblePieceMoves(Collection<ChessPosition> positions) {
        ArrayList<ChessMove> returnMoves = new ArrayList<>();
        for (ChessPosition piecePos : positions) {
            ChessPiece currPiece = this.board.getPiece(piecePos);
            ArrayList<ChessMove> currMoves = (ArrayList<ChessMove>) currPiece.pieceMoves(this.board, piecePos);
            returnMoves.addAll(currMoves);
        }
        return returnMoves;
    }

    private Collection<ChessMove> getPossibleTeamMoves(TeamColor team) {
        Collection<ChessPosition> teamPositions = getTeamPiecePositions(team);
        return getPossiblePieceMoves(teamPositions);
    }
}

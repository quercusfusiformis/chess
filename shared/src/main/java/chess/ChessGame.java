package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard board;
    TeamColor teamTurn;

    public void initialize() {
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
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece movePiece = this.board.getPiece(startPosition);
        if (movePiece == null) { return null; }
        else {
            HashSet<ChessMove> validMoves = new HashSet<>();
            for (ChessMove possibleMove : movePiece.pieceMoves(this.board, startPosition)) {
                if (isMoveValid(possibleMove)) {
                    validMoves.add(possibleMove);
                }
            }
            return validMoves;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor currPieceTeam = this.board.getPiece(move.getStartPosition()).getTeamColor();
        HashSet<ChessMove> validMoves = (HashSet<ChessMove>) validMoves(move.getStartPosition());
        if (validMoves.contains(move) && currPieceTeam == getTeamTurn()) {
            applyMove(move);
            if (getTeamTurn() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            }
            else if (getTeamTurn() == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            } else {
                throw new InvalidMoveException("Error: game is over");
            }
        }
        else { throw new InvalidMoveException("Error: invalid move"); }
    }

    public void applyMove(ChessMove move) {
        ChessPiece movePiece = this.board.getPiece(move.getStartPosition());
        this.board.addPiece(move.getStartPosition(), null);
        if (move.getPromotionPiece() == null) {
            this.board.addPiece(move.getEndPosition(), movePiece);
        }
        else {
            ChessPiece promotePiece = new ChessPiece(movePiece.getTeamColor(), move.getPromotionPiece());
            this.board.addPiece(move.getEndPosition(), promotePiece);
        }
    }

    public boolean isMoveValid(ChessMove move) {
        boolean valid = false;
        boolean checkValidity = false;
        ChessPosition movePos = move.getStartPosition();
        ChessPiece movePiece = this.board.getPiece(movePos);
        ChessPosition landPos = move.getEndPosition();
        ChessPiece landPiece = this.board.getPiece(landPos);
        if (!(movePiece == null)) {
            if (landPiece == null) { checkValidity = true; }
            else {
                if (!(movePiece.getTeamColor() == landPiece.getTeamColor())) { checkValidity = true; }
            }
        }

        if (checkValidity) {
            ChessBoard oldBoard = this.board.deepcopy();
            applyMove(move);
            valid = !isInCheck(movePiece.getTeamColor());
            this.board = oldBoard;
        }

        return valid;
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
        if (kingAtRiskPos == null) { return false; }
        else {
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
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean inCheck = isInCheck(teamColor);
        HashSet<ChessMove> validMoves = (HashSet<ChessMove>) getValidTeamMoves(teamColor);
        return (inCheck && validMoves.isEmpty());
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean inCheck = isInCheck(teamColor);
        HashSet<ChessMove> validMoves = (HashSet<ChessMove>) getValidTeamMoves(teamColor);
        return (!inCheck && validMoves.isEmpty());
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
                if (!(currPiece == null)) {
                    if (currPiece.getTeamColor() == team && currPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        kingPosition = currPos;
                    }
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
                if (!(currPiece == null)) {
                    if (currPiece.getTeamColor() == team) { teamPiecePositions.add(currPos); }
                }
            }
        }
        return teamPiecePositions;
    }

    private Collection<ChessMove> getPossiblePieceMoves(Collection<ChessPosition> positions) {
        ArrayList<ChessMove> returnMoves = new ArrayList<>();
        for (ChessPosition piecePos : positions) {
            ChessPiece currPiece = this.board.getPiece(piecePos);
            if (!(currPiece == null)) {
                ArrayList<ChessMove> currMoves = (ArrayList<ChessMove>) currPiece.pieceMoves(this.board, piecePos);
                returnMoves.addAll(currMoves);
            }
        }
        return returnMoves;
    }

    private Collection<ChessMove> getPossibleTeamMoves(TeamColor team) {
        Collection<ChessPosition> teamPositions = getTeamPiecePositions(team);
        return getPossiblePieceMoves(teamPositions);
    }

    private Collection<ChessMove> getValidTeamMoves(TeamColor team) {
        Collection<ChessPosition> teamPositions = getTeamPiecePositions(team);
        HashSet<ChessMove> validMoves = new HashSet<>();
        for (ChessPosition position : teamPositions) {
            validMoves.addAll(validMoves(position));
        }
        return validMoves;
    }
}

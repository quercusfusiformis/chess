package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] board = new ChessPiece[8][8];

    public ChessBoard() {}

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        // Uses 1-8 to represent board axes
        this.board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        // Uses 1-8 to represent board axes
        return this.board[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessPiece[][] getBoard() {
        return this.board;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // NOTE: Directly accessing the board was clearer and more succinct than using the addPiece function here
        //     Uses indices 0-7 to represent board axes
        // Sets white pieces
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        this.board[0][0] = new ChessPiece(white, ChessPiece.PieceType.ROOK);
        this.board[0][1] = new ChessPiece(white, ChessPiece.PieceType.KNIGHT);
        this.board[0][2] = new ChessPiece(white, ChessPiece.PieceType.BISHOP);
        this.board[0][3] = new ChessPiece(white, ChessPiece.PieceType.QUEEN);
        this.board[0][4] = new ChessPiece(white, ChessPiece.PieceType.KING);
        this.board[0][5] = new ChessPiece(white, ChessPiece.PieceType.BISHOP);
        this.board[0][6] = new ChessPiece(white, ChessPiece.PieceType.KNIGHT);
        this.board[0][7] = new ChessPiece(white, ChessPiece.PieceType.ROOK);
        for (int j = 0; j < 8; j++) {
            this.board[1][j] = new ChessPiece(white, ChessPiece.PieceType.PAWN);
        }
        // Sets middle of board as empty
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j] = null;
            }
        }
        // Sets white pieces
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        for (int j = 0; j < 8; j++) {
            this.board[6][j] = new ChessPiece(black, ChessPiece.PieceType.PAWN);
        }
        this.board[7][0] = new ChessPiece(black, ChessPiece.PieceType.ROOK);
        this.board[7][1] = new ChessPiece(black, ChessPiece.PieceType.KNIGHT);
        this.board[7][2] = new ChessPiece(black, ChessPiece.PieceType.BISHOP);
        this.board[7][3] = new ChessPiece(black, ChessPiece.PieceType.QUEEN);
        this.board[7][4] = new ChessPiece(black, ChessPiece.PieceType.KING);
        this.board[7][5] = new ChessPiece(black, ChessPiece.PieceType.BISHOP);
        this.board[7][6] = new ChessPiece(black, ChessPiece.PieceType.KNIGHT);
        this.board[7][7] = new ChessPiece(black, ChessPiece.PieceType.ROOK);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    public ChessBoard deepcopy() {
        ChessBoard rBoard = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = this.board[i][j];
                if (piece == null) { rBoard.board[i][j] = null; }
                else { rBoard.board[i][j] = piece.deepcopy(); }
            }
        }
        return rBoard;
    }

    @Override
    public String toString() {
        StringBuilder rString = new StringBuilder();
        rString.append("\n");
        for(int i = 1; i < 9; i++) {
            rString.append("|");
            for(int j = 1; j < 9; j++) {
                ChessPiece piece = getPiece(new ChessPosition(i, j));
                if (piece == null) {
                    rString.append(" ");
                }
                else {
                    ChessPiece.PieceType type = piece.getPieceType();
                    if (type == ChessPiece.PieceType.KING) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { rString.append("K"); }
                        else { rString.append("k"); }
                    } else if (type == ChessPiece.PieceType.QUEEN) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { rString.append("Q"); }
                        else { rString.append("q"); }
                    } else if (type == ChessPiece.PieceType.BISHOP) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { rString.append("B"); }
                        else { rString.append("b"); }
                    } else if (type == ChessPiece.PieceType.KNIGHT) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { rString.append("N"); }
                        else { rString.append("n"); }
                    } else if (type == ChessPiece.PieceType.ROOK) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { rString.append("R"); }
                        else { rString.append("r"); }
                    } else if (type == ChessPiece.PieceType.PAWN) {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) { rString.append("P"); }
                        else { rString.append("p"); }
                    } else {
                        rString.append(" ");
                    }
                }
                rString.append("|");
            }
            rString.append("\n");
        }
        return rString.toString();
    }
}

package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece [][] board = new ChessPiece[8][8];

    public ChessBoard() {
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
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
        for(int j = 0; j < 8; j++) {
            this.board[1][j] = new ChessPiece(white, ChessPiece.PieceType.PAWN);
        }
        // Sets middle of board as empty
        for(int i = 2; i < 6; i++) {
            for(int j = 0; j < 8; j++) {
                this.board[i][j] = null;
            }
        }
        // Sets black pieces
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        for(int j = 0; j < 8; j++) {
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
}

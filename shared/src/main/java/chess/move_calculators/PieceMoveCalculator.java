package chess.move_calculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public abstract class PieceMoveCalculator {
    protected ChessBoard board;
    protected ChessPosition position;

    public PieceMoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public abstract Collection<ChessMove> calculateMoves();

    protected Collection<ChessMove> kingKnightCalculator(int [][] characteristicCombos) {
        ChessGame.TeamColor movePieceColor = this.board.getPiece(this.position).getTeamColor();
        ArrayList<ChessMove> moves = new ArrayList<>();
        // Adds characteristic knight moves
        int row = this.position.getRow();
        int col = this.position.getColumn();
        for (int[] combo : characteristicCombos) {
            int newRow = row + combo[0];
            int newCol = col + combo[1];
            ChessPosition tmpPos = new ChessPosition(newRow, newCol);
            if (!tmpPos.outOfBounds()) {
                ChessPiece jumpedPiece = this.board.getPiece((tmpPos));
                if (jumpedPiece == null) { moves.add(new ChessMove(this.position, tmpPos, null)); }
                else if (!(jumpedPiece.getTeamColor() == movePieceColor)) {
                    moves.add(new ChessMove(this.position, tmpPos, null));
                }
            }
        }
        return moves;
    }

    protected Collection<ChessMove> bishopCalculator() {
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
                if(!(hasClearDiagonalPath(this.position, tmpPos))) { continue; }
                ChessPiece jumpedPiece = this.board.getPiece((tmpPos));
                if (jumpedPiece == null) { moves.add(new ChessMove(this.position, tmpPos, null)); }
                else if (!(jumpedPiece.getTeamColor() == movePieceColor)) {
                    moves.add(new ChessMove(this.position, tmpPos, null));
                }
            }
        }
        return moves;
    }

    protected Collection<ChessMove> rookCalculator() {
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
                if(!(hasClearStraightPath(this.position, tmpPos))) { continue; }
                ChessPiece jumpedPiece = this.board.getPiece((tmpPos));
                if (jumpedPiece == null) { moves.add(new ChessMove(this.position, tmpPos, null)); }
                else if (!(jumpedPiece.getTeamColor() == movePieceColor)) {
                    moves.add(new ChessMove(this.position, tmpPos, null));
                }
            }
        }
        return moves;
    }

    protected boolean hasClearStraightPath(ChessPosition first, ChessPosition second) {
        // ChessPositions passed in must be straight across from each other. Undefined behavior if not
        ArrayList<Integer[]> intermediates = first.findStraightPositionsBetween(second);
        return allPositionsClear(intermediates);
    }

    protected boolean hasClearDiagonalPath(ChessPosition first, ChessPosition second) {
        // ChessPositions passed in must be exactly diagonal from each other. Undefined behavior if not
        ArrayList<Integer[]> intermediates = first.findDiagonalPositionsBetween(second);
        return allPositionsClear(intermediates);
    }

    private boolean allPositionsClear(Collection<Integer []> intermediates) {
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

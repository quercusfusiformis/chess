package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    public boolean outOfBounds() {
        return (this.row < 1 || this.row > 8 || this.col < 1|| this.col > 8);
    }

    public ArrayList<Integer[]> findStraightPositionsBetween(ChessPosition other) {
        // Handling of rook-style paths
        int otherRow = other.getRow();
        int otherCol = other.getColumn();
        if (this.row == otherRow && this.col == otherCol) { return new ArrayList<>(); }
        ArrayList<Integer[]> intermediates = new ArrayList<>();
        if (this.row == otherRow) {
            if (this.col > otherCol) {
                for(int i = otherCol + 1; i < this.col; i++) {
                    Integer [] coords = {otherRow, i};
                    intermediates.add(coords);
                }
            }
            if (otherCol > this.col) {
                for(int i = this.col + 1; i < otherCol; i++) {
                    Integer [] coords = {otherRow, i};
                    intermediates.add(coords);
                }
            }
        }
        else if (this.col == otherCol) {
            if (this.row > otherRow) {
                for(int i = otherRow + 1; i < this.row; i++) {
                    Integer [] coords = {i, otherCol};
                    intermediates.add(coords);
                }
            }
            if (otherRow > this.row) {
                for(int i = this.row + 1; i < otherRow; i++) {
                    Integer [] coords = {i, otherCol};
                    intermediates.add(coords);
                }
            }
        }
        return intermediates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + this.row + "," + this.col + ")";
    }
}

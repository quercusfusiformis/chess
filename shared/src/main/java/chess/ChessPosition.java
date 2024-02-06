package chess;

import java.util.ArrayList;
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

    public ArrayList<Integer[]> findDiagonalPositionsBetween(ChessPosition other) {
        // Handling of bishop-style paths
        int otherRow = other.getRow();
        int otherCol = other.getColumn();
        if (this.row == otherRow && this.col == otherCol) { return new ArrayList<>(); }
        // Generate all diagonal adjustments to see if other is in any diagonal
        Integer[] adjustToOther = getDiagonalAdjustment(otherRow, otherCol);

        // Find which direction to search back and find all positions in between the two specified positions
        Integer [] backAdjuster = {};
        if (adjustToOther[0] > 0 && adjustToOther[1] > 0) { backAdjuster = new Integer[]{-1, -1}; }
        else if (adjustToOther[0] > 0 && adjustToOther[1] < 0) { backAdjuster = new Integer[]{-1, 1}; }
        else if (adjustToOther[0] < 0 && adjustToOther[1] > 0) { backAdjuster = new Integer[]{1, -1}; }
        else if (adjustToOther[0] < 0 && adjustToOther[1] < 0) { backAdjuster = new Integer[]{1, 1}; }
        ArrayList<Integer[]> intermediates = new ArrayList<>();
        Integer [] currCoord = {otherRow, otherCol};
        while(!(currCoord[0] == this.row) && !(currCoord[1] == this.col)) {
            currCoord = new Integer[]{ currCoord[0] + backAdjuster[0], currCoord[1] + backAdjuster[1]};
            if (currCoord[0] == this.row && currCoord[1] == this.col) { break; }
            else { intermediates.add(currCoord); }
        }
        return intermediates;
    }

    private Integer[] getDiagonalAdjustment(int otherRow, int otherCol) {
        ArrayList<Integer[]> diagAdjusts = new ArrayList<>();
        for(int i = 1; i < 8; i++) {
            Integer [] perm1 = {i, i};
            Integer [] perm2 = {i, -i};
            Integer [] perm3 = {-i, i};
            Integer [] perm4 = {-i, -i};
            diagAdjusts.add(perm1);
            diagAdjusts.add(perm2);
            diagAdjusts.add(perm3);
            diagAdjusts.add(perm4);
        }
        Integer [] adjustToOther = {};
        for(Integer [] combo : diagAdjusts) {
            int adjRow = this.row + combo[0];
            int adjCol = this.col + combo[1];
            if (adjRow == otherRow && adjCol == otherCol) { adjustToOther = combo; }
        }
        return adjustToOther;
    }

    @Override
    public boolean equals(Object o) {
        // May not function as intended: needs further attention
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPosition that = (ChessPosition) o;
        return (this.row == that.row && this.col == that.col);

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

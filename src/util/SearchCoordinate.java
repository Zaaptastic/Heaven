package util;

public class SearchCoordinate {
    private int row;
    private int col;
    private int distanceFromOrigin;

    public SearchCoordinate(int row, int col, int distanceFromOrigin) {
        this.row = row;
        this.col = col;
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDistanceFromOrigin() {
        return distanceFromOrigin;
    }

    public boolean isEqual(SearchCoordinate otherCoordinate) {
        if (otherCoordinate == null) {
            return false;
        }
        if (this.row == otherCoordinate.row && this.col == otherCoordinate.col) {
            return true;
        }
        return false;
    }
}

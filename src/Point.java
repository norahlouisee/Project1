import java.util.HashMap;

public class Point {
    private final int row;
    private final int col;

    public Point(int x, int y) {
        row = y;
        col = x;
    }

    public int getRow() { return row; }

    public int getCol() { return col; }

    // Return a new point to the north of the current point
    public Point north() {
        return new Point(col, row - 1);
    }

    // Return a new point to the north of the current point
    public Point south() {
        return new Point(col, row + 1);
    }

    // Return a new point to the east of the current point
    public Point east() {
        return new Point(col + 1, row);
    }

    // Return a new point to the west of the current point
    public Point west() {
        return new Point(col - 1, row);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

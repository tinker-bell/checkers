package checkers.models;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Boolean isWhite() {
        return (getRow() % 2 == 0 && getColumn() % 2 != 0) ||
                (getRow() % 2 != 0 && getColumn() % 2 == 0);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Position)) {
            return false;
        }
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return (row + 50) * (column + 100);
    }

    @Override
    public String toString() {
        return String.format("Position<Row: %s Column: %s Is White: %s>", row, column, isWhite());
    }

    public boolean isDiagonalNeighbour(Position position, int distance) {
        return Math.abs(row - position.getRow()) == distance && Math.abs(column - position.getColumn()) == distance;
    }

    public boolean isSameDiagonal(Position position) {
        return Math.abs(row - position.getRow()) == Math.abs(column - position.getColumn());
    }
}

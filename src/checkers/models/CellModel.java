package checkers.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CellModel {
    private Position position;
    private ObjectProperty<PieceModel> piece = new SimpleObjectProperty<>();

    public CellModel(int row, int column) {
        position = new Position(row, column);
    }

    public final PieceModel getPiece() {
        return piece.getValue();
    }

    public void setPiece(final PieceModel value) {
        piece.setValue(value);
    }

    public final ObjectProperty<PieceModel> pieceProperty() {
        return piece;
    }

    public boolean isEmpty() {
        return getPiece() == null;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isWhite() {
        return getPosition().isWhite();
    }

    public boolean isForwardMove(Position position) {
        if (getPiece() == null) {
            return false;
        }

        return getPiece().getIsWhite() ?
                position.getRow() > getPosition().getRow() :
                position.getRow() < getPosition().getRow();
    }

    public List<Position> getPossibleMovePositions() {
        List<Position> result = new ArrayList<>();

        if (getPiece() == null) {
            return result;
        }

        int row = getPosition().getRow();
        int column = getPosition().getColumn();

        int maxDistance = getPiece().getIsKing() ? 6 : 2;

        for (int i = 1; i <= maxDistance; i++) {
            Position[] positions = new Position[]{
                    new Position(row + i, column + i),
                    new Position(row + i, column - i),
                    new Position(row - i, column + i),
                    new Position(row - i, column - i)};

            result.addAll(Arrays.asList(Arrays.stream(positions)
                    .filter(this::isValidPosition)
                    .toArray(Position[]::new)));
        }

        return result;
    }

    public boolean isValidPosition(Position p) {
        final int min = 0;
        final int max = 7;
        return p.getRow() >= min && p.getRow() <= max &&
                p.getColumn() >= min && p.getColumn() <= max;
    }
}

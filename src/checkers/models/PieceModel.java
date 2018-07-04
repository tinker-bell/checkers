package checkers.models;

public class PieceModel {
    private boolean isWhite;
    private boolean isKing;
    private boolean isSelected;
    private Position position;

    public PieceModel(boolean isWhite, Position position) {
        this.isWhite = isWhite;
        this.position = position;
    }

    public boolean getIsWhite() {
        return this.isWhite;
    }

    public void setIsKing(boolean value) {
        this.isKing = value;
    }

    public boolean getIsKing() {
        return this.isKing;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean value) {
        isSelected = value;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position value) {
        position = value;
    }

    public boolean isEnemyTo(PieceModel piece) {
        return isWhite != piece.getIsWhite();
    }
}

package checkers.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;


public class GameBoardModel {
    private boolean isWhitesTurn = true;
    private CellModel selectedCell;
    private final List<CellModel> cells = new ArrayList<>();
    private final int whitesKingRow = 7;
    private final int blacksKingRow = 0;

    private final List<Position> whiteInitialPositions = Arrays.asList(
            new Position(0, 0), new Position(0, 2), new Position(0, 4),
            new Position(0, 6), new Position(1, 1), new Position(1, 3),
            new Position(1, 5), new Position(1, 7),
            new Position(2, 0), new Position(2, 2),
            new Position(2, 4), new Position(2, 6));

    private final List<Position> blackInitialPositions = Arrays.asList(
            new Position(5, 1), new Position(5, 3),
            new Position(5, 5), new Position(5, 7),
            new Position(6, 0), new Position(6, 2), new Position(6, 4),
            new Position(6, 6), new Position(7, 1), new Position(7, 3),
            new Position(7, 5), new Position(7, 7));


    public GameBoardModel() {
        for (int row = 0; row <= 7; row++) {
            for (int column = 0; column <= 7; column++) {
                CellModel cell = new CellModel(row, column);
                if (whiteInitialPositions.contains(cell.getPosition())) {
                    cell.setPiece(new PieceModel(true, cell.getPosition()));
                }
                if (blackInitialPositions.contains(cell.getPosition())) {
                    cell.setPiece(new PieceModel(false, cell.getPosition()));
                }
                this.cells.add(cell);
            }
        }
    }

    public List<CellModel> getCells() {
        return this.cells;
    }

    public CellModel getCellByPosition(Position position) {
        return cells.stream().filter((x) -> x.getPosition().equals(position)).findFirst().orElse(null);
    }

    public void onCellClicked(Position position) {
        System.out.println(position);

        if (position.isWhite()) {
            return;
        }

        CellModel clickedCell = getCellByPosition(position);

        if (clickedCell.isEmpty()) {
            tryMoveTo(position);
        } else {
            trySelect(clickedCell);
        }
    }

    private void tryMoveTo(Position newPosition) {
        if (selectedCell == null || selectedCell.getPiece() == null || newPosition.isWhite()) {
            return;
        }

        PieceModel selectedPiece = selectedCell.getPiece();
        if (isWhitesTurn != selectedPiece.getIsWhite()) {
            return;
        }

        CellModel newCell = getCellByPosition(newPosition);

        if (!newCell.isEmpty()) {
            return;
        }

        BiConsumer<CellModel, CellModel> moveAction = selectedPiece.getIsKing() ? this::tryMoveKing : this::tryMoveMan;

        moveAction.accept(selectedCell, newCell);
    }

    private void tryMoveMan(CellModel fromCell, CellModel toCell) {
        Position fromPosition = fromCell.getPosition();
        Position toPosition = toCell.getPosition();

        if (fromPosition.isDiagonalNeighbour(toPosition, 1) &&
                fromCell.isForwardMove(toPosition)) {
            makeMove(fromCell, toCell, null);
        } else if (fromCell.getPosition().isDiagonalNeighbour(toCell.getPosition(), 2)) {
            CellModel enemyCell = getCellToCapture(fromCell, toCell);
            if (enemyCell != null) {
                makeMove(fromCell, toCell, enemyCell);
            }
        }
    }

    private void tryMoveKing(CellModel fromCell, CellModel toCell) {
        if (fromCell.getPosition().isSameDiagonal(toCell.getPosition())) {
            if (isEmptyDiagonal(fromCell, toCell)) {
                makeMove(fromCell, toCell, null);
            } else {
                CellModel enemyCell = getCellToCapture(fromCell, toCell);
                if (enemyCell != null) {
                    makeMove(fromCell, toCell, enemyCell);
                }
            }
        }
    }

    private void makeMove(CellModel from, CellModel to, CellModel captured) {
        if (getCellWithCaptureRequired() != null && captured == null) {
            return;
        }

        PieceModel fromPiece = from.getPiece();
        to.setPiece(fromPiece);
        from.setPiece(null);
        trySetKing(to);

        if (captured != null) {
            captured.setPiece(null);
        }

        boolean swapped = trySwapTurns(captured == null);
        fromPiece.setIsSelected(!swapped);
    }

    private boolean trySwapTurns(boolean wasSimpleMove) {
        if (!wasSimpleMove && getCellWithCaptureRequired() != null) {
            return false;
        }

        isWhitesTurn = !isWhitesTurn;
        selectedCell = null;
        return true;
    }

    private CellModel getCellWithCaptureRequired() {
        CellModel[] activeCells = cells.stream()
                .filter((c) -> c.getPiece() != null && c.getPiece().getIsWhite() == isWhitesTurn)
                .toArray(CellModel[]::new);

        return Arrays.stream(activeCells).filter(this::isCaptureRequired).findFirst().orElse(null);
    }

    private boolean isCaptureRequired(CellModel cell) {
        for (Position possiblePosition : cell.getPossibleMovePositions()) {
            CellModel possibleCell = getCellByPosition(possiblePosition);
            if (possibleCell.isEmpty()) {
                CellModel toCapture = getCellToCapture(cell, possibleCell);
                if (toCapture != null) {
                    return true;
                }
            }
        }

        return false;
    }

    private void trySetKing(CellModel cell) {
        PieceModel piece = cell.getPiece();
        int row = cell.getPosition().getRow();
        if (piece != null && !piece.getIsKing()) {
            if (piece.getIsWhite() && row == whitesKingRow || !piece.getIsWhite() && row == blacksKingRow) {
                piece.setIsKing(true);
            }
        }
    }

    private boolean isEmptyDiagonal(CellModel from, CellModel to) {
        List<CellModel> cells = getCellsBetween(from, to);

        return cells.stream().allMatch(CellModel::isEmpty);
    }

    private CellModel getCellToCapture(CellModel from, CellModel to) {
        PieceModel fromPiece = from.getPiece();
        List<CellModel> cells = getCellsBetween(from, to);

        CellModel[] notEmptyCells = cells.stream().filter((c) -> !c.isEmpty()).toArray(CellModel[]::new);
        if (notEmptyCells.length != 1) {
            return null;
        }

        CellModel toCapture = notEmptyCells[0];
        if (fromPiece.isEnemyTo(toCapture.getPiece())) {
            return toCapture;
        }
        return null;
    }


    private List<CellModel> getCellsBetween(CellModel from, CellModel to) {
        List<CellModel> result = new ArrayList<>();
        int fromRow = from.getPosition().getRow();
        int fromColumn = from.getPosition().getColumn();
        int toRow = to.getPosition().getRow();
        int toColumn = to.getPosition().getColumn();
        int stepsCount = Math.abs(fromRow - toRow) - 1;
        int rowStep = fromRow < toRow ? 1 : -1;
        int columnStep = fromColumn < toColumn ? 1 : -1;

        for (int i = 1; i <= stepsCount; i++) {
            CellModel cell = getCellByPosition(
                    new Position(fromRow + i * rowStep, fromColumn + i * columnStep));
            result.add(cell);
        }

        return result;
    }

    private void trySelect(CellModel cell) {
        PieceModel piece = cell.getPiece();
        if (piece != null) {
            if (isWhitesTurn == piece.getIsWhite()) {
                if (selectedCell != null && selectedCell.getPiece() != null) {
                    selectedCell.getPiece().setIsSelected(false);
                }
                selectedCell = cell;
                piece.setIsSelected(true);
            }
        }
    }
}


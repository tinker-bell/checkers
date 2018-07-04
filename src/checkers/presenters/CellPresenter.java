package checkers.presenters;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import checkers.models.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CellPresenter {
    private final CellModel cellModel;
    private final Pane cellView;

    public CellPresenter(CellModel cellModel, Pane cellView) {
        this.cellModel = cellModel;
        this.cellView = cellView;

        this.cellModel.pieceProperty().addListener((ObservableValue<? extends PieceModel> ov,
                                                    PieceModel oldVal,
                                                    PieceModel newVal) -> {
            System.out.println("old value:" + oldVal);
            System.out.println("new value:" + newVal);
            this.render(newVal);
        });

        this.render(this.cellModel.getPiece());
    }

    public void rerender() {
        render(this.cellModel.getPiece());
    }

    private void render(PieceModel pieceModel) {
        this.cellView.getChildren().clear();

        if (pieceModel == null) {
            this.cellView.getStyleClass().remove("selected-cell");
            return;
        }

        if (pieceModel.getIsSelected()) {
            if (!this.cellView.getStyleClass().contains("selected-cell")) {
                this.cellView.getStyleClass().add("selected-cell");
            }
        } else {
            this.cellView.getStyleClass().remove("selected-cell");
        }

        if (pieceModel.getIsKing()) {
            this.cellView.getChildren().addAll(this.buildKingPiece(pieceModel.getIsWhite()));
        } else {
            this.cellView.getChildren().addAll(this.buildManPiece(pieceModel.getIsWhite()));
        }
    }

    private List<Node> buildKingPiece(boolean isWhite) {
        Circle circle1 = new Circle();
        circle1.setCenterX(22);
        circle1.setCenterY(22);
        circle1.setRadius(18);
        circle1.setStrokeType(StrokeType.INSIDE);

        Circle circle2 = new Circle();
        circle2.setCenterX(12);
        circle2.setCenterY(12);
        circle2.setLayoutX(10);
        circle2.setLayoutY(10);
        circle2.setRadius(10);
        circle2.setStrokeType(StrokeType.INSIDE);

        if (isWhite) {
            circle1.getStyleClass().add("white-piece");
            circle2.getStyleClass().add("white-piece");
        } else {
            circle1.getStyleClass().add("black-piece");
            circle2.getStyleClass().add("black-piece");
        }

        return Arrays.asList(circle1, circle2);
    }

    private List<Node> buildManPiece(boolean isWhite) {
        Circle circle1 = new Circle();
        circle1.setCenterX(22);
        circle1.setCenterY(22);
        circle1.setRadius(18);
        circle1.setStrokeType(StrokeType.INSIDE);

        if (isWhite) {
            circle1.getStyleClass().add("white-piece");
        } else {
            circle1.getStyleClass().add("black-piece");
        }

        return Arrays.asList(circle1);
    }
}

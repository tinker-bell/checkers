package checkers;

import checkers.models.CellModel;
import checkers.models.GameBoardModel;
import checkers.presenters.CellPresenter;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;


public class BoardTestController {

    @FXML
    private GridPane boardGridPane;
    private GameBoardModel gameBoard;
    private List<CellPresenter> cellPresenters = new ArrayList<>();


    public void initialize() {
        resetGame();
    }


    private void resetGame() {
        boardGridPane.getChildren().clear();

        gameBoard = new GameBoardModel();

        for (CellModel cellModel : gameBoard.getCells()) {
            Pane cellPane = new Pane();

            var cellStyle = cellModel.isWhite() ? "white-cell" : "black-cell";

            cellPane.getStyleClass().add(cellStyle);

            if (!cellModel.isWhite()) {
                final EventHandler<MouseEvent> mouseClickedHandler =
                        (MouseEvent e) -> {
                            gameBoard.onCellClicked(cellModel.getPosition());
                            e.consume();
                        };
                cellPane.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickedHandler);
            }

            boardGridPane.add(cellPane,
                    7 - cellModel.getPosition().getColumn(),
                    7 - cellModel.getPosition().getRow());

            cellPresenters.add(new CellPresenter(cellModel, cellPane));
        }
    }
}

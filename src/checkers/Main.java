package checkers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Board.fxml"));
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(new Scene(root, 420, 470));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

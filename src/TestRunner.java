import classes.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TestRunner extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Heaven Test App");
        //Multiple Stages for each main view, multiple scenes per Stage.
        Label label = new Label("Welcome! ");
        Button button = new Button("Start Game");

        Controller controller = new Controller(label);
        button.setOnAction(value -> {
            Stage gameStage = new Stage();
            primaryStage.close();
            controller.startNewGame(gameStage);
        });

        VBox hbox = new VBox(label,button);

        Scene scene = new Scene(hbox, 400, 400);
        primaryStage.setScene(scene);


        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

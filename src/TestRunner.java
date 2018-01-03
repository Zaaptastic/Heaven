import classes.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.applet.Applet;

/*
    <applet code="TestRunner" width=800 height=600>
    </applet>
 */
public class TestRunner extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Heaven Test App");
        //Multiple Stages for each main view, multiple scenes per Stage.
        Label label = new Label("Heaven");
        Button button = new Button("Start Game");

        Controller controller = new Controller(label);
        button.setOnAction(value -> {
            Stage gameStage = new Stage();
            primaryStage.close();
            controller.startNewGame(gameStage);
        });

        HBox hbox = new HBox(button, label);

        Scene scene = new Scene(hbox, 800, 600);
        primaryStage.setScene(scene);


        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /*public void paint(Graphics g) {
        g.drawString("welcome", 150, 150);

    }*/

    /*public static void main(String[] args) {
        //Controller controller = new Controller();
        //controller.startNewGame();

    }*/
}

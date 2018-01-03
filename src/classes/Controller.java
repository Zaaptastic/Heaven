package classes;

import battlefields.TutorialIsland;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.HeavenReturnStatus;

public class Controller {
    private Label label;

    public Controller(Label label) {
        this.label = label;
    }

    public HeavenReturnStatus startNewGame(Stage gameStage) {
        // TODO: Allow selection of BattlefieldSpecification
        GameController gameController = new GameController(new TutorialIsland(), gameStage);

        HeavenReturnStatus endOfTurnStatus = new HeavenReturnStatus(true);

        endOfTurnStatus = gameController.initializeGame();

        return endOfTurnStatus;
    }

}

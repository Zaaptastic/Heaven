package classes;

import battlefields.TutorialIsland;
import util.HeavenConstants;
import util.HeavenReturnStatus;

public class Controller {

    public HeavenReturnStatus startNewGame() {
        // TODO: Allow selection of BattlefieldSpecification
        GameController gameController = new GameController(new TutorialIsland());

        HeavenReturnStatus endOfTurnStatus = new HeavenReturnStatus(true);

        while(endOfTurnStatus.getSuccessStatus() && endOfTurnStatus.getEvent() != HeavenConstants.Event.CAPITAL_CAPTURE) {
            endOfTurnStatus = gameController.nextTurn();
        }

        return endOfTurnStatus;
    }

}

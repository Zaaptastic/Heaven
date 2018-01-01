package classes;

import battlefields.TutorialIsland;
import util.HeavenConstants;
import util.HeavenReturnStatus;

public class Controller {

    public HeavenReturnStatus startNewGame() {
        // TODO: Allow selection of BattlefieldSpecification
        GameController gameController = new GameController(new TutorialIsland());

        HeavenReturnStatus endOfTurnStatus = new HeavenReturnStatus(true);

        System.out.println("LOG: Starting game");
        while(endOfTurnStatus.getSuccessStatus()) {
            endOfTurnStatus = gameController.nextTurn();
            if (endOfTurnStatus.getEvent() == HeavenConstants.Event.CAPITAL_CAPTURE) {
                break;
            }
        }

        System.out.println(gameController.getGameLog());

        return endOfTurnStatus;
    }

}

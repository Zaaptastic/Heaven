package battlefields;

import classes.Structure;
import util.HeavenConstants.*;

import java.util.ArrayList;

public class TutorialIsland extends BattlefieldSpecification {
    public TutorialIsland() {
        this.length = 10;
        this.width = 10;
        this.structures = getBattlefieldStructures();
    }

    private ArrayList<Structure> getBattlefieldStructures() {
        ArrayList<Structure> toReturn = new ArrayList<>();

        // Player Capitals
        toReturn.add(new Structure(StructureType.CAPITAL, 1, 1, Player.PLAYER_ONE));
        toReturn.add(new Structure(StructureType.CAPITAL, 8, 8, Player.PLAYER_TWO));

        // Starting Player Structures
        toReturn.add(new Structure(StructureType.FACTORY, 2,2, Player.PLAYER_ONE));
        toReturn.add(new Structure(StructureType.FACTORY, 7,7, Player.PLAYER_TWO));

        // Neutral Structures
        toReturn.add(new Structure(StructureType.CITY, 0,3));
        toReturn.add(new Structure(StructureType.CITY, 3, 0));
        toReturn.add(new Structure(StructureType.CITY, 3,6));
        toReturn.add(new Structure(StructureType.CITY, 6, 3));
        toReturn.add(new Structure(StructureType.CITY, 9, 6));
        toReturn.add(new Structure(StructureType.CITY, 6, 9));
        toReturn.add(new Structure(StructureType.CITY, 4, 4));
        toReturn.add(new Structure(StructureType.CITY, 5, 5));

        return toReturn;
    }
}

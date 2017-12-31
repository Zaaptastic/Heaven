import util.HeavenReturnStatus;

import java.util.ArrayList;

public class Battlefield {
    private int width;
    private int length;
    private ArrayList<Unit> units;
    private ArrayList<Structure> structures;

    public Battlefield() {
        this.width = 100;
        this.length = 100;
        this.units = new ArrayList<>();
        //TODO: Add method to validate structures as they are added. Make the structures list static
        this.structures = new ArrayList<>();
    }

    public HeavenReturnStatus addUnit(Unit unit) {
        try {
            units.add(unit);
            return new HeavenReturnStatus(true);
        } catch (Exception e) {
            return new HeavenReturnStatus(false, "Could not add unit to battlefield");
        }
    }

    public HeavenReturnStatus removeUnit(Unit unit) {
        if (units.remove(unit)) {
            return new HeavenReturnStatus(true);
        } else {
            return new HeavenReturnStatus(false, "Could not remove unit from battlefield");
        }
    }


}
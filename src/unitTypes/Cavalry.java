package unitTypes;

import util.HeavenConstants;

public class Cavalry extends UnitType {
    public Cavalry() {
        super(100, 5, 3, 1, 5,
                HeavenConstants.UnitClass.MELEE, 7000, "C");
    }
}

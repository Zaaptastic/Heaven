package unitTypes;

import util.HeavenConstants;

public class Cavalry extends UnitType {
    public Cavalry() {
        super(100, 3, 2, 1, 3, HeavenConstants.UnitClass.MELEE);
    }
}

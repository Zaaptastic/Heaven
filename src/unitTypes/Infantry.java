package unitTypes;

import util.HeavenConstants;

public class Infantry extends UnitType {
    public Infantry() {
        super(100, 3, 2, 1, 3, HeavenConstants.UnitClass.MELEE);
    }
}

package unitTypes;

import util.HeavenConstants;

public class Artillery extends UnitType{
    public Artillery() {
        super(100, 3, 2, 1, 3, HeavenConstants.UnitClass.RANGED);
    }
}

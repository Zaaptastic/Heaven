package unitTypes;

import util.HeavenConstants;

public class Artillery extends UnitType{
    public Artillery() {
        super(100, 7, 1, 3, 3, HeavenConstants.UnitClass.RANGED, 6000);
    }
}
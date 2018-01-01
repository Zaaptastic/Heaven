package classes;

import util.HeavenUtils;

public class Square {
    private int row;
    private int col;
    private Unit unitOnSquare;
    private Structure structureOnSquare;

    public Square(int row, int col, Structure structureOnSquare) {
        this.row = row;
        this.col = col;
        this.structureOnSquare = structureOnSquare;
        this.unitOnSquare = null;
    }

    public Square(int row, int col) {
        this.row = row;
        this.col = col;
        this.structureOnSquare = null;
        this.unitOnSquare = null;
    }

    public String toString() {
        // Format: [ <Unit Id><Owning Player> | <Structure Id><Owning Player> ]
        // ex. P1 Infantry on P1 Capital: [ I1 | *1 ]
        // ex. P2 Infantry on unowned city: [I2 | $_ ]
        String unitId = "_";
        String unitOwnerId = "_";
        String structureId = "_";
        String structureOwnerId = "_";
        if (unitOnSquare != null) {
            unitId = unitOnSquare.getUnitType().getIdentifier();
            unitOwnerId = HeavenUtils.getUnitOwnerIdentifier(unitOnSquare);
        }
        if (structureOnSquare != null) {
            structureId = HeavenUtils.getStructureIdentifier(structureOnSquare);
            structureOwnerId = HeavenUtils.getStructureOwnerIdentifier(structureOnSquare);
        }
        return "[ " + unitId + unitOwnerId + " | " + structureId + structureOwnerId + " ] ";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Unit getUnitOnSquare() {
        return unitOnSquare;
    }

    public void setUnitOnSquare(Unit unitOnSquare) {
        this.unitOnSquare = unitOnSquare;
    }

    public Structure getStructureOnSquare() {
        return structureOnSquare;
    }

    public void setStructureOnSquare(Structure structureOnSquare) {
        this.structureOnSquare = structureOnSquare;
    }
}

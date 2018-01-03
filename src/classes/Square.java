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
        return "[ " + unitId + unitOwnerId + " | " + structureId + structureOwnerId + " ]";
    }

    /**
     * In contrast to the above, this method returns a full representation of information about the current Square.
     * @return STring representation of all Square information.
     */
    public String getFullInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Currently Selected Coordinates: (" + row + "," + col + ")");
        // First, populate with Unit information
        if (unitOnSquare == null) {
            stringBuilder.append("\nUnit: None");
        } else {
            stringBuilder.append(String.format("\nUnit: %s's %s [HP:%d/%d ATK:%d DEF:%d SPEED:%d RANGE:%d-%d]", unitOnSquare.getOwner(),
                    unitOnSquare.getUnitType(), unitOnSquare.getCurrentHealth(), unitOnSquare.getMaximumHealth(),
                    unitOnSquare.getAttackValue(), unitOnSquare.getDefenseValue(), unitOnSquare.getMovementSpeed(),
                    unitOnSquare.getMinimumRange(), unitOnSquare.getMinimumRange()));
        }
        // Next, populate with Structure information
        if (structureOnSquare == null) {
            stringBuilder.append("\nStructure: None");
        } else {
            stringBuilder.append(String.format("\nStructure: %s [HP:%d/%d OWNER:%s]", structureOnSquare.getStructureType(),
                    structureOnSquare.getCurrentHealth(), structureOnSquare.getMaxHealth(), structureOnSquare.getOwner()));
        }
        stringBuilder.append("\n\n");

        return stringBuilder.toString();
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

package classes;

import battlefields.BattlefieldSpecification;
import util.HeavenReturnStatus;

import java.util.ArrayList;

public class Battlefield {
    private int width;
    private int length;
    private ArrayList<Unit> units;
    private ArrayList<Structure> structures;
    private Unit[][] grid;

    public Battlefield(BattlefieldSpecification battlefieldSpecification) {
        this.width = battlefieldSpecification.getWidth();
        this.length = battlefieldSpecification.getLength();
        this.units = new ArrayList<>();
        this.grid = new Unit[length][width];
        this.structures = battlefieldSpecification.getStructures();
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public HeavenReturnStatus addUnit(Unit unit, int row, int col) {
        try {
            units.add(unit);
            grid[row][col] = unit;
            return new HeavenReturnStatus(true);
        } catch (Exception e) {
            return new HeavenReturnStatus(false, "Could not add unit to battlefield: " + e);
        }
    }

    public HeavenReturnStatus removeUnit(int row, int col) {
        try {
            grid[row][col] = null;
            return new HeavenReturnStatus(true);
        } catch (Exception e) {
            return new HeavenReturnStatus(false, "Could not remove unit from battlefield: " + e);
        }
    }

    public Unit getUnitAtPosition(int row, int col) {
        if (row < 0 || row >= this.length || col < 0 || col >= this.width) {
            return null;
        } else {
            return grid[row][col];
        }
    }

    public boolean isOpenPosition(int row, int col) {
        if (row < 0 || row >= this.length || col < 0 || col >= this.width) {
            return false;
        } else {
            return getUnitAtPosition(row, col) == null;
        }
    }

    public Structure getStructureAtPosition(int row, int col) {
        for (Structure structure : structures) {
            if (structure.getRow() == row && structure.getCol() == col ) {
                return structure;
            }
        }
        return null;
    }

    public String gridToString() {
        //TODO: Update to include structures
        StringBuilder builder = new StringBuilder();
        for (Unit[] row : grid) {
            for (Unit unit : row) {
                if (unit == null) {
                    builder.append("-");
                    break;
                }
                builder.append(unit.getUnitType().getIdentifier());
            }
        }
        return builder.toString();
    }

    public HeavenReturnStatus resetUnitActivity() {
        for (Unit[] row : grid) {
            for (Unit unit : row) {
                unit.setAttacked(false);
                unit.setMoved(false);
            }
        }

        return new HeavenReturnStatus(true);
    }
}
package classes;

import battlefields.BattlefieldSpecification;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.HeavenReturnStatus;

import java.util.ArrayList;

public class Battlefield {
    private int width;
    private int length;
    private ArrayList<Structure> structures;
    private Square[][] grid;

    public Battlefield(BattlefieldSpecification battlefieldSpecification) {
        this.width = battlefieldSpecification.getWidth();
        this.length = battlefieldSpecification.getLength();
        this.grid = new Square[length][width];

        for (int r = 0; r < length; r++) {
            for (int c = 0; c < width; c++) {
                grid[r][c] = new Square(r, c);
            }
        }

        this.structures = battlefieldSpecification.getStructures();
        for (Structure structure : structures) {
            grid[structure.getRow()][structure.getCol()].setStructureOnSquare(structure);
        }
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public HeavenReturnStatus addUnit(Unit unit, int row, int col) {
        try {
            grid[row][col].setUnitOnSquare(unit);
            return new HeavenReturnStatus(true);
        } catch (Exception e) {
            return new HeavenReturnStatus(false, "Could not add unit to battlefield: " + e);
        }
    }

    public HeavenReturnStatus removeUnit(int row, int col) {
        try {
            grid[row][col].setUnitOnSquare(null);
            return new HeavenReturnStatus(true);
        } catch (Exception e) {
            return new HeavenReturnStatus(false, "Could not remove unit from battlefield: " + e);
        }
    }

    public Unit getUnitAtPosition(int row, int col) {
        if (row < 0 || row >= this.length || col < 0 || col >= this.width) {
            return null;
        } else {
            return grid[row][col].getUnitOnSquare();
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
        StringBuilder builder = new StringBuilder();
        for (Square[] row : grid) {
            for (Square square : row) {
                builder.append(square.toString());
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public Square[][] getGrid() {
        return grid.clone();
    }

    public HeavenReturnStatus resetUnitActivity() {
        for (Square[] row : grid) {
            for (Square square : row) {
                Unit unit = square.getUnitOnSquare();
                if (unit == null) {
                    continue;
                }
                unit.setAttacked(false);
                unit.setMoved(false);
            }
        }

        return new HeavenReturnStatus(true);
    }
}
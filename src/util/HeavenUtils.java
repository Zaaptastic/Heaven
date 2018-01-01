package util;

import classes.Battlefield;
import classes.Structure;
import classes.Unit;
import unitTypes.Artillery;
import unitTypes.Cavalry;
import unitTypes.Infantry;
import unitTypes.UnitType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class HeavenUtils {
    public static int calculateDamage(int attacker, int defender) {
        // Take currentHealth/maximumHealth into account as a modifier of attack
        return 100;
    }

    public static ArrayList<UnitType> getAllUnitTypes() {
        ArrayList<UnitType> unitTypes = new ArrayList<>();
        unitTypes.add(new Infantry());
        unitTypes.add(new Artillery());
        unitTypes.add(new Cavalry());
        return unitTypes;
    }

    public static String getStructureIdentifier(Structure structure) {
        HeavenConstants.StructureType structureType = structure.getStructureType();
        if (structureType == HeavenConstants.StructureType.CAPITAL) {
            return "*";
        } else if (structureType == HeavenConstants.StructureType.FACTORY) {
            return "#";
        } else if (structureType == HeavenConstants.StructureType.CITY) {
            return "$";
        } else {
            return "_";
        }
    }

    public static String getStructureOwnerIdentifier(Structure structure) {
        if (structure.getOwner() == HeavenConstants.Player.PLAYER_ONE) {
            return "1";
        } else if (structure.getOwner() == HeavenConstants.Player.PLAYER_TWO) {
            return "2";
        } else {
            return "_";
        }
    }

    public static String getUnitOwnerIdentifier(Unit unit) {
        if (unit.getOwner() == HeavenConstants.Player.PLAYER_ONE) {
            return "1";
        } else if (unit.getOwner() == HeavenConstants.Player.PLAYER_TWO) {
            return "2";
        } else {
            return "_";
        }
    }

    public static HashMap<Integer, Integer> findLegalMoves(Battlefield battlefield, Unit unit, int row, int col) {
        int maxMovementDistance = unit.getMovementSpeed();
        Queue<SearchCoordinate> searchSpace = new LinkedList<>();
        // Add origin to search space to begin alg. Convenient since this is always a valid square.
        searchSpace.add(new SearchCoordinate(row, col, 0));

        return iterativeGridSearch(battlefield, searchSpace, maxMovementDistance, 0);
    }

    public static HashMap<Integer, Integer> findLegalAttacks(Battlefield battlefield, Unit unit, int row, int col) {
        int maxAttackRange = unit.getRange();
        int minAttackRange = unit.getMinimumRange();
        Queue<SearchCoordinate> searchSpace = new LinkedList<>();
        // Add origin to search space to begin alg. However this is not a valid attack square
        searchSpace.add(new SearchCoordinate(row, col, 0));

        // minimumDistance > 0 so that we do not add the Origin, allowing a unit to attack itself.
        return iterativeGridSearch(battlefield, searchSpace, maxAttackRange, minAttackRange);
    }

    private static HashMap<Integer, Integer> iterativeGridSearch(Battlefield battlefield, Queue<SearchCoordinate> searchSpace, int maxDist, int minDist) {
        ArrayList<SearchCoordinate> solutions = new ArrayList<>();

        while (!searchSpace.isEmpty()) {
            SearchCoordinate validMove = searchSpace.remove();
            int currentRow = validMove.getRow();
            int currentCol = validMove.getCol();
            if (validMove.getDistanceFromOrigin() >= minDist) {
                // For findLegalAttacks. See comment above.
                solutions.add(validMove);
            }
            int currentDistanceFromOrigin = validMove.getDistanceFromOrigin();

            // Check each adjacent square, if it is not a solution and not already on the Queue, add it to the Queue.
            // TODO: Remove duplicate work. Don't need to check squares closer to the Origin (going backwards).
            if (currentDistanceFromOrigin == maxDist) {
                continue;
            }

            SearchCoordinate northCoordinate = new SearchCoordinate(currentRow - 1, currentCol, currentDistanceFromOrigin + 1);
            SearchCoordinate southCoordinate = new SearchCoordinate(currentRow +1, currentCol, currentDistanceFromOrigin + 1);
            SearchCoordinate westCoordinate = new SearchCoordinate(currentRow, currentCol-1, currentDistanceFromOrigin + 1);
            SearchCoordinate eastCoordinate = new SearchCoordinate(currentRow, currentCol+1, currentDistanceFromOrigin + 1);

            //North
            if (battlefield.isOpenPosition(currentRow-1, currentCol) && !solutions.contains(northCoordinate)
                    && !searchSpace.contains(northCoordinate)) {
                searchSpace.add(northCoordinate);
            }
            //South
            if (battlefield.isOpenPosition(currentRow+1, currentCol)&& !solutions.contains(southCoordinate)
                    && !searchSpace.contains(southCoordinate)) {
                searchSpace.add(southCoordinate);
            }
            //West
            if (battlefield.isOpenPosition(currentRow, currentCol-1)&& !solutions.contains(westCoordinate)
                    && !searchSpace.contains(westCoordinate)) {
                searchSpace.add(westCoordinate);
            }
            //East
            if (battlefield.isOpenPosition(currentRow, currentCol+1)&& !solutions.contains(eastCoordinate)
                    && !searchSpace.contains(eastCoordinate)) {
                searchSpace.add(eastCoordinate);
            }
        }

        HashMap<Integer, Integer> toReturn = new HashMap<>();
        for (SearchCoordinate solution : solutions) {
            toReturn.put(solution.getRow(), solution.getCol());
        }
        return toReturn;
    }
}

class SearchCoordinate {
    private int row;
    private int col;
    private int distanceFromOrigin;

    public SearchCoordinate(int row, int col, int distanceFromOrigin) {
        this.row = row;
        this.col = col;
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getDistanceFromOrigin() {
        return distanceFromOrigin;
    }
}
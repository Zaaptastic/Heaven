package util;

import classes.Battlefield;
import classes.Structure;
import classes.Unit;
import unitTypes.Artillery;
import unitTypes.Cavalry;
import unitTypes.Infantry;
import unitTypes.UnitType;

import java.util.*;

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

    public static boolean isSearchCoordinateInSelection(ArrayList<SearchCoordinate> selection, int row, int col) {
        for (SearchCoordinate searchCoordinate : selection) {
            if (searchCoordinate.getRow() == row && searchCoordinate.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<SearchCoordinate> findLegalMoves(Battlefield battlefield, Unit unit, int row, int col) {
        int maxMovementDistance = unit.getMovementSpeed();
        Stack<SearchCoordinate> searchSpace = new Stack<>();
        // Add origin to search space to begin alg. Convenient since this is always a valid square.
        searchSpace.add(new SearchCoordinate(row, col, 0));

        return iterativeGridSearch(battlefield, searchSpace, maxMovementDistance, 0, false);
    }

    public static ArrayList<SearchCoordinate> findLegalAttacks(Battlefield battlefield, Unit unit, int row, int col) {
        int maxAttackRange = unit.getRange();
        int minAttackRange = unit.getMinimumRange();
        Stack<SearchCoordinate> searchSpace = new Stack<>();
        // Add origin to search space to begin alg. However this is not a valid attack square
        searchSpace.add(new SearchCoordinate(row, col, 0));

        // minimumDistance > 0 so that we do not add the Origin, allowing a unit to attack itself.
        return iterativeGridSearch(battlefield, searchSpace, maxAttackRange, minAttackRange, true);
    }

    private static ArrayList<SearchCoordinate> iterativeGridSearch(Battlefield battlefield, Stack<SearchCoordinate> searchSpace, int maxDist, int minDist, boolean ignoreUnits) {
        ArrayList<SearchCoordinate> solutions = new ArrayList<>();

        while (!searchSpace.isEmpty()) {
            SearchCoordinate validMove = searchSpace.pop();
            int currentRow = validMove.getRow();
            int currentCol = validMove.getCol();
            if (validMove.getDistanceFromOrigin() >= minDist) {
                // For findLegalAttacks. See comment above.
                solutions.add(validMove);
            }
            int currentDistanceFromOrigin = validMove.getDistanceFromOrigin();

            if (currentDistanceFromOrigin == maxDist) {
                continue;
            }

            SearchCoordinate northCoordinate = new SearchCoordinate(currentRow - 1, currentCol, currentDistanceFromOrigin + 1);
            SearchCoordinate southCoordinate = new SearchCoordinate(currentRow + 1, currentCol, currentDistanceFromOrigin + 1);
            SearchCoordinate westCoordinate = new SearchCoordinate(currentRow, currentCol - 1, currentDistanceFromOrigin + 1);
            SearchCoordinate eastCoordinate = new SearchCoordinate(currentRow, currentCol + 1, currentDistanceFromOrigin + 1);

            //North
            if (battlefield.isOpenPosition(currentRow-1, currentCol, ignoreUnits) && !solutions.contains(northCoordinate)) {
                searchSpace.push(northCoordinate);
            }
            //South
            if (battlefield.isOpenPosition(currentRow+1, currentCol, ignoreUnits)&& !solutions.contains(southCoordinate)) {
                searchSpace.push(southCoordinate);
            }
            //West
            if (battlefield.isOpenPosition(currentRow, currentCol-1, ignoreUnits)&& !solutions.contains(westCoordinate)) {
                searchSpace.push(westCoordinate);
            }
            //East
            if (battlefield.isOpenPosition(currentRow, currentCol+1, ignoreUnits)&& !solutions.contains(eastCoordinate)) {
                searchSpace.push(eastCoordinate);
            }
        }

        //TODO: HashMap is a really bad return structure here, need to find something better. Need a list of tuples, there must be an existing structure.
        return solutions;
        /*HashMap<Integer, Integer> toReturn = new HashMap<>();
        for (SearchCoordinate solution : solutions) {
            toReturn.put(solution.getRow(), solution.getCol());
        }
        System.out.println(toReturn);
        return toReturn;*/
    }
}

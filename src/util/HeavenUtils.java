package util;

import classes.Battlefield;
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

    public static HashMap<Integer, Integer> findLegalMoves(Battlefield battlefield, Unit unit, int row, int col) {
        int maxMovementDistance = unit.getMovementSpeed();
        Queue<SearchCoordinate> searchSpace = new LinkedList<>();
        // Add origin to search space to begin alg. Convenient since this is always a valid square.
        searchSpace.add(new SearchCoordinate(row, col, 0));

        return iterativeGridSearch(battlefield, searchSpace, maxMovementDistance, 0);
    }

    public static HashMap<Integer, Integer> findLegalAttacks(Battlefield battlefield, Unit unit, int row, int col) {
        int maxAttackRange = unit.getRange();
        Queue<SearchCoordinate> searchSpace = new LinkedList<>();
        // Add origin to search space to begin alg. However this is not a valid attack square
        searchSpace.add(new SearchCoordinate(row, col, 0));

        // minimumDistance = 1 so that we do not add the Origin, allowing a unit to attack itself.
        // TODO: Minimum Distances other than 1
        return iterativeGridSearch(battlefield, searchSpace, maxAttackRange, 1);
    }

    private static HashMap<Integer, Integer> iterativeGridSearch(Battlefield battlefield, Queue<SearchCoordinate> searchSpace, int maxDist, int minDist) {
        HashMap<Integer, Integer> solutions = new HashMap<>();

        while (!searchSpace.isEmpty()) {
            SearchCoordinate validMove = searchSpace.remove();
            int currentRow = validMove.getRow();
            int currentCol = validMove.getCol();
            if (validMove.getDistanceFromOrigin() >= minDist) {
                // For findLegalAttacks. See comment above.
                solutions.put(currentRow, currentCol);
            }
            int currentDistanceFromOrigin = validMove.getDistanceFromOrigin();

            // Check each adjacent square, if it is not a solution and not already on the Queue, add it to the Queue.
            // TODO: Remove duplicate work. Don't need to check squares closer to the Origin (going backwards).
            if (currentDistanceFromOrigin == maxDist) {
                continue;
            }
            //North
            if (battlefield.isOpenPosition(currentRow-1, currentCol)) {
                searchSpace.add(new SearchCoordinate(currentRow - 1, currentCol, currentDistanceFromOrigin + 1));
            }
            //South
            if (battlefield.isOpenPosition(currentRow+1, currentCol)) {
                searchSpace.add(new SearchCoordinate(currentRow +1, currentCol, currentDistanceFromOrigin + 1));
            }
            //West
            if (battlefield.isOpenPosition(currentRow, currentCol-1)) {
                searchSpace.add(new SearchCoordinate(currentRow, currentCol-1, currentDistanceFromOrigin + 1));
            }
            //East
            if (battlefield.isOpenPosition(currentRow, currentCol+1)) {
                searchSpace.add(new SearchCoordinate(currentRow, currentCol+1, currentDistanceFromOrigin + 1));
            }
        }

        return solutions;
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
package classes;

import unitTypes.UnitType;
import util.HeavenConstants.*;
import util.HeavenReturnStatus;
import util.HeavenUtils;
import util.HeavenUtils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static util.HeavenUtils.findLegalAttacks;
import static util.HeavenUtils.findLegalMoves;

public class Controller {
    private Battlefield battlefield;
    private Scanner listener;
    private HashMap<String, UnitType> unitTypes;

    private int turnCount;
    private HashMap<Player, Integer> playerFunds;

    public Controller() {
        turnCount = 0;
        battlefield = new Battlefield(100, 100);
        playerFunds = new HashMap<>();
        playerFunds.put(Player.PLAYER_ONE, 0);
        playerFunds.put(Player.PLAYER_TWO, 0);

        listener = new Scanner(System.in);

        unitTypes = new HashMap<>();
        List<UnitType> allUnitTypes = HeavenUtils.getAllUnitTypes();
        for (UnitType unitType : allUnitTypes) {
            unitTypes.put(unitType.getIdentifier(), unitType);
        }
    }

    public HeavenReturnStatus nextTurn() {
        turnCount++;
        HeavenReturnStatus returnStatus = nextTurn(Player.PLAYER_ONE);
        if (returnStatus.getEvent() == Event.CAPITAL_CAPTURE) {
            return returnStatus;
        } else if (!returnStatus.getSuccessStatus()) {
            return returnStatus;
        }

        returnStatus = nextTurn(Player.PLAYER_TWO);
        if (returnStatus.getEvent() == Event.CAPITAL_CAPTURE) {
            return returnStatus;
        } else if (!returnStatus.getSuccessStatus()) {
            return returnStatus;
        }

        return battlefield.resetUnitActivity();
    }

    private HeavenReturnStatus nextTurn(Player player) {
        boolean endOfTurn = false;

        // Beginning of each player's turn: calculate funds

        //TODO: Add turn history and logging
        while (!endOfTurn) {
            // Listen for inputs, executing each as a move, until the player declares end of his turn.
            System.out.println("Temporary UI for making moves:\nend - End Turn\nmR,C:R,C - move unit to new position" +
                    "\naR,C:R,C - attack with unit to position\ncR,C:I - create unit at structure");
            String input = listener.next();

            if (input.equals("end")) {
                endOfTurn = true;
            } else if (input.startsWith("m")) {
                // Attempting to move a unit to a new square.
                String combinedCoords = input.substring(1, input.length());
                String startCoords = combinedCoords.split(":")[0];
                String endCoords = combinedCoords.split(":")[1];
                int startRow = Integer.parseInt(startCoords.split(",")[0]);
                int startCol = Integer.parseInt(startCoords.split(",")[1]);
                int endRow = Integer.parseInt(endCoords.split(",")[0]);
                int endCol = Integer.parseInt(endCoords.split(",")[1]);

                HeavenReturnStatus returnStatus = moveUnit(startRow, startCol, endRow, endCol);
                if (!returnStatus.getSuccessStatus()) {
                    System.out.println(returnStatus.getErrorMsg());
                }
            } else if (input.startsWith("a")) {
                // Attempting to attack another unit.
                String combinedCoords = input.substring(1, input.length());
                String startCoords = combinedCoords.split(":")[0];
                String endCoords = combinedCoords.split(":")[1];
                int startRow = Integer.parseInt(startCoords.split(",")[0]);
                int startCol = Integer.parseInt(startCoords.split(",")[1]);
                int endRow = Integer.parseInt(endCoords.split(",")[0]);
                int endCol = Integer.parseInt(endCoords.split(",")[1]);

                HeavenReturnStatus returnStatus = attackUnit(startRow, startCol, endRow, endCol);
                if (!returnStatus.getSuccessStatus()) {
                    System.out.println(returnStatus.getErrorMsg());
                }
            } else if (input.startsWith("c")) {
                // Attempting to create unit at structure.
                String coords = input.substring(1, input.length()).split(":")[0];
                String identifier = input.substring(1, input.length()).split(":")[1];
                int row = Integer.parseInt(coords.split(",")[0]);
                int col = Integer.parseInt(coords.split(",")[1]);

                HeavenReturnStatus returnStatus = createUnit(row, col, identifier, player);
                if (!returnStatus.getSuccessStatus()) {
                    System.out.println(returnStatus.getErrorMsg());
                }
            } else {
                System.out.println("Could not parse input");
            }
        }

        // At the end of every turn, recalculate Structure health for capturing/restoring.
        for (Structure structure : battlefield.getStructures()) {
            Unit unit = battlefield.getUnitAtPosition(structure.getRow(), structure.getCol());
            if (unit == null) {
                structure.increaseHealth();
                continue;
            }

            if (unit.getOwner() == structure.getOwner()) {
                structure.increaseHealth();
                continue;
            }

            if (unit.getUnitType().getIdentifier().equals("I")) {
                HeavenReturnStatus returnStatus = structure.decreaseHealth(player, unit.getCurrentHealth());
                if (returnStatus.getEvent() == Event.CAPITAL_CAPTURE) {
                    // GAME OVER
                    return returnStatus;
                }
            }
        }

        return new HeavenReturnStatus(true);
    }

    private HeavenReturnStatus moveUnit(int startRow, int startCol, int endRow, int endCol) {
        Unit unitToMove = battlefield.getUnitAtPosition(startRow, startCol);
        if (unitToMove == null) {
            return new HeavenReturnStatus(false, "Could not find unit at position (" + startRow + "," + startCol);
        }
        if (unitToMove.hasMoved()) {
            return new HeavenReturnStatus(false, "Unit has already moved");
        }
        if (unitToMove.getUnitClass() == UnitClass.RANGED && unitToMove.hasAttacked()) {
            return new HeavenReturnStatus(false, "Ranged units cannot move and attack on the same turn");
        }

        Map<Integer, Integer> validMoves = findLegalMoves(battlefield, unitToMove, startRow, startCol);
        if (validMoves.containsKey(endRow) && validMoves.containsValue(endCol)) {
            battlefield.removeUnit(startRow, startCol);
            battlefield.addUnit(unitToMove, endRow, endCol);
            unitToMove.setMoved(true);
            return new HeavenReturnStatus(true);
        } else {
            return new HeavenReturnStatus(false, "Invalid move. Cannot move to position (" + endRow + "," + endCol);
        }
    }

    private HeavenReturnStatus attackUnit(int attackerRow, int attackerCol, int defenderRow, int defenderCol) {
        Unit attacker = battlefield.getUnitAtPosition(attackerRow, attackerCol);
        Unit defender = battlefield.getUnitAtPosition(defenderRow, defenderCol);
        if (attacker == null || defender == null ) {
            return new HeavenReturnStatus(false, "Could not find both an attacking unit and defending unit");
        }
        if (attacker.getOwner() == defender.getOwner()) {
            return new HeavenReturnStatus(false, "Cannot attack friendly units");
        }
        if (attacker.hasAttacked()) {
            return new HeavenReturnStatus(false, "Unit has already attacked");
        }
        if (attacker.getUnitClass() == UnitClass.RANGED && attacker.hasMoved()) {
            return new HeavenReturnStatus(false, "Ranged units cannot move and attack on the same turn");
        }

        Map<Integer, Integer> validAttacks = findLegalAttacks(battlefield, attacker, attackerRow, attackerCol);
        if (validAttacks.containsKey(defenderRow) && validAttacks.containsValue(defenderCol)) {
            HeavenReturnStatus returnStatus = attacker.attackEnemyUnit(defender);
            if (attacker.getCurrentHealth() <= 0) {
                battlefield.removeUnit(attackerRow, attackerCol);
            }
            if (defender.getCurrentHealth() <= 0) {
                battlefield.removeUnit(defenderRow, defenderCol);
            }
            return returnStatus;
        }
        return new HeavenReturnStatus(false, "Unit cannot attack target square");
    }

    private HeavenReturnStatus createUnit(int row, int col, String identifier, Player player) {
        Structure structure = battlefield.getStructureAtPosition(row, col);

        if (structure == null) {
            return new HeavenReturnStatus(false, "No structure found at position (" + row + "," + col);
        }
        if (structure.getOwner() != player) {
            return new HeavenReturnStatus(false, "Cannot create unit on unowned structure");
        }
        if (!battlefield.isOpenPosition(row, col)) {
            return new HeavenReturnStatus(false, "Cannot create unit on occupied structure");
        }
        if (structure.getStructureType() != StructureType.FACTORY) {
            return new HeavenReturnStatus(false, "Unit - Structure creation mismatch");
        }
        if (!playerFunds.keySet().contains(player)) {
            return new HeavenReturnStatus(false, "Invalid player provided");
        }

        Unit unitToCreate = new Unit(unitTypes.get(identifier), player);
        int unitCost = unitToCreate.getCost();
        int currentPlayerFunds = playerFunds.get(player);
        if (unitCost > currentPlayerFunds) {
            return new HeavenReturnStatus(false, player + " cannot afford purchase");
        }

        playerFunds.replace(player, currentPlayerFunds - unitCost);
        battlefield.addUnit(unitToCreate, row, col);

        return new HeavenReturnStatus(true);
    }
}

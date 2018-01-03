package classes;

import battlefields.BattlefieldSpecification;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unitTypes.UnitType;
import util.HeavenConstants.*;
import util.HeavenReturnStatus;
import util.HeavenUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static util.HeavenUtils.findLegalAttacks;
import static util.HeavenUtils.findLegalMoves;

public class GameController {
    private Battlefield battlefield;
    private HashMap<String, UnitType> unitTypes;

    private int turnCount;
    private HashMap<Player, Integer> playerFunds;

    private StringBuilder gameLog;
    private Stage stage;
    private Scene scene;
    private Label gameInfoLabel;
    private Label squareInfoLabel;
    private VBox buttonsBox;

    public GameController(BattlefieldSpecification battlefieldSpecification, Stage stage) {
        this.turnCount = 0;
        this.battlefield = new Battlefield(battlefieldSpecification);
        this.playerFunds = new HashMap<>();
        this.playerFunds.put(Player.PLAYER_ONE, 0);
        this.playerFunds.put(Player.PLAYER_TWO, 0);

        this.unitTypes = new HashMap<>();
        List<UnitType> allUnitTypes = HeavenUtils.getAllUnitTypes();
        for (UnitType unitType : allUnitTypes) {
            this.unitTypes.put(unitType.getIdentifier(), unitType);
        }

        this.gameLog = new StringBuilder();
        this.stage = stage;
        setupGui();
    }

    public HeavenReturnStatus nextTurn() {
        turnCount++;
        gameLog.append("-------------------------- Turn " + turnCount + " --------------------------\n");
        gameLog.append("~~~~~ Player One ~~~~~~\n");

        if (true) {
            return new HeavenReturnStatus(true);
        }
        HeavenReturnStatus returnStatus = nextTurn(Player.PLAYER_ONE);
        if (returnStatus.getEvent() == Event.CAPITAL_CAPTURE) {
            return returnStatus;
        } else if (!returnStatus.getSuccessStatus()) {
            return returnStatus;
        }

        gameLog.append("~~~~~ Player Two ~~~~~\n");
        returnStatus = nextTurn(Player.PLAYER_TWO);
        if (returnStatus.getEvent() == Event.CAPITAL_CAPTURE) {
            return returnStatus;
        } else if (!returnStatus.getSuccessStatus()) {
            return returnStatus;
        }

        return battlefield.resetUnitActivity();
    }

    public String getGameLog() {
        return gameLog.toString();
    }

    private HeavenReturnStatus nextTurn(Player player) {
        boolean endOfTurn = false;

        // Beginning of each player's turn: calculate funds
        int totalIncome = 0;
        for (Structure structure : battlefield.getStructures()) {
            if (structure.getOwner() == player) {
                totalIncome += 1000;
            }
        }
        int updatedFunds = playerFunds.get(player) + totalIncome;
        playerFunds.replace(player, updatedFunds);

        while (!endOfTurn) {
            // Listen for inputs, executing each as a move, until the player declares end of his turn.
            gameInfoLabel.setText("Temporary UI for making moves:\nend - End Turn\nmR,C:R,C - move unit to new position" +
                    "\naR,C:R,C - attack with unit to position\ncR,C:I - create unit at structure\n" +
                    "show - Show current battlefield state\n>");
            String input = "NULL";

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

                HeavenReturnStatus returnStatus = moveUnit(startRow, startCol, endRow, endCol, player);
                if (!returnStatus.getSuccessStatus()) {
                    gameInfoLabel.setText(returnStatus.getErrorMsg());
                }
                gameLog.append(input + " -> " + endRow + "," + endCol + "\n");
            } else if (input.startsWith("a")) {
                // Attempting to attack another unit.
                String combinedCoords = input.substring(1, input.length());
                String startCoords = combinedCoords.split(":")[0];
                String endCoords = combinedCoords.split(":")[1];
                int startRow = Integer.parseInt(startCoords.split(",")[0]);
                int startCol = Integer.parseInt(startCoords.split(",")[1]);
                int endRow = Integer.parseInt(endCoords.split(",")[0]);
                int endCol = Integer.parseInt(endCoords.split(",")[1]);

                HeavenReturnStatus returnStatus = attackUnit(startRow, startCol, endRow, endCol, player);
                if (!returnStatus.getSuccessStatus()) {
                    gameInfoLabel.setText(returnStatus.getErrorMsg());
                }
                gameLog.append(input + " -> " + endRow + "," + endCol + "\n");
            } else if (input.startsWith("c")) {
                // Attempting to create unit at structure.
                String coords = input.substring(1, input.length()).split(":")[0];
                String identifier = input.substring(1, input.length()).split(":")[1];
                int row = Integer.parseInt(coords.split(",")[0]);
                int col = Integer.parseInt(coords.split(",")[1]);

                HeavenReturnStatus returnStatus = createUnit(row, col, identifier, player);
                if (!returnStatus.getSuccessStatus()) {
                    gameInfoLabel.setText(returnStatus.getErrorMsg());
                }
                gameLog.append(input);
            } else if (input.equals("show")) {
                gameInfoLabel.setText(battlefield.gridToString());
            } else {
                gameInfoLabel.setText("Could not parse input");
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

    /**
     * ----------------------|
     * |             |       | <------Info Panel
     * |             |       |
     * |             |-------|
     * |             |       | <------Buttons
     * |             |       |
     * |             |       |
     * |---------------------|
     */
    private void setupGui() {
        this.gameInfoLabel = new Label(getGameInfoText(Player.PLAYER_ONE));
        squareInfoLabel = new Label("\n\n\n\n");
        buttonsBox = new VBox();
        createActionButtonsForSquare(null);

        VBox leftBox = gridToBox(battlefield.getGrid());
        VBox rightBox = new VBox(gameInfoLabel, squareInfoLabel, buttonsBox);

        HBox mainBox = new HBox(leftBox, rightBox);

        this.scene = new Scene(mainBox, 1000, 600);

        stage.setTitle("Heaven Game Stage");
        stage.setScene(scene);
        stage.show();
    }

    private String getGameInfoText(Player player) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("~~~Turn ");
        stringBuilder.append(turnCount);
        stringBuilder.append("~~~");
        stringBuilder.append("\nPLAYER_ONE: ");
        stringBuilder.append(playerFunds.get(Player.PLAYER_ONE));
        if (player == Player.PLAYER_ONE) {
            stringBuilder.append(" <---");
        }
        stringBuilder.append("\nPLAYER_TWO: ");
        stringBuilder.append(playerFunds.get(Player.PLAYER_TWO));
        if (player == Player.PLAYER_TWO) {
            stringBuilder.append(" <---");
        }
        stringBuilder.append("\n\n");

        return stringBuilder.toString();
    }

    private VBox gridToBox(Square[][] grid) {
        VBox boxOfRows = new VBox();
        for (int r = 0; r < grid.length; r++) {
            HBox singleRow = new HBox();
            for (int c = 0; c < grid[r].length; c++) {
                singleRow.getChildren().add(createGridButtonForSquare(grid[r][c]));
            }
            boxOfRows.getChildren().add(singleRow);
        }
        return boxOfRows;
    }

    private Button createGridButtonForSquare(Square square) {
        String buttonText = square.toString();
        Button newButton = new Button(buttonText);
        //TODO: Fix dimensions of button and improve buttonText

        newButton.setOnAction(value -> {
            squareInfoLabel.setText(square.getFullInfo());
            createActionButtonsForSquare(square);
        });

        return newButton;
    }

    private void createActionButtonsForSquare(Square square) {
        Button endTurnButton = new Button("End Turn");
        buttonsBox.getChildren().remove(0, buttonsBox.getChildren().size());
        buttonsBox.getChildren().add(endTurnButton);
        if (square == null) {
            return;
        } else if (square.getUnitOnSquare() != null) {
            Button moveButton = new Button("Move");
            Button attackButton = new Button("Attack");
            buttonsBox.getChildren().addAll(moveButton, attackButton);
        } else if (square.getStructureOnSquare() != null && square.getStructureOnSquare().getStructureType() == StructureType.FACTORY) {
            Button createButton = new Button("Create");
            buttonsBox.getChildren().add(createButton);
        }
    }


    private HeavenReturnStatus moveUnit(int startRow, int startCol, int endRow, int endCol, Player player) {
        Unit unitToMove = battlefield.getUnitAtPosition(startRow, startCol);
        if (unitToMove == null) {
            return new HeavenReturnStatus(false, "Could not find unit at position (" + startRow + "," + startCol);
        }
        if (unitToMove.getOwner() != player) {
            return new HeavenReturnStatus(false, "Cannot move unowned unit");
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

    private HeavenReturnStatus attackUnit(int attackerRow, int attackerCol, int defenderRow, int defenderCol, Player player) {
        Unit attacker = battlefield.getUnitAtPosition(attackerRow, attackerCol);
        Unit defender = battlefield.getUnitAtPosition(defenderRow, defenderCol);
        if (attacker == null || defender == null ) {
            return new HeavenReturnStatus(false, "Could not find both an attacking unit and defending unit");
        }
        if (attacker.getOwner() != player) {
            return new HeavenReturnStatus(false, "Cannot attack with unowned unit");
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
        if (structure.getStructureType() != StructureType.FACTORY) {
            return new HeavenReturnStatus(false, "Unit - Structure creation mismatch");
        }
        if (structure.getOwner() != player) {
            return new HeavenReturnStatus(false, "Cannot create unit on unowned structure");
        }
        if (!battlefield.isOpenPosition(row, col)) {
            return new HeavenReturnStatus(false, "Cannot create unit on occupied structure");
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

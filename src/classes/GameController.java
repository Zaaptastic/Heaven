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
import util.SearchCoordinate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static util.HeavenUtils.findLegalAttacks;
import static util.HeavenUtils.findLegalMoves;
import static util.HeavenUtils.isSearchCoordinateInSelection;

public class GameController {
    private Battlefield battlefield;
    private HashMap<String, UnitType> unitTypes;

    private int turnCount;
    private HashMap<Player, Integer> playerFunds;

    private Player currentPlayer;
    private int currentSelectedRow;
    private int currentSelectedCol;

    // GUI-related fields
    private StringBuilder gameLog;
    private Stage stage;
    private Scene scene;
    private Label gameInfoLabel;
    private Label squareInfoLabel;
    private VBox buttonsBox;
    private Label debugLabel;
    private VBox gridBox;
    private VBox infoBox;

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
        this.currentPlayer = Player.PLAYER_ONE;
        this.currentSelectedRow = -1;
        this.currentSelectedCol = -1;
        setupGui(null);
    }

    public String getGameLog() {
        return gameLog.toString();
    }

    public HeavenReturnStatus initializeGame() {
        turnCount++;
        int totalIncomePlayerOne = playerFunds.get(Player.PLAYER_ONE);
        int totalIncomePlayerTwo = playerFunds.get(Player.PLAYER_TWO);
        for (Structure structure : battlefield.getStructures()) {
            if (structure.getOwner() == Player.PLAYER_ONE) {
                totalIncomePlayerOne += 1000;
            } else if (structure.getOwner() == Player.PLAYER_TWO) {
                totalIncomePlayerTwo += 1000;
            }
        }
        playerFunds.replace(Player.PLAYER_ONE, totalIncomePlayerOne);
        playerFunds.replace(Player.PLAYER_TWO, totalIncomePlayerTwo);

        // Update Game Info
        gameInfoLabel.setText(getGameInfoText());
        // TODO: Debugging console.
        /*while (!endOfTurn) {
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
        }*/

        return new HeavenReturnStatus(true);
    }

    /**
     * |---------------------|
     * |             |       | <------Info Panel
     * |             |       |
     * |             |-------|
     * |             |       | <------Buttons
     * |             |       |
     * |             |       |
     * |---------------------|
     */
    private void setupGui(ArrayList<SearchCoordinate> selection) {
        this.gameInfoLabel = new Label(getGameInfoText());
        squareInfoLabel = new Label("\n\n\n\n");
        buttonsBox = new VBox();
        Square selectedSquare = null;
        if (currentSelectedRow != -1 && currentSelectedCol != -1) {
            selectedSquare = battlefield.getGrid()[currentSelectedRow][currentSelectedCol];
            squareInfoLabel.setText(selectedSquare.getFullInfo());
            createActionButtonsForSquare(selectedSquare);
        } else {
            squareInfoLabel.setText("\n\n\n\n");
            createActionButtonsForSquare(null);
        }

        debugLabel = new Label("");

        gridBox = gridToBox(battlefield.getGrid(), selection);
        infoBox = new VBox(gameInfoLabel, squareInfoLabel, buttonsBox, debugLabel);

        HBox mainBox = new HBox(gridBox, infoBox);

        this.scene = new Scene(mainBox, 1200, 600);

        stage.setTitle("Heaven Game Stage");
        stage.setScene(scene);
        stage.show();
    }

    private String getGameInfoText() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("~~~Turn ");
        stringBuilder.append(turnCount);
        stringBuilder.append("~~~");
        stringBuilder.append("\nPLAYER_ONE: ");
        stringBuilder.append(playerFunds.get(Player.PLAYER_ONE));
        if (currentPlayer == Player.PLAYER_ONE) {
            stringBuilder.append(" <---");
        }
        stringBuilder.append("\nPLAYER_TWO: ");
        stringBuilder.append(playerFunds.get(Player.PLAYER_TWO));
        if (currentPlayer == Player.PLAYER_TWO) {
            stringBuilder.append(" <---");
        }
        stringBuilder.append("\n\n");

        return stringBuilder.toString();
    }

    private VBox gridToBox(Square[][] grid, ArrayList<SearchCoordinate> selection) {
        Button invisibleButton = new Button();
        invisibleButton.setOpacity(0.0);
        VBox boxOfRows = new VBox(invisibleButton);
        for (int r = 0; r < grid.length; r++) {
            HBox singleRow = new HBox();
            for (int c = 0; c < grid[r].length; c++) {
                Button buttonToAdd = createGridButtonForSquare(grid[r][c]);
                if (r == currentSelectedRow && c == currentSelectedCol) {
                    buttonToAdd.setDefaultButton(true);
                }
                if (selection != null && isSearchCoordinateInSelection(selection, r, c)) {
                    buttonToAdd.setOpacity(0.5);
                }
                singleRow.getChildren().add(buttonToAdd);
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
            currentSelectedRow = square.getRow();
            currentSelectedCol = square.getCol();
            setupGui(null);
            createActionButtonsForSquare(square);
        });

        return newButton;
    }

    private void createActionButtonsForSquare(Square square) {
        // TODO: Actions for all of these buttons
        Button endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(value -> {
            endTurn();
        });
        buttonsBox.getChildren().remove(0, buttonsBox.getChildren().size());
        buttonsBox.getChildren().add(endTurnButton);
        if (square == null) {
            return;
        } else if (square.getUnitOnSquare() != null) {
            Button moveButton = new Button("Move");
            moveButton.setOnAction(value -> {
                ArrayList<SearchCoordinate> selection = HeavenUtils.findLegalMoves(battlefield, square.getUnitOnSquare(),square.getRow(), square.getCol());
                setupGui(selection);
            });
            Button attackButton = new Button("Attack");
            attackButton.setOnAction(value -> {
                ArrayList<SearchCoordinate> selection = HeavenUtils.findLegalAttacks(battlefield, square.getUnitOnSquare(),square.getRow(), square.getCol());
                setupGui(selection);
            });
            buttonsBox.getChildren().addAll(moveButton, attackButton);
        } else if (square.getStructureOnSquare() != null && square.getStructureOnSquare().getStructureType() == StructureType.FACTORY) {
            Button createButton = new Button("Create");
            createButton.setOnAction(value -> {
                createUnit(square.getRow(), square.getCol(), "I");
                setupGui(null);
            });
            buttonsBox.getChildren().add(createButton);
        }
    }


    private HeavenReturnStatus moveUnit(int startRow, int startCol, int endRow, int endCol) {
        Unit unitToMove = battlefield.getUnitAtPosition(startRow, startCol);
        if (unitToMove == null) {
            return new HeavenReturnStatus(false, "Could not find unit at position (" + startRow + "," + startCol);
        }
        if (unitToMove.getOwner() != currentPlayer) {
            return new HeavenReturnStatus(false, "Cannot move unowned unit");
        }
        if (unitToMove.hasMoved()) {
            return new HeavenReturnStatus(false, "Unit has already moved");
        }
        if (unitToMove.getUnitClass() == UnitClass.RANGED && unitToMove.hasAttacked()) {
            return new HeavenReturnStatus(false, "Ranged units cannot move and attack on the same turn");
        }

        ArrayList<SearchCoordinate> validMoves = findLegalMoves(battlefield, unitToMove, startRow, startCol);
        if (isSearchCoordinateInSelection(validMoves, endRow, endCol)) {
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
        if (attacker.getOwner() != currentPlayer) {
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

        ArrayList<SearchCoordinate> validAttacks = findLegalAttacks(battlefield, attacker, attackerRow, attackerCol);
        if (isSearchCoordinateInSelection(validAttacks, defenderRow, defenderCol)) {
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

    private HeavenReturnStatus createUnit(int row, int col, String identifier) {
        Structure structure = battlefield.getStructureAtPosition(row, col);

        if (structure == null) {
            return new HeavenReturnStatus(false, "No structure found at position (" + row + "," + col);
        }
        if (structure.getStructureType() != StructureType.FACTORY) {
            return new HeavenReturnStatus(false, "Unit - Structure creation mismatch");
        }
        if (structure.getOwner() != currentPlayer) {
            return new HeavenReturnStatus(false, "Cannot create unit on unowned structure");
        }
        if (!battlefield.isOpenPosition(row, col, false)) {
            return new HeavenReturnStatus(false, "Cannot create unit on occupied structure");
        }
        if (!playerFunds.keySet().contains(currentPlayer)) {
            return new HeavenReturnStatus(false, "Invalid player provided");
        }

        Unit unitToCreate = new Unit(unitTypes.get(identifier), currentPlayer);
        int unitCost = unitToCreate.getCost();
        int currentPlayerFunds = playerFunds.get(currentPlayer);
        if (unitCost > currentPlayerFunds) {
            return new HeavenReturnStatus(false, currentPlayerFunds + " cannot afford purchase");
        }

        playerFunds.replace(currentPlayer, currentPlayerFunds - unitCost);
        battlefield.addUnit(unitToCreate, row, col);

        return new HeavenReturnStatus(true);
    }

    /**
     * Perform actions needed to handle end of turn logistics including:
     *   -Reset movement and attack flags for last player's units
     *   -Calculate and record income
     *   -Updated currentPlayer
     *   -Redraw the Gui and update info
     *   -Increment turnCount
     *   -Clear currentSelectedSquare values
     * @return
     */
    private HeavenReturnStatus endTurn() {
        //TODO: Perhaps in the future this only needs to flip units belong to the previous player.
        battlefield.resetUnitActivity();
        currentSelectedRow = -1;
        currentSelectedCol = -1;

        // Recalculate structure health
        for (Structure structure : battlefield.getStructures()) {
            Unit unit = battlefield.getUnitAtPosition(structure.getRow(), structure.getCol());
            if (unit == null || unit.getOwner() == structure.getOwner()) {
                structure.increaseHealth();
                continue;
            }

            // TODO: Move this to an Action so that the healing can be moved to the else-block
            if (unit.getUnitType().getIdentifier().equals("I")) {
                HeavenReturnStatus returnStatus = structure.decreaseHealth(currentPlayer, unit.getCurrentHealth());
                if (returnStatus.getEvent() == Event.CAPITAL_CAPTURE) {
                    // GAME OVER
                    return returnStatus;
                }
            }
        }

        if (currentPlayer == Player.PLAYER_ONE) {
            currentPlayer = Player.PLAYER_TWO;
        } else {
            currentPlayer = Player.PLAYER_ONE;

            turnCount++;

            // Beginning of each player's turn: calculate funds
            int totalIncomePlayerOne = playerFunds.get(Player.PLAYER_ONE);
            int totalIncomePlayerTwo = playerFunds.get(Player.PLAYER_TWO);
            for (Structure structure : battlefield.getStructures()) {
                if (structure.getOwner() == Player.PLAYER_ONE) {
                    totalIncomePlayerOne += 1000;
                } else if (structure.getOwner() == Player.PLAYER_TWO) {
                    totalIncomePlayerTwo += 1000;
                }
            }
            playerFunds.replace(Player.PLAYER_ONE, totalIncomePlayerOne);
            playerFunds.replace(Player.PLAYER_TWO, totalIncomePlayerTwo);

        }

        setupGui(null);

        return new HeavenReturnStatus(true);
    }
}

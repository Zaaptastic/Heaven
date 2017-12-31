package classes;

import util.HeavenConstants.*;
import util.HeavenReturnStatus;

public class Structure {
    private StructureType structureType;
    private int currentHealth;
    private int maxHealth;
    private Player owner;
    private int row;
    private int col;

    public Structure(StructureType structureType, int row, int col) {
        this.structureType = structureType;
        this.currentHealth = 100;
        this.maxHealth = 100;
        this.row = row;
        this.col = col;
        this.owner = null;
    }

    public Structure(StructureType structureType, int row, int col, Player player) {
        this.structureType = structureType;
        this.currentHealth = 100;
        this.maxHealth = 100;
        this.row = row;
        this.col = col;
        this.owner = player;
    }

    public HeavenReturnStatus decreaseHealth(Player attackingPlayer, int damageToTake) {
        currentHealth = currentHealth - damageToTake;
        if (currentHealth <= 0) {
            // Structure has been captured by attackingPlayer.
            owner = attackingPlayer;
            currentHealth = 50;
            if (structureType == StructureType.CAPITAL) {
                // Send a unique HeavenReturnStatus to indicate a capital has been flipped.
                return new HeavenReturnStatus(true, Event.CAPITAL_CAPTURE);
            }
        }

        return new HeavenReturnStatus(true);
    }

    public HeavenReturnStatus increaseHealth() {
        if (currentHealth < 100) {
            currentHealth += 10;
            if (currentHealth > 100) {
                currentHealth = 100;
            }
        }

        return new HeavenReturnStatus(true);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return getMaxHealth();
    }

    public Player getOwner() {
        return owner;
    }

    public StructureType getStructureType() {
        return structureType;
    }
}
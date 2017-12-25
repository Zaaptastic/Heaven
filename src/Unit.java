import unitTypes.UnitType;
import util.HeavenConstants.UnitClass;
import util.HeavenConstants.Player;
import util.HeavenUtils;

public class Unit {
    private UnitType unitType;
    private UnitClass unitClass;
    private Player owningPlayer;
    private int currentHealth;
    private int maximumHealth;
    private int attackValue;
    private int defenseValue;
    private int range;
    private int movementSpeed;


    public Unit (UnitType unitType, Player player) {
        this.unitType = unitType;
        this.unitClass = unitType.getUnitClass();
        this.owningPlayer = player;

        // These attributes are set here to allow for Player-specific attributes later
        this.maximumHealth = unitType.getMaximumHealth();
        this.currentHealth = maximumHealth;
        this.attackValue = unitType.getAttackValue();
        this.defenseValue = unitType.getDefenseValue();
        this.range = unitType.getRange();
        this.movementSpeed = unitType.getMovementSpeed();
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public UnitClass getUnitClass() {
        return unitClass;
    }

    public Player getOwningPlayer() {
        return owningPlayer;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaximumHealth() {
        return maximumHealth;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public int getRange() {
        return range;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }



}
package unitTypes;

import util.HeavenConstants;

public class UnitType {
    private int maximumHealth;
    private int attackValue;
    private int defenseValue;
    private int range;
    private int minimumRange;
    private int movementSpeed;
    private HeavenConstants.UnitClass unitClass;
    private int cost;
    private String identifier;
    private String name;

    UnitType(int maximumHealth, int attackValue, int defenseValue, int range, int minimumRange, int movementSpeed,
             HeavenConstants.UnitClass unitClass, int cost, String identifier, String name){
        this.maximumHealth = maximumHealth;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.range = range;
        this.minimumRange = minimumRange;
        this.movementSpeed = movementSpeed;
        this.unitClass = unitClass;
        this.cost = cost;
        this.identifier = identifier;
        this.name = name;
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

    public int getMinimumRange() {
        return minimumRange;
    }

    public int getMovementSpeed() {
        return movementSpeed;
    }

    public int getMaximumHealth() {
        return  maximumHealth;
    }

    public HeavenConstants.UnitClass getUnitClass() {
        return unitClass;
    }

    public int getCost() {
        return cost;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }
}

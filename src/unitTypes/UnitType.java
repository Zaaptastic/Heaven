package unitTypes;

import util.HeavenConstants;

public class UnitType {
    private int maximumHealth;
    private int attackValue;
    private int defenseValue;
    private int range;
    private int movementSpeed;
    private HeavenConstants.UnitClass unitClass;
    private int cost;

    UnitType(int maximumHealth, int attackValue, int defenseValue, int range, int movementSpeed, HeavenConstants.UnitClass unitClass, int cost){
        this.maximumHealth = maximumHealth;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.range = range;
        this.movementSpeed = movementSpeed;
        this.unitClass = unitClass;
        this.cost = cost;
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

    public int getMaximumHealth() {
        return  maximumHealth;
    }

    public HeavenConstants.UnitClass getUnitClass() {
        return unitClass;
    }

    public int getCost() {
        return cost;
    }
}

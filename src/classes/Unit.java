package classes;

import unitTypes.UnitType;
import util.HeavenConstants.UnitClass;
import util.HeavenConstants.Player;
import util.HeavenReturnStatus;

public class Unit {
    private UnitType unitType;
    private UnitClass unitClass;
    private Player owner;
    private int currentHealth;
    private int maximumHealth;
    private int attackValue;
    private int defenseValue;
    private int range;
    private int movementSpeed;
    private int cost;
    private boolean moved;
    private boolean attacked;


    public Unit (UnitType unitType, Player player) {
        this.unitType = unitType;
        this.unitClass = unitType.getUnitClass();
        this.owner = player;

        // These attributes are set here to allow for Player-specific attributes later
        this.maximumHealth = unitType.getMaximumHealth();
        this.currentHealth = maximumHealth;
        this.attackValue = unitType.getAttackValue();
        this.defenseValue = unitType.getDefenseValue();
        this.range = unitType.getRange();
        this.movementSpeed = unitType.getMovementSpeed();
        this.cost = unitType.getCost();

        this.moved = false;
        this.attacked = false;
    }

    public HeavenReturnStatus attackEnemyUnit(Unit enemyUnit) {
        if (enemyUnit.owner == this.owner) {
            return new HeavenReturnStatus(false, "Cannot attack friendly unit");
        }

        enemyUnit.setCurrentHealth(enemyUnit.currentHealth - calculateDamageToDeal(attackValue, enemyUnit.getDefenseValue(), currentHealth, maximumHealth));

        // Retaliation damage, but only if the enemy unit is alive and not ranged.
        if (enemyUnit.getCurrentHealth() > 0 && enemyUnit.getUnitClass() != UnitClass.RANGED) {
            currentHealth = currentHealth - calculateDamageToDeal(enemyUnit.getAttackValue(), defenseValue, enemyUnit.getCurrentHealth(), enemyUnit.getMaximumHealth());
        }

        // Cannot move after attacking
        moved = true;
        attacked = true;

        return new HeavenReturnStatus(true);
    }

    private int calculateDamageToDeal(int attackerAttackValue, int defenderDefenseValue, int attackerCurrentHealth, int attackerMaximumHealth) {
        double damageMultiplier = (double) (attackerAttackValue / defenderDefenseValue);
        double fatigueMultiplier = (double) (attackerCurrentHealth / attackerMaximumHealth);
        if (damageMultiplier > 1.0) {
            damageMultiplier = 1;
        }

        return (int) Math.ceil(attackerAttackValue * damageMultiplier * fatigueMultiplier);
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public UnitClass getUnitClass() {
        return unitClass;
    }

    public Player getOwner() {
        return owner;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
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

    public int getCost() {
        return cost;
    }

    public boolean hasAttacked() {
        return attacked;
    }

    public void setAttacked(boolean attacked) {
        this.attacked = attacked;
    }

    public boolean hasMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }
}
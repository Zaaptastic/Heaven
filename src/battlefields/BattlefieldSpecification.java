package battlefields;

import classes.Structure;

import java.util.ArrayList;

public class BattlefieldSpecification {
    int length;
    int width;
    ArrayList<Structure> structures;

    public BattlefieldSpecification() {
        length = 0;
        width = 0;
        structures = new ArrayList<>();

        //TODO: Add support for balance infantry
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }
}

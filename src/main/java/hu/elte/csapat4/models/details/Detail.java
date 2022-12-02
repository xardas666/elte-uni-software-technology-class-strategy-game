package hu.elte.csapat4.models.details;

import hu.elte.csapat4.models.map.ActionType;

import java.util.ArrayList;

public class Detail {
    private final Cost cost;
    private final int life;
    private final ArrayList<ActionType> actions;

    public Detail(Cost cost, int life, ArrayList<ActionType> actions) {
        this.cost = cost;
        this.life = life;
        this.actions = actions;
    }

    public Cost getCost() {
        return cost;
    }

    public int getLife() {
        return life;
    }

    public ArrayList<ActionType> getActions() {
        return actions;
    }
}

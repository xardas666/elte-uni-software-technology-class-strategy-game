package hu.elte.csapat4.models.details;

import hu.elte.csapat4.models.map.ActionType;

import java.util.ArrayList;

public class CharacterDetails extends Detail {

    private final int hit;
    private final int shield;
    private final int distance;


    public CharacterDetails(int hit, int shield, int distance, int life, Cost cost, ArrayList<ActionType> actions) {
        super(cost, life, actions);
        this.hit = hit;
        this.shield = shield;
        this.distance = distance;
    }

    public int getHit() {
        return hit;
    }

    public int getShield() {
        return shield;
    }

    public int getDistance() {
        return distance;
    }

}

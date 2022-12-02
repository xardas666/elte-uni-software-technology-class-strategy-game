package hu.elte.csapat4.models.details;


import hu.elte.csapat4.models.map.ActionType;

import java.util.ArrayList;

public class BuildingDetails extends Detail {

    private final int buildingTime;

    public BuildingDetails(int buildingTime, int life, Cost cost, ArrayList<ActionType> actions) {
        super(cost, life, actions);
        this.buildingTime = buildingTime;
    }

    public int getBuildingTime() {
        return buildingTime;
    }

}

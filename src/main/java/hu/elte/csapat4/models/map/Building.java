package hu.elte.csapat4.models.map;

import hu.elte.csapat4.models.Player;
import hu.elte.csapat4.models.details.BuildingDetails;
import hu.elte.csapat4.settings.ModelSettings;
import hu.elte.csapat4.settings.RangeType;

import java.util.ArrayList;

public class Building implements IMapObject {
    private final Player player;
    //From detail
    private final int buildingTime;
    private int life;
    private int originalLife;
    private int progress;
    private final ArrayList<ActionType> actions;
    private final Coordinate position;
    private final MapObjectType mapObjectType;

    public Building(MapObjectType mapObjectType, Player player, Coordinate position) {
        this.mapObjectType = mapObjectType;
        this.player = player;
        this.position = position;

        BuildingDetails details = (BuildingDetails) ModelSettings.getDetailFor(mapObjectType);
        buildingTime = details.getBuildingTime();
        life = details.getLife();
        originalLife = details.getLife();
        progress = 0;
        actions = details.getActions();
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Coordinate getPosition() {
        return position;
    }

    @Override
    public boolean isStanding() {
        if (life >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<ActionType> getActions() {
        if (!isBuilt()) {
            return new ArrayList<>();
        } else {
            return actions;
        }
    }

    @Override
    public void addDamage(IMapObject attackerMapObject) {
        Character attacker = ((Character) attackerMapObject);
        life -= attacker.getHitPoints();
    }

    @Override
    public int getLife() {
        return life;
    }

    @Override
    public int getShield() {
        return 0;
    }

    @Override
    public int getHitPoints() {
        return 0;
    }

    @Override
    public String infoText() {
        return mapObjectType.name() +
                " Health: " + originalLife+"/"+life +
                " progress: " + buildingTime + "/" + progress +
                " at: " + position;
    }

    public void progress(Integer workerCount) {
        if (!isBuilt()) {
            progress+=workerCount;
        }
    }

    public void doProduction(Integer workerCount) {
        if (isBuilt()) {
            switch (mapObjectType) {
                case MINE:
                    player.addMaterial(MaterialType.GOLD, workerCount * RangeType.L.getValue());
                    break;
                case FARM:
                    player.addMaterial(MaterialType.FOOD, workerCount * RangeType.L.getValue());
                    break;
                case CABIN:
                    player.addMaterial(MaterialType.WOOD, workerCount * RangeType.L.getValue());
                    break;
            }
        }
    }

    @Override
    public MapObjectType getType() {
        return mapObjectType;
    }

    public boolean isBuilt(){
        return progress >= buildingTime;
    }
}


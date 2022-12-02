package hu.elte.csapat4.models.map;

import hu.elte.csapat4.models.Player;
import hu.elte.csapat4.models.details.TerrainDetails;
import hu.elte.csapat4.settings.ModelSettings;

import java.util.ArrayList;

public class Terrain implements IMapObject {
    private final ArrayList<ActionType> actions;
    private final Coordinate position;
    private final MapObjectType mapObjectType;

    public Terrain(MapObjectType mapObjectType, Coordinate position) {
        this.position = position;
        this.mapObjectType = mapObjectType;

        TerrainDetails details = (TerrainDetails) ModelSettings.getDetailFor(mapObjectType);
        this.actions = details.getActions();
    }

    @Override
    public MapObjectType getType() {
        return mapObjectType;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public Coordinate getPosition() {
        return position;
    }

    @Override
    public boolean isStanding() {
        return true;
    }

    @Override
    public ArrayList<ActionType> getActions() {
        return actions;
    }

    @Override
    public void addDamage(IMapObject attacker) {
    }

    @Override
    public int getLife() {
        return 0;
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
                " at: " +   position;
    }

}

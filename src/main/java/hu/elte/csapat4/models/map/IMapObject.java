package hu.elte.csapat4.models.map;

import hu.elte.csapat4.models.Player;

import java.util.ArrayList;

public interface IMapObject {

    MapObjectType getType();

    Player getPlayer();

    Coordinate getPosition();

    boolean isStanding();

    ArrayList<ActionType> getActions();

    void addDamage(IMapObject attacker);

    int getLife();

    int getShield();

    int getHitPoints();

    String infoText();

}

